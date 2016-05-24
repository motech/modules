package org.motechproject.openmrs.tasks;

import org.motechproject.openmrs.tasks.builder.ChannelRequestBuilder;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>TasksChannelServiceInstance</code> class, holds Tasks-related beans. The reason this class
 * must exist is because no Spring beans should be initialised if the Tasks module is not present. Therefore,
 * we should first make sure that the Tasks module is present and only then we use Tasks classes, to
 * avoid <code>NoClassDefFoundError</code>.
 */
public class TasksChannelServiceInstance {

    private static final Logger LOGGER = LoggerFactory.getLogger(TasksChannelServiceInstance.class);

    private ChannelService channelService;
    private ChannelRequestBuilder channelRequestBuilder;

    public TasksChannelServiceInstance(Object service, ChannelRequestBuilder builder) {
        if (service instanceof ChannelService) {
            this.channelService = (ChannelService) service;
        }
        this.channelRequestBuilder = builder;
    }

    /**
     * Registers or updates the task channel if it contains any task triggers, if it doesn't the channel
     * will be unregistered.
     */
    public void updateTaskChannel() {
        if (channelService != null) {
            ChannelRequest channelRequest = channelRequestBuilder.build();
            if (!channelRequest.getActionTaskEvents().isEmpty()) {
                LOGGER.trace("Registering channel with the following request: {}", channelRequest);
                channelService.registerChannel(channelRequest);
            } else {
                unregisterTaskChannel();
            }
        }
    }

    /**
     * Unregisters the channel originating from the OpenMRS module.
     */
    public void unregisterTaskChannel() {
        LOGGER.trace("Unregistering OpenMRS tasks channel");
        channelService.unregisterChannel(channelRequestBuilder.getModuleSymbolicName());
    }
}
