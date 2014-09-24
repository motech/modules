package org.motechproject.commcare.tasks;

import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.service.ChannelService;

/**
 * The <code>TasksChannelServiceInstance</code> class, holds Tasks-related beans. The reason this class
 * must exist is because no Spring beans should be initialised if the Tasks module is not present. Therefore,
 * we should first make sure that the Tasks module is present and only then we use Tasks classes, to
 * avoid <code>NoClassDefFoundError</code>.
 */
public class TasksChannelServiceInstance {

    private ChannelService channelService;
    private ChannelRequestBuilder channelRequestBuilder;

    public TasksChannelServiceInstance(Object service, ChannelRequestBuilder builder) {
        if (service instanceof ChannelService) {
            this.channelService = (ChannelService) service;
        }
        this.channelRequestBuilder = builder;
    }

    public void updateTaskChannel() {
        ChannelRequest channelRequest = channelRequestBuilder.buildChannelRequest();
        if (channelService != null && !channelRequest.getTriggerTaskEvents().isEmpty()) {
            channelService.registerChannel(channelRequest);
        }
    }
}
