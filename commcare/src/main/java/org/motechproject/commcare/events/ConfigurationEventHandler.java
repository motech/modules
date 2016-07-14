package org.motechproject.commcare.events;

import org.motechproject.commcare.config.manager.ConfigurationManager;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.commcare.events.constants.EventSubjects.CONFIG_CREATED;
import static org.motechproject.commcare.events.constants.EventSubjects.CONFIG_DELETED;
import static org.motechproject.commcare.events.constants.EventSubjects.CONFIG_UPDATED;

/**
 * Listens to notifications about configurations and performs actions when such event is received. If configuration
 * is deleted, its applications current schemas are dropped. If configuration is created a call to CommcareHQ is made to
 * fetch the latest schemas version. Once we parse the response, tasks are notified to update triggers.
 */
@Component
public class ConfigurationEventHandler {

    @Autowired
    private ConfigurationManager configurationManager;

    /**
     * Responsible for handling {@code CONFIG_CREATED} event. This event is fired when user creates a new configuration.
     * Handling this event will result in adding new configuration and downloading applications related with it.
     *
     * @param event  the event to be handled
     */
    @MotechListener(subjects = CONFIG_CREATED)
    public synchronized void configCreated(MotechEvent event) {
        String configName = (String) event.getParameters().get(EventDataKeys.CONFIG_NAME);
        configurationManager.configCreated(configName);
    }

    /**
     * Responsible for handling {@code CONFIG_UPDATED} event. This event is fired when user updates an existing
     * configuration. Handling this event will result in deleting all stored applications related with the updated
     * configuration and downloading new ones from the CommCare server.
     *
     * @param event  the event to be handled
     */
    @MotechListener(subjects = CONFIG_UPDATED)
    public synchronized void configUpdated(MotechEvent event) {
        String configName = (String) event.getParameters().get(EventDataKeys.CONFIG_NAME);
        boolean isVerifyConfig = (boolean) event.getParameters().get(EventDataKeys.VERIFY_CONFIG);
        configurationManager.configUpdated(configName, isVerifyConfig);
    }

    /**
     * Responsible for handling {@code CONFIG_DELETED} event. This event is fired when user deletes an existing
     * configuration. Handling this event will result in removing the configuration itself and all related application
     * stored in the database.
     *
     * @param event  the event to be handled
     */
    @MotechListener(subjects = CONFIG_DELETED)
    public synchronized void configDeleted(MotechEvent event) {
        String configName = (String) event.getParameters().get(EventDataKeys.CONFIG_NAME);
        configurationManager.configDeleted(configName);
    }
}
