package org.motechproject.commcare.events;

import com.google.gson.JsonParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.request.StockTransactionRequest;
import org.motechproject.commcare.service.CommcareStockTransactionService;
import org.motechproject.commcare.util.RequestTestUtils;
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
import static org.motechproject.commcare.util.CommcareStockTransactionTestUtils.prepareStockTransactionsList;
import static org.motechproject.commcare.util.RequestTestUtils.prepareRequest;

public class QueryStockLedgerEventHandlerTest {

    private static final String CONFIG_NAME = "FooConfig";

    @Mock
    private CommcareStockTransactionService stockTransactionService;

    @Mock
    private EventRelay eventRelay;

    private StockTransactionRequest request;

    private QueryStockLedgerEventHandler eventHandler;

    @Before
    public void setUp() {
        initMocks(this);
        eventHandler = new QueryStockLedgerEventHandler(stockTransactionService, eventRelay);
        request = prepareRequest();
    }

    @Test
    public void shouldHandleEventWithoutExtraDataProperly() {
        testEventHandling(false);
    }

    @Test
    public void shouldHandleEventWithExtraDataProperly() {
        testEventHandling(true);
    }

    @Test(expected = JsonParseException.class)
    public void shouldFailToHandleEventIfResponseIsMalformed() throws Exception {

        when(stockTransactionService.getStockTransactions(eq(request), eq(CONFIG_NAME)))
                .thenThrow(new JsonParseException("Failure"));

        try {
            MotechEvent event = prepareEvent(false);
            eventHandler.handleEvent(event);
        } finally {
            verify(eventRelay, never()).sendEventMessage(any(MotechEvent.class));
        }
    }

    private void testEventHandling(boolean withExtraData) {
        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        MotechEvent expectedEventOne = prepareExpectedEventOne(withExtraData);
        MotechEvent expectedEventTwo = prepareExpectedEventTwo(withExtraData);

        when(stockTransactionService.getStockTransactions(eq(request), eq(CONFIG_NAME)))
                .thenReturn(prepareStockTransactionsList());

        MotechEvent event = prepareEvent(withExtraData);
        eventHandler.handleEvent(event);

        verify(eventRelay, times(2)).sendEventMessage(eventCaptor.capture());

        List<MotechEvent> capturedEvents = eventCaptor.getAllValues();

        assertEquals(expectedEventOne, capturedEvents.get(0));
        assertEquals(expectedEventTwo, capturedEvents.get(1));
    }

    private MotechEvent prepareEvent(boolean withExtraData) {

        String subject = EventSubjects.QUERY_STOCK_LEDGER + "." + CONFIG_NAME;

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(EventDataKeys.CASE_ID, RequestTestUtils.CASE_ID);
        params.put(EventDataKeys.SECTION_ID, RequestTestUtils.SECTION_ID);
        params.put(EventDataKeys.START_DATE, RequestTestUtils.START_DATE);
        params.put(EventDataKeys.END_DATE, RequestTestUtils.END_DATE);

        if (withExtraData) {
            Map<String, String> extraData = new HashMap<>();

            extraData.put("key1", "val1");
            extraData.put("key2", "val2");

            params.put(EventDataKeys.EXTRA_DATA, extraData);
        }

        return new MotechEvent(subject, params);
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
}