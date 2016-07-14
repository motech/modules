package org.motechproject.commcare.events;

import org.motechproject.commcare.config.manager.ConfigurationManager;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.commcare.events.constants.EventSubjects.SCHEMA_CHANGE_EVENT;

/**
 * The <code>AppSchemaChangeEventHandler</code> class listens to notifications about Schema changes
 * and performs actions when such event is received. The current schema is dropped and a call to
 * CommcareHQ is made to fetch the latest schema version. Once we parse the response, tasks are notified
 * to update triggers.
 */
@Component
public class AppSchemaChangeEventHandler {

    @Autowired
    private ConfigurationManager configurationManager;

    /**
     * Responsible for handling {@code SCHEMA_CHANGE_EVENT}. This event is fired when the CommCare server sends a
     * notification about schema change to the MOTECH forwarding endpoint. All stored applications that originate from
     * the configuration passed in the event will be deleted and new applications will be downloaded from the CommCare
     * server.
     *
     * @param event  the schema change event to be handled
     */
    @MotechListener(subjects = SCHEMA_CHANGE_EVENT)
    public synchronized void schemaChange(MotechEvent event) {
        String configName = (String) event.getParameters().get(EventDataKeys.CONFIG_NAME);
        configurationManager.configUpdated(configName, true);
    }
}
