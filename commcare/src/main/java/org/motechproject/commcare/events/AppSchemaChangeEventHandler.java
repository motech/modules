package org.motechproject.commcare.events;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.service.CommcareAppStructureService;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.tasks.CommcareTasksNotifier;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @MotechListener(subjects = SCHEMA_CHANGE_EVENT)
    public void schemaChange(MotechEvent event) {
        List<CommcareApplicationJson> applications = appStructureService.getAllApplications();
        commcareApplicationDataService.deleteAll();

        for (CommcareApplicationJson app : applications) {
            commcareApplicationDataService.create(app);
        }

        commcareTasksNotifier.updateTaskChannel();
    }
}
