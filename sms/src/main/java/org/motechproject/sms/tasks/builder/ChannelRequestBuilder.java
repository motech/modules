package org.motechproject.sms.tasks.builder;


import java.io.InputStream;
import java.io.StringWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import org.apache.commons.io.IOUtils;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.json.ActionEventRequestDeserializer;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.reflect.TypeToken;


/**
 * The <code>ChannelRequestBuilder</code> class is responsible for building the
 * {@link org.motechproject.tasks.contract.ChannelRequest}.Channel request is created from json
 * and then the configuration options are set. If there are no configurations for sms then
 * the send_sms action event is removed from the channel request. Such request is later on
 * used to register the channel for the sms module.
 */
public class ChannelRequestBuilder {

    private static final String TASK_CHANNEL_LOCATION = "sms-task-channel.json";

    private static Map<Type, Object> typeAdapters = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelRequestBuilder.class);

    private BundleContext bundleContext;
    private MotechJsonReader motechJsonReader;
    private SortedSet<String> configOptions;

    static {
        typeAdapters.put(ActionEventRequest.class, new ActionEventRequestDeserializer());
    }

    public ChannelRequestBuilder(BundleContext bundleContext, SortedSet<String> configOptions) {

        this.bundleContext = bundleContext;
        this.configOptions = configOptions;
        this.motechJsonReader = new MotechJsonReader();
    }

    public ChannelRequest buildChannelRequest() {
        ChannelRequest channelRequest = getChannelRequestFromJson();
        if (channelRequest == null) {
            return null;
        }
        List<ActionEventRequest> actionTaskEvents = channelRequest.getActionTaskEvents();
        ActionEventRequest sendSmsActionEventRequest = null;
        for (ActionEventRequest actionEventRequest : actionTaskEvents) {
            if ("send_sms".equals(actionEventRequest.getSubject())) {
                if (!configOptions.isEmpty()) {
                    sendSmsActionEventRequest = actionEventRequest;
                } else {
                    actionTaskEvents.remove(actionEventRequest);
                }
                break;
            }
        }
        if (!configOptions.isEmpty() && sendSmsActionEventRequest != null) {
            SortedSet<ActionParameterRequest> actionParameters = sendSmsActionEventRequest.getActionParameters();
            ActionParameterRequest configActionParameterRequest = null;
            for (ActionParameterRequest parameterRequest : actionParameters) {
                if ("config".equals(parameterRequest.getKey())) {
                    configActionParameterRequest = parameterRequest;
                    break;
                }
            }
            if (configActionParameterRequest != null) {
                configActionParameterRequest.setOptions(configOptions);
            }
        }
        channelRequest.setModuleName(bundleContext.getBundle().getSymbolicName());
        channelRequest.setModuleVersion(bundleContext.getBundle().getVersion().toString());
        return channelRequest;
    }

    private ChannelRequest getChannelRequestFromJson() {
        Type type = new TypeToken<ChannelRequest>() {
        }.getType();
        StringWriter writer = new StringWriter();
        URL taskChannelURL = bundleContext.getBundle().getResource(TASK_CHANNEL_LOCATION);
        ChannelRequest channelRequest = null;
        try (InputStream stream = taskChannelURL.openStream()) {
            IOUtils.copy(stream, writer);
            channelRequest = (ChannelRequest) motechJsonReader.readFromString(writer.toString(), type, typeAdapters);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return  channelRequest;
    }
}







