package org.motechproject.commcare.service.impl;

import com.google.gson.JsonParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareStockTransactionList;
import org.motechproject.commcare.request.StockTransactionRequest;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.JsonTestUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commcare.util.CommcareStockTransactionTestUtils.assertStockTransactionListsEqual;
import static org.motechproject.commcare.util.CommcareStockTransactionTestUtils.prepareStockTransactionsList;
import static org.motechproject.commcare.util.ConfigTestUtils.prepareConfig;
import static org.motechproject.commcare.util.JsonTestUtils.MALFORMED_STOCK_LEDGER;
import static org.motechproject.commcare.util.JsonTestUtils.VALID_STOCK_LEDGER;
import static org.motechproject.commcare.testutil.RequestTestUtils.prepareRequest;

public class CommcareStockTransactionServiceImplTest {

    private static final String CONFIG_NAME = "fooConfig";

    @Mock
    private CommcareConfigService configService;

    @Mock
    private CommCareAPIHttpClient commCareAPIHttpClient;

    private CommcareStockTransactionServiceImpl stockTransactionService;

    private Config config;

    private StockTransactionRequest request;

    private CommcareStockTransactionList expectedTransactions;

    @Before
    public void setUp() {
        initMocks(this);
        config = prepareConfig(CONFIG_NAME);
        request = prepareRequest();
        expectedTransactions = prepareStockTransactionsList();
        when(configService.getByName(eq(CONFIG_NAME))).thenReturn(config);
        when(configService.getByName(null)).thenReturn(config);
        stockTransactionService = new CommcareStockTransactionServiceImpl(commCareAPIHttpClient, configService);
    }

    @Test
    public void shouldFetchStockTransactionsWithConfigName() throws Exception {

        when(commCareAPIHttpClient.stockTransactionsRequest(config.getAccountConfig(), request))
                .thenReturn(JsonTestUtils.loadFromFile(VALID_STOCK_LEDGER));

        CommcareStockTransactionList transactions = stockTransactionService.getStockTransactions(request, CONFIG_NAME);

        assertStockTransactionListsEqual(expectedTransactions, transactions);

        verify(commCareAPIHttpClient, times(1))
                .stockTransactionsRequest(any(AccountConfig.class), any(StockTransactionRequest.class));
    }

    @Test
    public void shouldFetchStockTransactionsWithoutConfigName() throws Exception {

        when(commCareAPIHttpClient.stockTransactionsRequest(config.getAccountConfig(), request))
                .thenReturn(JsonTestUtils.loadFromFile(VALID_STOCK_LEDGER));

        CommcareStockTransactionList transactions = stockTransactionService.getStockTransactions(request);

        assertStockTransactionListsEqual(expectedTransactions, transactions);

        verify(commCareAPIHttpClient, times(1))
                .stockTransactionsRequest(any(AccountConfig.class), any(StockTransactionRequest.class));
    }

    @Test(expected = JsonParseException.class)
    public void shouldThrowExceptionIfServerResponseIsMalformed() throws Exception {

        when(commCareAPIHttpClient.stockTransactionsRequest(config.getAccountConfig(), request))
                .thenReturn(JsonTestUtils.loadFromFile(MALFORMED_STOCK_LEDGER));

        try {
            stockTransactionService.getStockTransactions(request);
        } finally {
            verify(commCareAPIHttpClient, times(1))
                    .stockTransactionsRequest(any(AccountConfig.class), any(StockTransactionRequest.class));
        }
    }
}
