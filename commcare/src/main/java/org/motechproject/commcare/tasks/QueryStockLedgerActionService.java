package org.motechproject.commcare.tasks;

import org.joda.time.DateTime;

import java.util.Map;

/**
 * This service is responsible for handling Commcare stock ledger actions in tasks.
 */
public interface QueryStockLedgerActionService {

    /**
     * This task action allows to query the Commcare stock ledger based on the incoming Case ID and Section ID.
     * When the stock ledger is queried, each response is parsed by MOTECH and a Received Stock Transaction
     * event is raised.
     *
     * @param configName  the name of the configuration used for connecting to CommcareHQ
     * @param caseId  the id of the case on CommCareHQ
     * @param sectionId  the ID of the section
     * @param startDate  the beginning of the allowed period for transaction dates
     * @param endDate  the finish of the allowed period for transaction dates
     * @param extraData   the extra data that will be sent with stock transaction as an event
     */
    void queryStockLedger(String configName, String caseId, String sectionId, String startDate, String endDate, Map<String, Object> extraData);

    /**
     * Same as {@link #queryStockLedger(String, String, String, String, String, Map)} queryStockLedger} but
     * handles dates in {@link DateTime} type.
     */
    void queryStockLedger(String configName, String caseId, String sectionId, DateTime startDate, DateTime endDate, Map<String, Object> extraData);
}
