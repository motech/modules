package org.motechproject.openlmis.service;

/**
 * Processes Task channel updates for this module
 * @see org.motechproject.tasks.service.ChannelService
 * @see org.motechproject.openlmis.tasks.ChannelRequestBuilder
 * */
public interface TasksService {

    /**
     * Builds a channel request from the information saved in MDS and then updates the channel for this
     * module.
     */
    void updateChannel();
}
