package org.motechproject.server.verboice;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.motechproject.callflow.domain.FlowSessionRecord;
import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.decisiontree.model.FlowSession;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.CallDirection;
import org.motechproject.ivr.domain.CallDisposition;
import org.motechproject.ivr.service.contract.CallRequest;
import org.motechproject.ivr.service.contract.IVRService;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Verboice specific implementation of the IVR Service interface
 */
@Component
public class VerboiceIVRService implements IVRService {
    private static Logger log = LoggerFactory.getLogger(VerboiceIVRService.class);
    private static final String CALLBACK_URL = "callback_url";
    private static final String CALLBACK_STATUS_URL = "status_callback_url";
    private static final String CALL_FLOW_ID = "call_flow_id";
    private static final String LANGUAGE = "language";

    private SettingsFacade settings;
    private HttpClient commonsHttpClient;
    private FlowSessionService flowSessionService;

    @Autowired
    public VerboiceIVRService(@Qualifier("verboiceAPISettings") SettingsFacade settings, HttpClient commonsHttpClient, FlowSessionService flowSessionService) {
        this.settings = settings;
        this.commonsHttpClient = commonsHttpClient;
        this.flowSessionService = flowSessionService;
    }

    @Override
    public void initiateCall(CallRequest callRequest) {
        initSession(callRequest);
        try {
            GetMethod getMethod = new GetMethod(outgoingCallUri(callRequest));
            getMethod.addRequestHeader("Authorization", "Basic " + basicAuthValue());
            int status = commonsHttpClient.executeMethod(getMethod);

            log.info(String.format("[%d]\n%s", status, getMethod.getResponseBodyAsString()));
        } catch (IOException e) {
            log.error("Exception when initiating call: ", e);
        }
    }

    private String basicAuthValue() {
        return new String(Base64.encodeBase64((settings.getProperty("username") + ":" + settings.getProperty("password")).getBytes()));
    }

    private FlowSession initSession(CallRequest callRequest) {
        FlowSessionRecord flowSession = (FlowSessionRecord) flowSessionService.findOrCreate(callRequest.getCallId(), callRequest.getPhone());
        final CallDetailRecord callDetailRecord = flowSession.getCallDetailRecord();
        callDetailRecord.setCallDirection(CallDirection.Outbound);
        callDetailRecord.setDisposition(CallDisposition.UNKNOWN);
        for (String key : callRequest.getPayload().keySet()) {
            if (!CALLBACK_URL.equals(key) && !CALLBACK_STATUS_URL.equals(key)) {
                flowSession.set(key, callRequest.getPayload().get(key));
            }
        }
        flowSessionService.updateSession(flowSession);

        return flowSession;
    }

    private String outgoingCallUri(CallRequest callRequest) throws UnsupportedEncodingException {
        StringBuilder queryParameters = new StringBuilder("");

        Map<String, String> requestPayload = callRequest.getPayload();

        if (requestPayload != null) {
            appendParam(requestPayload.get(CALLBACK_URL), CALLBACK_URL, queryParameters);
            appendParam(requestPayload.get(CALLBACK_STATUS_URL), CALLBACK_STATUS_URL, queryParameters);
            appendParam(requestPayload.get(CALL_FLOW_ID), CALL_FLOW_ID, queryParameters);
            appendParam(requestPayload.get(LANGUAGE), "vars[" + LANGUAGE + "]", queryParameters);
        }

        String url = format(
                "http://%s:%s/api/call?channel=%s&address=%s%s&callback_params[motech_call_id]=%s",
                settings.getProperty("host"),
                settings.getProperty("port"),
                isBlank(callRequest.getCallBackUrl()) ? settings.getProperty("channel") : callRequest.getCallBackUrl(),
                        callRequest.getPhone(), queryParameters.toString(), callRequest.getCallId()
                );

        return URLEncoder.encode(url, "UTF-8");
    }

    private void appendParam(String value, String key, StringBuilder queryParameters) {
        if (value == null) {
            return;
        }

        queryParameters.append("&" + key + "=" + value);
    }
}
