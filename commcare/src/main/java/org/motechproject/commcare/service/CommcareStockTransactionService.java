package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareStockTransactionList;
import org.motechproject.commcare.request.StockTransactionRequest;

/**
 * A service to perform queries against CommCareHQ stock transaction APIs.
 */
public interface CommcareStockTransactionService {

    /**
     * Retrieves a list of stock transactions, based on provided query parameters. It uses the default configuration.
     *
     * @param request  the request query parameters
     * @return the list of stock transactions
     */
    CommcareStockTransactionList getStockTransactions(StockTransactionRequest request);

    /**
     * Retrieves a list of stock transactions, based on provided query parameters and configuration name.
     *
     * @param request  the request query parameters
     * @param configName  the name of the configuration
     * @return the list of stock transactions
     */
    CommcareStockTransactionList getStockTransactions(StockTransactionRequest request, String configName);
}
