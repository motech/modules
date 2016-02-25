package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.DisplayNames;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;
import org.osgi.framework.BundleContext;

import java.util.List;

/**
 * Builds a {@link ChannelRequest} for the module
 */
public class ChannelRequestBuilder {

    private BundleContext bundleContext;
    private List<FormDefinition> formDefinitions;


    /**
     * Constructor.
     * @param bundleContext {@link BundleContext}
     * @param formDefinitions List of {@link FormDefinition}
     */
    public ChannelRequestBuilder(BundleContext bundleContext, List<FormDefinition> formDefinitions) {
        this.bundleContext = bundleContext;
        this.formDefinitions = formDefinitions;
    }

    public ChannelRequest build() {
        FormTriggerBuilder triggerBuilder = new FormTriggerBuilder(formDefinitions);
        List<TriggerEventRequest> triggers = triggerBuilder.buildTriggers();
        RepeatGroupTriggerBuilder repeatGroupTriggerBuilder = new RepeatGroupTriggerBuilder(formDefinitions);
        triggers.addAll(repeatGroupTriggerBuilder.buildTriggers());
        ActionBuilder actionBuilder = new ActionBuilder(formDefinitions);
        List<ActionEventRequest> actions = actionBuilder.build();
        return new ChannelRequest(DisplayNames.CHANNEL_DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, triggers, actions);
    }
}
