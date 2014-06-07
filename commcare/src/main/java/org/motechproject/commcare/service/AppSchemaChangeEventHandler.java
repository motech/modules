package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.motechproject.commcare.events.constants.EventSubjects.SCHEMA_CHANGE_EVENT;

@Service
public class AppSchemaChangeEventHandler {

    @Autowired
    private CommcareAppStructureService appStructureService;

    @Autowired
    private CommcareApplicationDataService commcareApplicationDataService;

    @MotechListener(subjects = SCHEMA_CHANGE_EVENT)
    public void schemaChange(MotechEvent event) {
        List<CommcareApplicationJson> applications = appStructureService.getAllApplications();
        commcareApplicationDataService.deleteAll();

        for (CommcareApplicationJson app : applications) {
            commcareApplicationDataService.create(app);
        }
    }
}
