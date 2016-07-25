package org.motechproject.commcare.service.impl;

import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CommcareStockTransaction;
import org.motechproject.commcare.domain.CommcareStockTransactionList;
import org.motechproject.commcare.request.StockTransactionRequest;
import org.motechproject.commcare.service.CommcareStockTransactionService;
import org.motechproject.commcare.service.QueryStockLedgerActionService;
import org.motechproject.commcare.util.CommcareParamHelper;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.motechproject.commcare.events.constants.EventDataKeys.PRODUCT_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.PRODUCT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.QUANTITY;
import static org.motechproject.commcare.events.constants.EventDataKeys.SECTION_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.STOCK_ON_HAND;
import static org.motechproject.commcare.events.constants.EventDataKeys.TRANSACTION_DATE;
import static org.motechproject.commcare.events.constants.EventDataKeys.TYPE;
import static org.motechproject.commcare.events.constants.EventSubjects.RECEIVED_STOCK_TRANSACTION;

/**
 * This service is responsible for handling "Query Stock Ledger" action in tasks.
 */
@Service("queryStockLedgerActionService")
public class QueryStockLedgerActionServiceImpl implements QueryStockLedgerActionService {

    private CommcareStockTransactionService stockTransactionService;
    private EventRelay eventRelay;

    @Autowired
    public QueryStockLedgerActionServiceImpl(CommcareStockTransactionService stockTransactionService, EventRelay eventRelay) {
        this.stockTransactionService = stockTransactionService;
        this.eventRelay = eventRelay;
    }

    @Override
    public void queryStockLedger(String configName, String caseId, String sectionId, String startDate, String endDate, Map<String, Object> extraData) {

        StockTransactionRequest request = new StockTransactionRequest();
        request.setCaseId(caseId);
        request.setSectionId(sectionId);
        request.setStartDate(startDate);
        request.setEndDate(endDate);

        CommcareStockTransactionList transactions = stockTransactionService.getStockTransactions(request, configName);

        for (CommcareStockTransaction transaction : transactions.getObjects()) {
            sendStockTransactionAsEvent(transaction, configName, extraData);
        }
    }

    private void sendStockTransactionAsEvent(CommcareStockTransaction transaction, String configName,
                                             Map<String, Object> extraData) {

        Map<String, Object> eventParams = new LinkedHashMap<>();

        if (extraData != null) {
            eventParams.putAll(extraData);
        }

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

    @Override
    public void queryStockLedger(String configName, String caseId, String sectionId, DateTime startDate, DateTime endDate, Map<String, Object> extraData) {
        queryStockLedger(configName, caseId, sectionId, CommcareParamHelper.printObjectAsDateTime(startDate),
                CommcareParamHelper.printObjectAsDateTime(endDate), extraData);
    }
}
