package org.motechproject.commcare.events;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.service.CommcareAppStructureService;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.tasks.CommcareTasksNotifier;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private CommcareAppStructureService appStructureService;

    @Autowired
    private CommcareApplicationDataService commcareApplicationDataService;

    @Autowired
    private CommcareTasksNotifier commcareTasksNotifier;

    @MotechListener(subjects = CONFIG_CREATED)
    public synchronized void configCreated(MotechEvent event) {

        String configName = (String) event.getParameters().get(EventDataKeys.CONFIG_NAME);

        List<CommcareApplicationJson> applications = appStructureService.getAllApplications(configName);

        for (CommcareApplicationJson app : applications) {
            app.setConfigName(configName);
            commcareApplicationDataService.create(app);
        }

        commcareTasksNotifier.updateTasksInfo();
    }

    @MotechListener(subjects = CONFIG_UPDATED)
    public synchronized void configUpdated(MotechEvent event) {

        String configName = (String) event.getParameters().get(EventDataKeys.CONFIG_NAME);

        for (CommcareApplicationJson application : commcareApplicationDataService.bySourceConfiguration(configName)) {
            commcareApplicationDataService.delete(application);
        }

        for (CommcareApplicationJson app : appStructureService.getAllApplications(configName)) {
            app.setConfigName(configName);
            commcareApplicationDataService.create(app);
        }

        commcareTasksNotifier.updateTasksInfo();
    }

    @MotechListener(subjects = CONFIG_DELETED)
    public synchronized void configDeleted(MotechEvent event) {

        String configName = (String) event.getParameters().get(EventDataKeys.CONFIG_NAME);

        List<CommcareApplicationJson> applications = commcareApplicationDataService.bySourceConfiguration(configName);

        for (CommcareApplicationJson application : applications) {
            commcareApplicationDataService.delete(application);
        }

        commcareTasksNotifier.updateTasksInfo();
    }
}
