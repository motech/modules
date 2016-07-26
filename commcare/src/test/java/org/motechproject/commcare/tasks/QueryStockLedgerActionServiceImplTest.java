package org.motechproject.commcare.tasks;

import com.google.gson.JsonParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.request.StockTransactionRequest;
import org.motechproject.commcare.service.CommcareStockTransactionService;
import org.motechproject.commcare.testutil.RequestTestUtils;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commcare.testutil.RequestTestUtils.prepareRequest;
import static org.motechproject.commcare.util.CommcareStockTransactionTestUtils.prepareStockTransactionsList;

public class QueryStockLedgerActionServiceImplTest {

    private static final String CONFIG_NAME = "FooConfig";

    @Mock
    private CommcareStockTransactionService stockTransactionService;

    @Mock
    private EventRelay eventRelay;

    private StockTransactionRequest request;

    private QueryStockLedgerActionServiceImpl queryStockLedgerActionService;

    @Before
    public void setUp() {
        initMocks(this);
        queryStockLedgerActionService = new QueryStockLedgerActionServiceImpl(stockTransactionService, eventRelay);
        request = prepareRequest();
    }

    @Test
    public void testQueryStockLedgerWithoutExtraData() {
        testQueryStockLedger(false);
    }

    @Test
    public void testQueryStockLedgerWithExtraData() {
        testQueryStockLedger(true);
    }

    @Test(expected = JsonParseException.class)
    public void shouldFailToHandleEventIfResponseIsMalformed() throws Exception {

        when(stockTransactionService.getStockTransactions(eq(request), eq(CONFIG_NAME)))
                .thenThrow(new JsonParseException("Failure"));

        try {
            queryStockLedgerActionService.queryStockLedger(CONFIG_NAME, RequestTestUtils.CASE_ID,
                    RequestTestUtils.SECTION_ID, RequestTestUtils.START_DATE, RequestTestUtils.END_DATE, null);
        } finally {
            verify(eventRelay, never()).sendEventMessage(any(MotechEvent.class));
        }
    }

    private void testQueryStockLedger(boolean withExtraData) {
        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        MotechEvent expectedEventOne = prepareExpectedEventOne(withExtraData);
        MotechEvent expectedEventTwo = prepareExpectedEventTwo(withExtraData);

        when(stockTransactionService.getStockTransactions(eq(request), eq(CONFIG_NAME)))
                .thenReturn(prepareStockTransactionsList());

        queryStockLedgerActionService.queryStockLedger(CONFIG_NAME, RequestTestUtils.CASE_ID,
                RequestTestUtils.SECTION_ID, RequestTestUtils.START_DATE, RequestTestUtils.END_DATE,
                withExtraData ? getExtraData() : null);

        verify(eventRelay, times(2)).sendEventMessage(eventCaptor.capture());

        List<MotechEvent> capturedEvents = eventCaptor.getAllValues();

        assertEquals(expectedEventOne, capturedEvents.get(0));
        assertEquals(expectedEventTwo, capturedEvents.get(1));
    }

    private MotechEvent prepareExpectedEventOne(boolean withExtraData) {

        Map<String, Object> params = new LinkedHashMap<>();

        params.put(EventDataKeys.PRODUCT_ID, "p1");
        params.put(EventDataKeys.PRODUCT_NAME, null);
        params.put(EventDataKeys.QUANTITY, 0.0);
        params.put(EventDataKeys.SECTION_ID, "s1");
        params.put(EventDataKeys.STOCK_ON_HAND, 13.0);
        params.put(EventDataKeys.TRANSACTION_DATE, "2015-08-10T14:59:55.029219");
        params.put(EventDataKeys.TYPE, "soh");

        if (withExtraData) {
            params.put("key1", "val1");
            params.put("key2", "val2");
        }

        return new MotechEvent(EventSubjects.RECEIVED_STOCK_TRANSACTION + "." + CONFIG_NAME, params);
    }

    private MotechEvent prepareExpectedEventTwo(boolean withExtraData) {

        Map<String, Object> params = new LinkedHashMap<>();

        params.put(EventDataKeys.PRODUCT_ID, "p3");
        params.put(EventDataKeys.PRODUCT_NAME, null);
        params.put(EventDataKeys.QUANTITY, 0.0);
        params.put(EventDataKeys.SECTION_ID, "s1");
        params.put(EventDataKeys.STOCK_ON_HAND, 17.0);
        params.put(EventDataKeys.TRANSACTION_DATE, "2015-08-10T14:59:55.029219");
        params.put(EventDataKeys.TYPE, "soh");

        if (withExtraData) {
            params.put("key1", "val1");
            params.put("key2", "val2");
        }

        return new MotechEvent(EventSubjects.RECEIVED_STOCK_TRANSACTION + "." + CONFIG_NAME, params);
    }

    private static Map<String, Object> getExtraData() {
        Map<String, Object> extraData = new HashMap<>();

        extraData.put("key1", "val1");
        extraData.put("key2", "val2");

        return extraData;
    }
}
