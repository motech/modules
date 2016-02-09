package org.motechproject.ipf.task;

import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.event.EventSubjects;
import org.motechproject.ipf.util.Constants;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ActionParameterRequestBuilder;
import org.motechproject.tasks.contract.ChannelRequest;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class IpfChannelRequestBuilder {

    private static final String UNICODE = "UNICODE";

    private int counter;
    private BundleContext bundleContext;
    private List<String> templates;
    private Collection<IPFRecipient> recipients;

    public IpfChannelRequestBuilder(BundleContext bundleContext, List<String> templates, Collection<IPFRecipient> recipients) {
        this.bundleContext = bundleContext;
        this.templates = templates;
        this.recipients = recipients;
    }

    public ChannelRequest build() {
        return new ChannelRequest(Constants.CHANNEL_DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, null, buildActions());
    }

    private List<ActionEventRequest> buildActions() {
        List<ActionEventRequest> actionEventRequests = new ArrayList<>();

        for (String template : templates) {
            for (IPFRecipient recipient : recipients) {
                counter = 0;

                SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
                ActionEventRequestBuilder builder = new ActionEventRequestBuilder();

                // TODO - remove
                ActionParameterRequestBuilder actionParameterBuilder = new ActionParameterRequestBuilder()
                        .setDisplayName(Constants.SAMPLE_DISPLAY_NAME_1)
                        .setKey(Constants.SAMPLE_KEY_1)
                        .setType(UNICODE)
                        .setRequired(true)
                        .setOrder(counter++);
                actionParameters.add(actionParameterBuilder.createActionParameterRequest());

                // TODO - remove
                actionParameterBuilder = new ActionParameterRequestBuilder()
                        .setDisplayName(Constants.SAMPLE_DISPLAY_NAME_2)
                        .setKey(Constants.SAMPLE_KEY_2)
                        .setType(UNICODE)
                        .setRequired(true)
                        .setOrder(counter++)
                        .setValue(recipient.getRecipientName());
                actionParameters.add(actionParameterBuilder.createActionParameterRequest());

                // hidden field with value - recipient name
                actionParameterBuilder = new ActionParameterRequestBuilder()
                        .setDisplayName(Constants.RECIPIENT_DISPLAY_NAME)
                        .setKey(Constants.RECIPIENT_NAME_PARAM)
                        .setType(UNICODE)
                        .setRequired(true)
                        .setHidden(true)
                        .setOrder(counter++)
                        .setValue(recipient.getRecipientName());
                actionParameters.add(actionParameterBuilder.createActionParameterRequest());

                builder.setActionParameters(actionParameters)
                        .setDisplayName(template + " " + recipient.getRecipientName())
                        .setName(template + "." + recipient.getRecipientName())
                        .setSubject(EventSubjects.TEMPLATE_ACTION + "." + template + "." + recipient.getRecipientName());

                actionEventRequests.add(builder.createActionEventRequest());
            }
        }

        return actionEventRequests;
    }
}
