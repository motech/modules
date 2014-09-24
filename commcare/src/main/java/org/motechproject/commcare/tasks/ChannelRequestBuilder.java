package org.motechproject.commcare.tasks;

import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ChannelRequestBuilder</code> class is responsible for building the
 * {@link org.motechproject.tasks.contract.ChannelRequest}. Such request is later on
 * used to register the channel for the Commcare module. To build the necessary triggers,
 * we use classes implementing {@link org.motechproject.commcare.tasks.TriggerBuilder}.
 */
public class ChannelRequestBuilder {
    private static final String DISPLAY_NAME = "commcare";

    private BundleContext bundleContext;
    private CommcareApplicationDataService commcareApplicationDataService;

    public ChannelRequestBuilder(CommcareApplicationDataService commcareApplicationDataService, BundleContext bundleContext) {
        this.commcareApplicationDataService = commcareApplicationDataService;
        this.bundleContext = bundleContext;
    }

    public ChannelRequest buildChannelRequest() {
        FormTriggerBuilder formTriggerBuilder = new FormTriggerBuilder(commcareApplicationDataService);
        CaseTriggerBuilder caseTriggerBuilder = new CaseTriggerBuilder(commcareApplicationDataService);
        CommonTriggerBuilder commonTriggerBuilder = new CommonTriggerBuilder();

        List<TriggerEventRequest> triggers = formTriggerBuilder.buildTriggers();
        triggers.addAll(caseTriggerBuilder.buildTriggers());
        triggers.addAll(commonTriggerBuilder.buildTriggers());

        return new ChannelRequest(DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, triggers, new ArrayList<ActionEventRequest>());
    }
}
