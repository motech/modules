package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.service.TasksService;
import org.motechproject.dhis2.tasks.builder.ChannelRequestBuilder;
import org.motechproject.tasks.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implementation of {@link org.motechproject.dhis2.service.TasksService}
 */
@Service
public class TasksServiceImpl implements TasksService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksServiceImpl.class);

    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelRequestBuilder channelRequestBuilder;

    @Override
    @Transactional
    public void updateChannel() {
        LOGGER.debug("Updating DHIS2 task channel...");
        channelService.registerChannel(channelRequestBuilder.build());
    }
}
