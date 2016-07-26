package org.motechproject.commcare.events;

import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.tasks.CaseActionService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This class serves as the event handler for the task actions, exposed by the Commcare module.
 * Respective methods extract the necessary data from the {@link MotechEvent} instance and
 * forward them to the {@link CaseActionService} service that passes the data to the
 * {@link org.motechproject.commcare.service.CommcareCaseService} that handles all the operations on Commcare cases.
 */
@Component
public class CaseEventHandler {

    private CaseActionService caseActionService;

    @MotechListener(subjects = EventSubjects.CREATE_CASE + ".*")
    public void createCase(MotechEvent event) {
        String configName = EventSubjects.getConfigName(event.getSubject());
        Map<String, Object> parameters = event.getParameters();

        String caseType = (String) parameters.get(EventDataKeys.CASE_TYPE);
        String caseName = (String) parameters.get(EventDataKeys.CASE_NAME);
        String ownerId = (String) parameters.get(EventDataKeys.OWNER_ID);
        Map<String, Object> fieldValues = (Map<String, Object>) parameters.get(EventDataKeys.FIELD_VALUES);

        caseActionService.createCase(configName, caseType, ownerId, caseName, fieldValues);
    }

    @MotechListener(subjects = EventSubjects.UPDATE_CASE + ".*")
    public void updateCase(MotechEvent event) {
        String configName = EventSubjects.getConfigName(event.getSubject());
        Map<String, Object> parameters = event.getParameters();

        String caseId = (String) parameters.get(EventDataKeys.CASE_ID);
        String ownerId = (String) parameters.get(EventDataKeys.OWNER_ID);
        Map<String, Object> fieldValues = (Map<String, Object>) parameters.get(EventDataKeys.FIELD_VALUES);
        Boolean closeCase = (Boolean) parameters.get(EventDataKeys.CLOSE_CASE);

        caseActionService.updateCase(configName, caseId, ownerId, closeCase, fieldValues);
    }

    @Autowired
    public CaseEventHandler(CaseActionService caseActionService) {
        this.caseActionService = caseActionService;
    }
}
