package org.motechproject.ivr.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.CallDirection;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.HttpMethod;
import org.motechproject.ivr.event.EventParams;
import org.motechproject.ivr.event.EventSubjects;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.service.CallInitiationException;
import org.motechproject.ivr.service.ConfigService;
import org.motechproject.ivr.service.OutboundCallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Generates & sends an HTTP request to an IVR provider to trigger an outbound call
 */
@Service("outboundCallService")
public class OutboundCallServiceImpl implements OutboundCallService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundCallServiceImpl.class);
    private ConfigService configService;
    private CallDetailRecordDataService callDetailRecordDataService;
    private EventRelay eventRelay;
    private StatusMessageService statusMessageService;
    private static final String FILE_PROTOCOL = "file";
    private static final String HTTP_PROTOCOL = "http";
    private static final String MODULE_NAME = "ivr";
    public static final List<Integer> ACCEPTABLE_IVR_RESPONSE_STATUSES = Arrays.asList(HttpStatus.SC_OK,
            HttpStatus.SC_ACCEPTED, HttpStatus.SC_CREATED);

    @Autowired
    public OutboundCallServiceImpl(@Qualifier("configService") ConfigService configService,
                                   StatusMessageService statusMessageService,
                                   CallDetailRecordDataService callDetailRecordDataService, EventRelay eventRelay) {
        this.configService = configService;
        this.statusMessageService = statusMessageService;
        this.callDetailRecordDataService = callDetailRecordDataService;
        this.eventRelay = eventRelay;
    }

    private void addCallDetailRecord(String callStatus, Config config, Map<String, String> params,
                                     String motechCallId) {
        LOGGER.debug(String.format("addCallDetailRecord(callStatus = %s, config = %s, params = %s, motechCallId = %s)",
                callStatus, config.getName(), params.toString(), motechCallId));

        CallDetailRecord callDetailRecord = new CallDetailRecord(config.getName(), null, null, null, CallDirection.OUTBOUND,
                callStatus, null, motechCallId, null, null, null, null);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (config.shouldIgnoreField(entry.getKey())) {
                LOGGER.debug("Ignoring provider field '{}' with value '{}'", entry.getKey(), entry.getValue());
            } else {
                //Default status like CALL_INITIATED will be overwritten by value provided in params
                callDetailRecord.setField(config.mapStatusField(entry.getKey()), entry.getValue(), config.getCallStatusMapping());
            }
        }

        callDetailRecordDataService.create(callDetailRecord);
    }

    @Override
    public void initiateCall(String configName, Map<String, String> parameters) {
        LOGGER.debug("initiateCall(configName = {}, params = {})", configName, parameters);

        Map<String, String> params = new HashMap<>(parameters);

        if (!configService.hasConfig(configName)) {
            String msg = String.format("Invalid config: '%s'", configName);
            statusMessageService.warn(msg, MODULE_NAME);
            throw new CallInitiationException(msg);
        }

        Config config = configService.getConfig(configName);

        String motechCallId = UUID.randomUUID().toString();
        Map<String, String> completeParams = new HashMap<>(params);
        completeParams.put("motechCallId", motechCallId);

        try {
            String protocol = new URL(config.getOutgoingCallUriTemplate()).getProtocol();

            switch (protocol) {
                case FILE_PROTOCOL: {
                    generateCallFile(config, params, completeParams, motechCallId);
                    break;
                }

                case HTTP_PROTOCOL: {
                    generateCallHttp(config, params, completeParams, motechCallId);
                    break;
                }
            }
        } catch (CallInitiationException e) {
            throw e;
        } catch (Exception e) {
            String message = String.format("Could not initiate call, unexpected exception: %s", e.toString());
            statusMessageService.warn(message, MODULE_NAME);
            params.put("ErrorMessage", message);
            addCallDetailRecord(CallDetailRecord.CALL_FAILED, config, params, motechCallId);
            throw new CallInitiationException(message, e);
        }
    }

    private void generateCallFile(Config config, Map<String, String> params, Map<String, String> completeParams, String motechCallId) throws URISyntaxException, IOException {
        String myUri = mergeUriAndRemoveParams(config.getOutgoingCallUriTemplate(), completeParams);

        Path path = Paths.get(new URI(myUri));
        Path tempDirectory = Files.createTempDirectory(path.getParent(), "motech");
        Path tempPath = Files.createTempFile(tempDirectory, "motech", ".tmp");

        BufferedWriter bufferedWriter = Files.newBufferedWriter(tempPath);
        for (Map.Entry<String, String> entry : completeParams.entrySet()) {
            bufferedWriter.write(String.format("%s: %s", entry.getKey(), entry.getValue()));
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

        Files.move(tempPath, path, StandardCopyOption.ATOMIC_MOVE);
        tempDirectory.toFile().delete();

        addCallDetailRecord(CallDetailRecord.CALL_INITIATED, config, params, motechCallId);
    }

    private void generateCallHttp(Config config, Map<String, String> params, Map<String, String > completeParams, String motechCallId) throws IOException {
        HttpUriRequest request = generateHttpRequest(config, completeParams);
        HttpResponse response = new DefaultHttpClient().execute(request);

        StatusLine statusLine = response.getStatusLine();

        //todo: it's possible that some IVR providers return an HTTP 200 and an error code in the response body.
        //todo: If we encounter such a provider, we'll have to beef up the response processing here
        if (!ACCEPTABLE_IVR_RESPONSE_STATUSES.contains(statusLine.getStatusCode())) {
            String message = String.format("Could not initiate call: %s", statusLine.toString());
            statusMessageService.warn(message, MODULE_NAME);
            params.put("ErrorMessage", message);
            addCallDetailRecord(CallDetailRecord.CALL_FAILED, config, params, motechCallId);
            throw new CallInitiationException(message);
        }

        Map<String, Object> completeJsonMap = new HashMap<>();
        if (config.isJsonResponse()) {
            try (InputStream json = response.getEntity().getContent()) {
                String jsonString = IOUtils.toString(json);

                // Grab key-value pairs from the keys the implementer defines during configuration.
                Map<String, String> extraParamsFromJson = getAdditionalParamsFromJson(jsonString, config.getJsonExtraParamsList());

                // Parse the complete JSON map, to be sent with the Motech event.
                completeJsonMap = getCompleteJsonMap(jsonString);
                params.putAll(extraParamsFromJson);
            } catch (IOException e) {
                String message = String.format("Could not parse for JSON for entity: %s", response.getEntity());
                LOGGER.info(message);
                statusMessageService.warn(message, MODULE_NAME);
            }

        }

        // Add a CDR to the database
        addCallDetailRecord(CallDetailRecord.CALL_INITIATED, config, params, motechCallId);

        // Generate a MOTECH event
        MotechEvent event = generateMotechEvent(config.getName(), params, completeJsonMap);
        LOGGER.debug("Sending MotechEvent {}", event.toString());
        eventRelay.sendEventMessage(event);
    }

    private String mergeUriAndRemoveParams(String uriTemplate, Map<String, String> params) {
        String mergedURI = uriTemplate;

        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String placeholder = String.format("[%s]", entry.getKey());
            if (mergedURI.contains(placeholder)) {
                mergedURI = mergedURI.replace(placeholder, entry.getValue());
                it.remove();
            }
        }

        return mergedURI;
    }

    private MotechEvent generateMotechEvent(String configName, Map<String, String> params, Map<String, Object> completeJsonMap) {
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put(EventParams.CONFIG, configName);
        eventParams.put(EventParams.MOTECH_TIMESTAMP, CallDetailRecord.getCurrentTimestamp());
        if (params.containsKey("to")) {
            eventParams.put(EventParams.TO, params.get("to"));
        }
        if (params.entrySet().size() > 0) {
            eventParams.put(EventParams.PROVIDER_EXTRA_DATA, params);
        }
        if (!completeJsonMap.isEmpty()) {
            eventParams.put(EventParams.PROVIDER_JSON_RESPONSE, completeJsonMap);
        }

        return new MotechEvent(EventSubjects.CALL_INITIATED, eventParams);
    }

    private HttpUriRequest generateHttpRequest(Config config, Map<String, String> params) {
        LOGGER.debug("generateHttpRequest(config = {}, params = {})", config, params);

        String uri = mergeUriAndRemoveParams(config.getOutgoingCallUriTemplate(), params);

        HttpUriRequest request;
        URIBuilder builder;
        try {
            builder = new URIBuilder(uri);

            if (HttpMethod.GET == config.getOutgoingCallMethod()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.setParameter(entry.getKey(), entry.getValue());
                }
                request = new HttpGet(builder.build());
            } else {
                HttpPost post = new HttpPost(uri);
                if (config.isJsonRequest()) {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    JsonObject jsonObject = new JsonObject();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        JsonElement obj;
                        try {
                            obj = new JsonParser().parse(entry.getValue());
                        } catch (JsonSyntaxException e) {
                            obj = new JsonPrimitive(entry.getValue());
                        }
                        jsonObject.add(entry.getKey(), obj);
                    }
                    String json = gson.toJson(jsonObject);
                    post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
                } else {
                    ArrayList<NameValuePair> postParameters = new ArrayList<>();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        builder.setParameter(entry.getKey(), entry.getValue());
                        postParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                    post.setEntity(new UrlEncodedFormEntity(postParameters));
                }
                request = post;
            }
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            throw new IllegalStateException("Unexpected error creating a URI", e);
        }

        if (config.isAuthRequired()) {
            request.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(config.getUsername(), config.getPassword()),
                    "UTF-8",
                    false));
        }

        LOGGER.debug("Generated {}", request.toString());

        return request;
    }

    /**
     * Extracts key-value pairs as strings from the provider's JSON response, with the keys defined by the implementer
     * during configuration.
     *
     * @param jsonString
     *   The provider's JSON response.
     * @param jsonExtraParamsList
     *   The list of JSON keys for which to extract a value.
     * @return
     */
    private Map<String, String> getAdditionalParamsFromJson(String jsonString, List<String> jsonExtraParamsList) {
        Map<String, String> data = new HashMap<String, String>();

        try {
            JsonNode rootNode = new ObjectMapper().readValue(jsonString, JsonNode.class);
            for (String key : jsonExtraParamsList) {
                JsonNode valueNode = rootNode.findValue(key);
                if (valueNode != null) {
                    data.put(key, valueNode.asText());
                }
            }
        } catch (IOException e) {
            String message = String.format("Could not parse for JSON: %s", jsonString);
            LOGGER.warn(message);
            statusMessageService.warn(message, MODULE_NAME);
        }

        return data;
    }

    private Map<String, Object> getCompleteJsonMap(String jsonString) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            data = new ObjectMapper().readValue(jsonString, Map.class);
        } catch (IOException e) {
            String message = String.format("Could not parse for JSON: %s", jsonString);
            LOGGER.warn(message);
            statusMessageService.warn(message, MODULE_NAME);
        }
        return data;
    }

}
