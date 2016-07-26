package org.motechproject.commcare.tasks;

import org.motechproject.commcare.domain.CaseTask;

import java.util.Map;

/**
 * This service is responsible for handling "Create Case" and "Update Case" actions in tasks.
 */
public interface CaseActionService {
    CaseTask createCase(String configName, String caseType, String ownerId, String caseName, Map<String, Object> fieldValues);
    void updateCase(String configName, String caseId, String ownerId, Boolean closeCase, Map<String, Object> fieldValues);
}
