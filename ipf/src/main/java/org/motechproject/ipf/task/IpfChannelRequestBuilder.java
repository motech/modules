package org.motechproject.ipf.task;

import org.apache.commons.lang.StringUtils;
import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.domain.IPFTemplate;
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

/**
 * Builds a channel request from uploaded templates and recipients. It generates task action for each pair of
 * template recipient.
 */
public class IpfChannelRequestBuilder {

    private static final String UNICODE = "UNICODE";

    private int counter;
    private BundleContext bundleContext;
    private List<IPFTemplate> templates;
    private Collection<IPFRecipient> recipients;

    public IpfChannelRequestBuilder(BundleContext bundleContext, List<IPFTemplate> templates, Collection<IPFRecipient> recipients) {
        this.bundleContext = bundleContext;
        this.templates = templates;
        this.recipients = recipients;
    }


    /**
     * Creates channel requests requests for each pair template recipient. Template name and recipient name fields are
     * automatically added(as hidden fields) to the task action request.
     *
     * @return the new channel request
     */
    public ChannelRequest build() {
        return new ChannelRequest(Constants.CHANNEL_DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, null, buildActions());
    }

    private List<ActionEventRequest> buildActions() {
        List<ActionEventRequest> actionEventRequests = new ArrayList<>();

        for (IPFTemplate template : templates) {
            for (IPFRecipient recipient : recipients) {
                counter = 0;

                SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
                ActionEventRequestBuilder builder = new ActionEventRequestBuilder();

                for (String parameter : template.getProperties().keySet()) {
                    actionParameters.add(getParamBuilder(null, parameter, template.getProperties().get(parameter), true, false).createActionParameterRequest());
                }

                // hidden fields with value - recipient name and template name
                actionParameters.add(getParamBuilder(recipient.getRecipientName(), Constants.RECIPIENT_NAME_PARAM, Constants.RECIPIENT_DISPLAY_NAME, true, true).createActionParameterRequest());
                actionParameters.add(getParamBuilder(template.getTemplateName(), Constants.TEMPLATE_NAME_PARAM, Constants.TEMPLATE_DISPLAY_NAME, true, true).createActionParameterRequest());

                builder.setActionParameters(actionParameters)
                        .setDisplayName(template.getTemplateName() + " " + recipient.getRecipientName())
                        .setName(template.getTemplateName() + "." + recipient.getRecipientName())
                        .setSubject(EventSubjects.TEMPLATE_ACTION + "." + template.getTemplateName() + "." + recipient.getRecipientName());

                actionEventRequests.add(builder.createActionEventRequest());
            }
        }

        return actionEventRequests;
    }

    private ActionParameterRequestBuilder getParamBuilder(String value, String param, String displayName, boolean required, boolean hidden) {
        ActionParameterRequestBuilder actionParameterRequestBuilder = new ActionParameterRequestBuilder()
                                                                        .setDisplayName(displayName)
                                                                        .setKey(param)
                                                                        .setType(UNICODE)
                                                                        .setRequired(required)
                                                                        .setHidden(hidden)
                                                                        .setOrder(counter++);
        if (StringUtils.isNotEmpty(value)) {
            actionParameterRequestBuilder.setValue(value);
        }

        return actionParameterRequestBuilder;
    }
}
