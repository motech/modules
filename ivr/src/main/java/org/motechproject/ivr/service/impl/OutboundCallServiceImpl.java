package org.motechproject.ivr.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.ivr.service.CallInitiationException;
import org.motechproject.ivr.event.EventParams;
import org.motechproject.ivr.event.EventSubjects;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.CallDirection;
import org.motechproject.ivr.domain.CallStatus;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.HttpMethod;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.repository.ConfigDataService;
import org.motechproject.ivr.service.OutboundCallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Generates & sends an HTTP request to an IVR provider to trigger an outbound call
 */
@Service("outboundCallService")
public class OutboundCallServiceImpl implements OutboundCallService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundCallServiceImpl.class);
    private ConfigDataService configDataService;
    private CallDetailRecordDataService callDetailRecordDataService;
    private EventRelay eventRelay;
    private StatusMessageService statusMessageService;
    private static final String MODULE_NAME = "ivr";

    @Autowired
    public OutboundCallServiceImpl(ConfigDataService configDataService, StatusMessageService statusMessageService,
                                   CallDetailRecordDataService callDetailRecordDataService, EventRelay eventRelay) {
        this.configDataService = configDataService;
        this.statusMessageService = statusMessageService;
        this.callDetailRecordDataService = callDetailRecordDataService;
        this.eventRelay = eventRelay;
    }

    private void addCallDetailRecord(CallStatus callStatus, Config config, Map<String, String> params,
                                     String motechCallId) {
        LOGGER.debug(String.format("addCallDetailRecord(config = %s, params = %s, motechCallId = %s)", config.getName(),
                params.toString(), motechCallId));

        callDetailRecordDataService.create(new CallDetailRecord(config.getName(), null, params.get("from"),
                params.get("to"), CallDirection.OUTBOUND, callStatus, motechCallId, null, params));
    }

    @Override
    public void initiateCall(String configName, Map<String, String> parameters) {
        LOGGER.debug("initiateCall(configName = {}, params = {})", configName, parameters);

        Config config = null;
        Map<String, String> params = new HashMap<>(parameters);

        config = configDataService.findByName(configName);
        LOGGER.debug("initiateCall(): read the following config from the database: {}", config);
        if (null == config) {
            String message = String.format("Invalid config: {}", configName);
            LOGGER.warn(message);
            statusMessageService.warn(message, MODULE_NAME);
            throw new CallInitiationException(message);
        }

        String motechCallId = UUID.randomUUID().toString();
        Map<String, String> completeParams = new HashMap<>(params);
        completeParams.put("motechCallId", motechCallId);

        HttpUriRequest request = generateHttpRequest(config, completeParams);
        HttpResponse response;
        try {
            response = new DefaultHttpClient().execute(request);
        } catch (Exception e) {
            String message = String.format("Could not initiate call, unexpected exception: %s", e.toString());
            LOGGER.info(message);
            statusMessageService.warn(message, MODULE_NAME);
            params.put("ErrorMessage", message);
            addCallDetailRecord(CallStatus.FAILED, config, params, motechCallId);
            throw new CallInitiationException(message, e);
        }
        StatusLine statusLine = response.getStatusLine();

        //todo: it's possible that some IVR providers return an HTTP 200 and an error code in the response body.
        //todo: If we encounter such a provider, we'll have to beef up the response processing here
        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            String message = String.format("Could not initiate call: %s", statusLine.toString());
            LOGGER.info(message);
            statusMessageService.warn(message, MODULE_NAME);
            params.put("ErrorMessage", message);
            addCallDetailRecord(CallStatus.FAILED, config, params, motechCallId);
            throw new CallInitiationException(message);
        }

        // Add a CDR to the database
        addCallDetailRecord(CallStatus.MOTECH_INITIATED, config, params, motechCallId);

        // Generate a MOTECH event
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put(EventParams.CONFIG, config.getName());
        eventParams.put(EventParams.MOTECH_TIMESTAMP, CallDetailRecord.getCurrentTimestamp());
        if (params.containsKey("to")) {
            eventParams.put(EventParams.TO, params.get("to"));
        }
        if (params.entrySet().size() > 0) {
            eventParams.put(EventParams.PROVIDER_EXTRA_DATA, params);
        }
        MotechEvent event = new MotechEvent(EventSubjects.CALL_INITIATED, eventParams);
        LOGGER.debug("Sending MotechEvent {}", event.toString());
        eventRelay.sendEventMessage(event);
    }

    private HttpUriRequest generateHttpRequest(Config config, Map<String, String> params) {
        LOGGER.debug("generateHttpRequest(config = {}, params = {})", config, params);

        String uri = config.getOutgoingCallUriTemplate();
        BasicHttpParams httpParams = new BasicHttpParams();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String placeholder = String.format("[%s]", entry.getKey());
            if (uri.contains(placeholder)) {
                uri = uri.replace(placeholder, entry.getValue());
            } else {
                httpParams.setParameter(entry.getKey(), entry.getValue());
            }
        }

        HttpUriRequest request;
        if (HttpMethod.GET == config.getOutgoingCallMethod()) {
            request = new HttpGet(uri);
        } else {
            request = new HttpPost(uri);
        }
        request.setParams(httpParams);

        LOGGER.debug("Generated {}", request.toString());

        return request;
    }

}
