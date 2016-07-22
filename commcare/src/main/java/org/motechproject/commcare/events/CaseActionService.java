package org.motechproject.commcare.events;

import org.motechproject.commcare.domain.CaseTask;

import java.util.Map;

/**
 * Created by root on 19.07.16.
 */
public interface CaseActionService {
    CaseTask createCase(String configName, String caseType, String ownerId, String caseName, Map<String, Object> fieldValues);
    void updateCase(String configName, String caseId, String ownerId, Boolean closeCase, Map<String, Object> fieldValues);
}
