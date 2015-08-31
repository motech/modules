package org.motechproject.commcare.events;

import org.motechproject.commcare.domain.CommcareStockTransaction;
import org.motechproject.commcare.domain.CommcareStockTransactionList;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.request.StockTransactionRequest;
import org.motechproject.commcare.service.CommcareStockTransactionService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.END_DATE;
import static org.motechproject.commcare.events.constants.EventDataKeys.PRODUCT_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.PRODUCT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.QUANTITY;
import static org.motechproject.commcare.events.constants.EventDataKeys.SECTION_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.START_DATE;
import static org.motechproject.commcare.events.constants.EventDataKeys.STOCK_ON_HAND;
import static org.motechproject.commcare.events.constants.EventDataKeys.TRANSACTION_DATE;
import static org.motechproject.commcare.events.constants.EventDataKeys.TYPE;
import static org.motechproject.commcare.events.constants.EventSubjects.RECEIVED_STOCK_TRANSACTION;

/**
 * Class responsible for handling "Query Stock Ledger" events. The service will query the CommCare stock transaction
 * API and then send a motech event for every parsed stock transaction.
 */
@Component
public class QueryStockLedgerEventHandler {

    private CommcareStockTransactionService stockTransactionService;
    private EventRelay eventRelay;

    /**
     * Handles the {@code EventSubjects.QUERY_STOCK_LEDGER} events. This will result in fetching stock transactions from
     * the CommCareHQ server(based on the event parameters) and then sending motech event for every parsed stock
     * transaction.
     *
     * @param event  the event to be handled
     */
    @MotechListener(subjects = EventSubjects.QUERY_STOCK_LEDGER + ".*")
    public void handleEvent(MotechEvent event) {

        String configName = getConfigName(event.getSubject());

        StockTransactionRequest request = parseEventToRequest(event);

        CommcareStockTransactionList transactions = stockTransactionService.getStockTransactions(request, configName);

        for (CommcareStockTransaction transaction : transactions.getObjects()) {
            sendStockTransactionAsEvent(transaction, configName);
        }
    }

    private void sendStockTransactionAsEvent(CommcareStockTransaction transaction, String configName) {

        Map<String, Object> eventParams = new LinkedHashMap<>();

        eventParams.put(PRODUCT_ID, transaction.getProductId());
        eventParams.put(PRODUCT_NAME, transaction.getProductName());
        eventParams.put(QUANTITY, transaction.getQuantity());
        eventParams.put(SECTION_ID, transaction.getSectionId());
        eventParams.put(STOCK_ON_HAND, transaction.getStockOnHand());
        eventParams.put(TRANSACTION_DATE, transaction.getTransactionDate());
        eventParams.put(TYPE, transaction.getType());

        eventRelay.sendEventMessage(new MotechEvent(RECEIVED_STOCK_TRANSACTION + "." + configName,
                eventParams));
    }

    private StockTransactionRequest parseEventToRequest(MotechEvent event) {
        StockTransactionRequest request = new StockTransactionRequest();
        request.setCaseId((String) event.getParameters().get(CASE_ID));
        request.setSectionId((String) event.getParameters().get(SECTION_ID));
        request.setStartDate((String) event.getParameters().get(START_DATE));
        request.setEndDate((String) event.getParameters().get(END_DATE));
        return request;
    }

    private String getConfigName(String subject) {
        return subject.substring(subject.lastIndexOf('.') + 1);
    }

    @Autowired
    public QueryStockLedgerEventHandler(CommcareStockTransactionService stockTransactionService, EventRelay eventRelay) {
        this.stockTransactionService = stockTransactionService;
        this.eventRelay = eventRelay;
    }
}
