package org.motechproject.openmrs19.tasks.builder;

import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for building channel request for the OpenMRS module.
 */
public class ChannelRequestBuilder {

    private static final String DISPLAY_NAME = "openMRS";
    private static final String DESCRIPTION = "Channel originating from the OpenMRS module";

    private BundleContext bundleContext;
    private ActionBuilder actionsBuilder;

    public ChannelRequestBuilder(BundleContext bundleContext, OpenMRSConfigService configService) {
        this.bundleContext = bundleContext;
        this.actionsBuilder = new ActionBuilder(configService);
    }

    /**
     * Builds the channel request for the OpenMRS module.
     *
     * @return the built channel request
     */
    public ChannelRequest build() {
        String moduleSymbolicName = getModuleSymbolicName();
        String moduleVersion = bundleContext.getBundle().getVersion().toString();
        List<TriggerEventRequest> triggers = new ArrayList<>();
        List<ActionEventRequest> actions = actionsBuilder.buildActions();
        return new ChannelRequest(DISPLAY_NAME, moduleSymbolicName, moduleVersion, DESCRIPTION, triggers, actions);
    }

    /**
     * Returns the module symbolic name of the Channel.
     *
     * @return the bundle symbolic name
     */
    public String getModuleSymbolicName() {
        return bundleContext.getBundle().getSymbolicName();
    }
}
