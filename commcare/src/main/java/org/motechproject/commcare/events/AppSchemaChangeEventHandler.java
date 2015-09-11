package org.motechproject.commcare.events;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.service.CommcareAppStructureService;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.tasks.CommcareTasksNotifier;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mds.filter.Filter;
import org.motechproject.mds.filter.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private CommcareAppStructureService appStructureService;

    @Autowired
    private CommcareApplicationDataService commcareApplicationDataService;

    @Autowired
    private CommcareTasksNotifier commcareTasksNotifier;

    @Autowired
    private CommcareConfigService configService;

    /**
     * Responsible for handling {@code SCHEMA_CHANGE_EVENT}. This event is fired when the CommCare server sends a
     * notification about schema change to the MOTECH forwarding endpoint. All stored applications that originate from
     * the configuration passed in the event will be deleted and new applications will be downloaded from the CommCare
     * server.
     *
     * @param event  the schema change event to be handled
     */
    @MotechListener(subjects = SCHEMA_CHANGE_EVENT)
    @Transactional
    public synchronized void schemaChange(MotechEvent event) {

        Config config = configService.getByName((String) event.getParameters().get(EventDataKeys.CONFIG_NAME));

        Filters filters = new Filters(new Filter("configName", config.getName()));

        List<CommcareApplicationJson> serverApps = appStructureService.getAllApplications(config.getName());
        List<CommcareApplicationJson> storedApps = commcareApplicationDataService.filter(filters, null);

        if (!serverApps.equals(storedApps)) {

            for (CommcareApplicationJson app : storedApps) {
                commcareApplicationDataService.delete(app);
            }

            for (CommcareApplicationJson app : serverApps) {
                commcareApplicationDataService.create(app);
            }

            commcareTasksNotifier.updateTasksInfo();
        }
    }
}
