package org.motechproject.commcare.events;

import org.joda.time.DateTime;

import java.util.Map;

/**
 * Created by root on 21.07.16.
 */
public interface QueryStockLedgerActionService {
    void queryStockLedger(String configName, String caseId, String sectionId, DateTime startDate, DateTime endDate, Map<String, Object> extraData);
}
