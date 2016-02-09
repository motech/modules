package org.motechproject.ipf.task;

import org.motechproject.ipf.util.Constants;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ActionParameterRequestBuilder;
import org.motechproject.tasks.contract.ChannelRequest;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class IpfChannelRequestBuilder {

    private static final String UNICODE = "UNICODE";

    private int counter;
    private BundleContext bundleContext;
    private List<String> templates;

    public IpfChannelRequestBuilder(BundleContext bundleContext, List<String> templates) {
        this.bundleContext = bundleContext;
        this.templates = templates;
    }

    public ChannelRequest build() {
        return new ChannelRequest(Constants.CHANNEL_DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, null, buildActions());
    }

    private List<ActionEventRequest> buildActions() {
        List<ActionEventRequest> actionEventRequests = new ArrayList<>();

        for (String template : templates) {

            counter = 0;

            SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
            ActionEventRequestBuilder builder = new ActionEventRequestBuilder();

            ActionParameterRequestBuilder actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(Constants.SAMPLE_DISPLAY_NAME_1)
                    .setKey(Constants.SAMPLE_KEY_1)
                    .setType(UNICODE)
                    .setRequired(true)
                    .setOrder(counter++);
            actionParameters.add(actionParameterBuilder.createActionParameterRequest());

            actionParameterBuilder = new ActionParameterRequestBuilder()
                    .setDisplayName(Constants.SAMPLE_DISPLAY_NAME_2)
                    .setKey(Constants.SAMPLE_KEY_2)
                    .setType(UNICODE)
                    .setRequired(true)
                    .setOrder(counter++);
            actionParameters.add(actionParameterBuilder.createActionParameterRequest());

            builder.setActionParameters(actionParameters)
                    .setDisplayName(template)
                    .setName(template)
                    .setSubject(template);

            actionEventRequests.add(builder.createActionEventRequest());
        }

        return actionEventRequests;
    }
}
