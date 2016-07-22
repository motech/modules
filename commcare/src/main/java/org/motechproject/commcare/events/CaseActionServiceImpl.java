package org.motechproject.commcare.events;

import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.domain.CloseTask;
import org.motechproject.commcare.domain.CreateTask;
import org.motechproject.commcare.domain.UpdateTask;
import org.motechproject.commcare.service.CommcareCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * Created by root on 19.07.16.
 */
@Service("caseActionService")
public class CaseActionServiceImpl implements CaseActionService {

    @Autowired
    private CommcareCaseService caseService;

    @Override
    public CaseTask createCase(String configName, String caseType, String ownerId, String caseName, Map<String, Object> fieldValues) {
        CaseTask caseTask = new CaseTask();
        // We generate a random UUID for case
        caseTask.setCaseId(UUID.randomUUID().toString());

        // <create> tag
        CreateTask createTask = new CreateTask();
        createTask.setCaseType(caseType);
        createTask.setCaseName(caseName);
        createTask.setOwnerId(ownerId);

        // <update> tag
        UpdateTask updateTask = new UpdateTask();
        updateTask.setFieldValues(fieldValues);

        caseTask.setCreateTask(createTask);
        caseTask.setUpdateTask(updateTask);

        caseService.uploadCase(caseTask, configName);

        return caseTask;
    }

    @Override
    public void updateCase(String configName, String caseId, String ownerId, Boolean closeCase, Map<String, Object> fieldValues) {
        CaseTask caseTask = new CaseTask();

        caseTask.setCaseId(caseId);

        // <update> tag
        UpdateTask updateTask = new UpdateTask();
        updateTask.setOwnerId(ownerId);
        updateTask.setFieldValues(fieldValues);

        // optional <close> tag
        if (closeCase != null && closeCase) {
            CloseTask closeTask = new CloseTask(true);
            caseTask.setCloseTask(closeTask);
        }

        caseTask.setUpdateTask(updateTask);

        caseService.uploadCase(caseTask, configName);
    }
}
