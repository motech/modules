package org.motechproject.commcare.events;

import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.domain.CloseTask;
import org.motechproject.commcare.domain.CreateTask;
import org.motechproject.commcare.domain.UpdateTask;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * This class serves as the event handler for the task actions, exposed by the Commcare module.
 * Respective methods extract the necessary data from the {@link MotechEvent} instance and
 * pass them to the {@link CommcareCaseService} that handles all the operations on Commcare cases.
 */
@Component
public class CaseEventHandler {

    @Autowired
    private CommcareCaseService caseService;

    @MotechListener(subjects = EventSubjects.CREATE_CASE + ".*")
    public void createCase(MotechEvent event) {
        String configName = EventSubjects.getConfigName(event.getSubject());
        Map<String, Object> parameters = event.getParameters();

        CaseTask caseTask = new CaseTask();
        // We generate a random UUID for case
        caseTask.setCaseId(UUID.randomUUID().toString());

        // <create> tag
        CreateTask createTask = new CreateTask();
        createTask.setCaseType((String) parameters.get(EventDataKeys.CASE_TYPE));
        createTask.setCaseName((String) parameters.get(EventDataKeys.CASE_NAME));
        createTask.setOwnerId((String) parameters.get(EventDataKeys.OWNER_ID));

        // <update> tag
        UpdateTask updateTask = new UpdateTask();
        updateTask.setFieldValues((Map<String, String>) parameters.get(EventDataKeys.FIELD_VALUES));

        caseTask.setCreateTask(createTask);
        caseTask.setUpdateTask(updateTask);

        caseService.uploadCase(caseTask, configName);
    }

    @MotechListener(subjects = EventSubjects.UPDATE_CASE + ".*")
    public void updateCase(MotechEvent event) {
        String configName = EventSubjects.getConfigName(event.getSubject());
        Map<String, Object> parameters = event.getParameters();

        CaseTask caseTask = new CaseTask();

        caseTask.setCaseId((String) parameters.get(EventDataKeys.CASE_ID));

        // <update> tag
        UpdateTask updateTask = new UpdateTask();
        updateTask.setOwnerId((String) parameters.get(EventDataKeys.OWNER_ID));
        updateTask.setFieldValues((Map<String, String>) parameters.get(EventDataKeys.FIELD_VALUES));

        // optional <close> tag
        if (parameters.get(EventDataKeys.CLOSE_CASE) != null && (Boolean) parameters.get(EventDataKeys.CLOSE_CASE)) {
            CloseTask closeTask = new CloseTask(true);
            caseTask.setCloseTask(closeTask);
        }

        caseTask.setUpdateTask(updateTask);

        caseService.uploadCase(caseTask, configName);
    }
}
