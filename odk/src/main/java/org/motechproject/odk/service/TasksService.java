package org.motechproject.odk.service;


/**
 * A Service for updating the {@link org.motechproject.tasks.domain.Channel} associated with this module
 */
public interface TasksService {

    /**
     * Generates a new {@link org.motechproject.tasks.domain.Channel} and calls {@link org.motechproject.tasks.service.ChannelService}
     * to update.
     */
    void updateTasksChannel();
}
