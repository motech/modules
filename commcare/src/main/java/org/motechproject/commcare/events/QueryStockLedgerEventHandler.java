package org.motechproject.commcare.events;

import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.QueryStockLedgerActionService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.END_DATE;
import static org.motechproject.commcare.events.constants.EventDataKeys.EXTRA_DATA;
import static org.motechproject.commcare.events.constants.EventDataKeys.SECTION_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.START_DATE;

/**
 * Class responsible for handling "Query Stock Ledger" events. The service will extract the necessary data
 * from the {@link MotechEvent} and pass them to the {@link QueryStockLedgerActionService} service that will
 * query the CommCare stock transaction API and then send a motech event for every parsed stock transaction.
 */
@Component
public class QueryStockLedgerEventHandler {

    private QueryStockLedgerActionService queryStockLedgerActionService;

    /**
     * Handles the {@code EventSubjects.QUERY_STOCK_LEDGER} events. This will result in fetching stock transactions from
     * the CommCareHQ server(based on the event parameters) and then sending motech event for every parsed stock
     * transaction.
     *
     * @param event  the event to be handled
     */
    @MotechListener(subjects = EventSubjects.QUERY_STOCK_LEDGER + ".*")
    public void handleEvent(MotechEvent event) {
        String configName = EventSubjects.getConfigName(event.getSubject());
        Map<String, Object> parameters = event.getParameters();

        String caseId = (String) parameters.get(CASE_ID);
        String sectionId = (String) parameters.get(SECTION_ID);
        String startDate = (String) parameters.get(START_DATE);
        String endDate = (String) parameters.get(END_DATE);
        Map<String, Object> extraData = (Map<String, Object>) parameters.get(EXTRA_DATA);

        queryStockLedgerActionService.queryStockLedger(configName, caseId, sectionId, startDate, endDate, extraData);
    }

    @Autowired
    public QueryStockLedgerEventHandler(QueryStockLedgerActionService queryStockLedgerActionService) {
        this.queryStockLedgerActionService = queryStockLedgerActionService;
    }
}
