package org.motechproject.commcare.tasks;

import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CaseTask;

import java.util.Map;

/**
 * Proxy, exposing the Commcare services as task actions.
 * It forwards the data to proper service that will handle the execution of the method
 */
public interface CommcareActionProxyService {

    /**
     * See {@link org.motechproject.commcare.service.CaseActionService#createCase(String, String, String, String, Map) createCase}
     */
    CaseTask createCase(String configName, String caseType, String ownerId, String caseName, Map<String, Object> fieldValues);

    /**
     * See {@link org.motechproject.commcare.service.CaseActionService#updateCase(String, String, String, Boolean, Map) updateCase}
     */
    void updateCase(String configName, String caseId, String ownerId, Boolean closeCase, Map<String, Object> fieldValues);

    /**
     * See {@link org.motechproject.commcare.service.imports.ImportFormActionService#importForms(String, DateTime, DateTime) importForms}
     */
    void importForms(String configName, DateTime startDate, DateTime endDate);

    /**
     * See {@link org.motechproject.commcare.service.QueryStockLedgerActionService#queryStockLedger(String, String, String, DateTime, DateTime, Map) queryStockLedger}
     */
    void queryStockLedger(String configName, String caseId, String sectionId, DateTime startDate, DateTime endDate, Map<String, Object> extraData);

    /**
     * See {@link org.motechproject.commcare.service.ReportActionService#queryReport(String, String, String, String)}
     */
    void queryReport (String configName, String reportId, String reportName, String urlParsedFilters);
}
