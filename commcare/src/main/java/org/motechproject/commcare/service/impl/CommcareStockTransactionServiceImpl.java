package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.domain.CommcareStockTransactionList;
import org.motechproject.commcare.parser.StockTransactionAdapter;
import org.motechproject.commcare.request.StockTransactionRequest;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareStockTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the {@link CommcareStockTransactionService} interface.
 */
@Service
public class CommcareStockTransactionServiceImpl implements CommcareStockTransactionService {

    private CommCareAPIHttpClient commcareHttpClient;
    private CommcareConfigService configService;

    @Override
    public CommcareStockTransactionList getStockTransactions(StockTransactionRequest request) {
        return getStockTransactions(request, null);
    }

    @Override
    public CommcareStockTransactionList getStockTransactions(StockTransactionRequest request, String configName) {
        String responseJson = commcareHttpClient.stockTransactionsRequest(getAccountConfig(configName), request);

        return StockTransactionAdapter.readListJson(responseJson);
    }

    private AccountConfig getAccountConfig(String configName) {
        return configService.getByName(configName).getAccountConfig();
    }

    @Autowired
    public CommcareStockTransactionServiceImpl(CommCareAPIHttpClient commcareHttpClient, CommcareConfigService configService) {
        this.commcareHttpClient = commcareHttpClient;
        this.configService = configService;
    }
}
