package org.motechproject.commcare.tasks;

import org.motechproject.commcare.domain.CaseTask;

import java.util.Map;

/**
 * This service is responsible for handling Commcare Case actions in tasks.
 */
public interface CaseActionService {

    /**
     * Creates a Commcare Case, by sending Case XML to the Submission API on the Commcare server.
     *
     * @param configName  the name of the configuration used for connecting to CommcareHQ
     * @param caseType  the case type of case on CommCareHQ
     * @param ownerId   the ID of the owner
     * @param caseName  the case name of case on CommCareHQ
     * @param fieldValues   the params that will be passed to CommcareHQ with case information
     * @return  an object representing the case information and case actions submitted as case xml
     */
    CaseTask createCase(String configName, String caseType, String ownerId, String caseName, Map<String, Object> fieldValues);

    /**
     * Updates a Commcare Case, by sending Case XML to the Submission API on the Commcare server.
     * The case ID is required to identify the case that is supposed to be updated
     *
     * @param configName  the name of the configuration used for connecting to CommcareHQ
     * @param caseId  the id of the case on CommCareHQ
     * @param ownerId   the ID of the owner
     * @param closeCase  true to optionally close the case on CommCareHQ
     * @param fieldValues   the params that will be passed to CommcareHQ with case information
     */
    void updateCase(String configName, String caseId, String ownerId, Boolean closeCase, Map<String, Object> fieldValues);
}
