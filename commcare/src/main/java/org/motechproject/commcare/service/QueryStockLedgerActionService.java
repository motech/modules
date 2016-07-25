package org.motechproject.commcare.service;

import org.joda.time.DateTime;

import java.util.Map;

/**
 * This service is responsible for handling "Query Stock Ledger" action in tasks.
 */
public interface QueryStockLedgerActionService {
    void queryStockLedger(String configName, String caseId, String sectionId, DateTime startDate, DateTime endDate, Map<String, Object> extraData);
    void queryStockLedger(String configName, String caseId, String sectionId, String startDate, String endDate, Map<String, Object> extraData);
}
