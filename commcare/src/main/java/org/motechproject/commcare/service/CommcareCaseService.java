package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.domain.CasesInfo;
import org.motechproject.commcare.response.OpenRosaResponse;

import java.util.List;

/**
 *  This service provides two main features: Interacting with CommCareHQ's programmatic case APIs and uploading case XML
 *  wrapped in a form instance to CommCareHQ.
 */
public interface CommcareCaseService {

    /**
     * Query CommCareHQ for a case by its case id.
     *
     * @param caseId  the id of the case on CommCareHQ
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the CaseInfo object representing the state of the case or null if that case does not exist.
     */
    CaseInfo getCaseByCaseId(String caseId, String configName);

    /**
     * Same as {@link #getCaseByCaseId(String, String) getCaseByCaseId} but uses default Commcare configuration.
     */
    CaseInfo getCaseByCaseId(String caseId);

    /**
     * Query CommCareHQ for all cases of a given case type and page.
     *
     * @param type  the type of case on CommCareHQ
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the list of CaseInfo objects representing cases of the given type and page found on the given CommcareHQ
     *          configuration
     */
    List<CaseInfo> getCasesByType(String type, Integer pageSize, Integer pageNumber, String configName);

    /**
     * Same as {@link #getCasesByType(String, Integer, Integer, String) getCasesByType} but uses default Commcare
     * configuration.
     */
    List<CaseInfo> getCasesByType(String type, Integer pageSize, Integer pageNumber);

    /**
     * Query CommCareHQ for all cases under a given user id.
     *
     * @param userId  the user id from CommCareHQ
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the list of CaseInfo objects representing cases under the given user id and page found on the given
     *          CommcareHQ configuration
     */
    List<CaseInfo> getCasesByUserId(String userId, Integer pageSize, Integer pageNumber, String configName);

    /**
     * Same as {@link #getCasesByUserId(String, Integer, Integer, String) getCasesByUserId} but uses default Commcare
     * configuration.
     */
    List<CaseInfo> getCasesByUserId(String userId, Integer pageSize, Integer pageNumber);

    /**
     * Query CommCareHQ for all cases of a given case type, user id and page.
     *
     * @param userId  the user id from CommCareHQ
     * @param type  the type of case on CommCareHQ
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the list of CaseInfo objects representing cases of the given type, user id and page found on the given
     *          CommcareHQ configuration
     */
    List<CaseInfo> getCasesByUserIdAndType(String userId, String type, Integer pageSize, Integer pageNumber, String configName);

    /**
     * Same as {@link #getCasesByUserIdAndType(String, String, Integer, Integer, String) getCasesByUserIdAndType} but
     * uses default Commcare configuration.
     */
    List<CaseInfo> getCasesByUserIdAndType(String userId, String type, Integer pageSize, Integer pageNumber);

    /**
     * Query CommCareHQ for all cases of a given page.
     *
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the list of CaseInfo objects representing cases of the given page found on the given CommcareHQ
     *          configuration
     */
    List<CaseInfo> getCases(Integer pageSize, Integer pageNumber, String configName);

    /**
     * Same as {@link #getCases(Integer, Integer, String) getCases} but uses default Commcare configuration.
     */
    List<CaseInfo> getCases(Integer pageSize, Integer pageNumber);

    /**
     * Query CommCareHQ for all cases of the given page and cases metadata
     *
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the CasesInfo wrapper, containing CaseInfo objects and case metadata from CommCareHQ
     */
    CasesInfo getCasesWithMetadata(Integer pageSize, Integer pageNumber, String configName);

    /**
     * Same as {@link #getCasesWithMetadata(Integer, Integer, String) getCasesWithMetadata} but uses default Commcare
     * configuration.
     */
    CasesInfo getCasesWithMetadata(Integer pageSize, Integer pageNumber);

    /**
     * Query CommCareHQ for all cases of a given case name, page and cases metadata.
     *
     * @param caseName  the case name of case on CommCareHQ
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the CasesInfo wrapper, containing CaseInfo objects and case metadata from CommCareHQ
     */
    CasesInfo getCasesByCasesNameWithMetadata(String caseName, Integer pageSize, Integer pageNumber, String configName);

    /**
     * Same as {@link #getCasesByCasesNameWithMetadata(String, Integer, Integer, String) getCasesByCasesNameWithMetadata}
     * but uses default Commcare configuration.
     */
    CasesInfo getCasesByCasesNameWithMetadata(String caseName, Integer pageSize, Integer pageNumber);

    /**
     * Query CommCareHQ for all cases of a given date modified range, page and cases metadata.
     *
     * @param dateModifiedStart  the start of the date range
     * @param dateModifiedEnd  the end of the date range
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the CasesInfo wrapper, containing CaseInfo objects and case metadata from CommCareHQ.
     */
    CasesInfo getCasesByCasesTimeWithMetadata(String dateModifiedStart, String dateModifiedEnd, Integer pageSize,
                                              Integer pageNumber, String configName);

    /**
     * Same as {@link #getCasesByCasesTimeWithMetadata(String, String, Integer, Integer, String) getCasesByCasesTimeWithMetadata}
     * but uses default Commcare configuration.
     */
    CasesInfo getCasesByCasesTimeWithMetadata(String dateModifiedStart, String dateModifiedEnd, Integer pageSize, Integer pageNumber);

    /**
     * Query CommCareHQ for all cases of a given case name, date modified range, page and cases metadata.
     *
     * @param caseName  the case name of case on CommCareHQ
     * @param dateModifiedStart  the start of the date range
     * @param dateModifiedEnd  the end of the date range
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the CasesInfo wrapper, containing CaseInfo objects and case metadata from CommCareHQ
     */
    CasesInfo getCasesByCasesNameAndTimeWithMetadata(String caseName, String dateModifiedStart, String dateModifiedEnd,
                                                     Integer pageSize, Integer pageNumber, String configName);

    /**
     * Same as
     * {@link #getCasesByCasesNameAndTimeWithMetadata(String, String, String, Integer, Integer, String) getCasesByCasesNameAndTimeWithMetadata}
     * but uses default Commcare configuration.
     */
    CasesInfo getCasesByCasesNameAndTimeWithMetadata(String caseName, String dateModifiedStart, String dateModifiedEnd,
                                                     Integer pageSize, Integer pageNumber);

    /**
     * Upload case xml wrapped in a minimal xform instance to CommCareHQ.
     *
     * @param caseTask  An object representing the case information and case actions to be submitted as case xml
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  an informational object representing the status, nature and message of the response from CommCareHQ when
     *          attempting to upload this instance of case xml. Returns null if your case xml was incorrect.
     */
    OpenRosaResponse uploadCase(CaseTask caseTask, String configName);

    /**
     * Same as {@link #uploadCase(CaseTask, String) uploadCase} but uses default Commcare configuration.
     */
    OpenRosaResponse uploadCase(CaseTask caseTask);
}
