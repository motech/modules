package org.motechproject.dhis2.service;

import org.motechproject.dhis2.tasks.builder.ChannelRequestBuilder;

/**
 * Processes Task channel updates for this module
 * @see org.motechproject.tasks.service.ChannelService
 * @see ChannelRequestBuilder
 * */
public interface TasksService {

    /**
     * Builds a channel request from the information saved in MDS and then updates the channel for this
     * module.
     */
    void updateChannel();
}
