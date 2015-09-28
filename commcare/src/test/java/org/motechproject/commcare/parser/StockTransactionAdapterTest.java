package org.motechproject.commcare.parser;

import com.google.gson.JsonParseException;
import org.junit.Test;
import org.motechproject.commcare.domain.CommcareStockTransactionList;
import org.motechproject.commcare.util.JsonTestUtils;

import static org.motechproject.commcare.util.CommcareStockTransactionTestUtils.assertStockTransactionListsEqual;
import static org.motechproject.commcare.util.CommcareStockTransactionTestUtils.prepareStockTransactionsList;

public class StockTransactionAdapterTest {

    private static final String VALID_STOCK_LEDGER = "json/domain/sampleStockLedger.json";
    private static final String MALFORMED_STOCK_LEDGER = "json/domain/malformedStockLedger.json";

    @Test
    public void shouldDeserializeListIfJsonIfValid() throws Exception {

        String json = JsonTestUtils.loadFromFile(VALID_STOCK_LEDGER);

        CommcareStockTransactionList transactionList = StockTransactionAdapter.readListJson(json);

        assertStockTransactionListsEqual(prepareStockTransactionsList(), transactionList);
    }

    @Test(expected = JsonParseException.class)
    public void shouldThrowExceptionIfJsonIsMalformed() throws Exception {

        String json = JsonTestUtils.loadFromFile(MALFORMED_STOCK_LEDGER);

        StockTransactionAdapter.readListJson(json);
    }
}
