package org.motechproject.openlmis.service.impl;

import org.motechproject.openlmis.service.TasksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.TasksService}
 */
@Service
public class TasksServiceImpl implements TasksService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksServiceImpl.class);

    @Override
    public void updateChannel() {
        LOGGER.debug("Updating OpenLMIS task channel...");
        
    }
}
