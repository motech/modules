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

    private MotechEvent event;

    private StockTransactionRequest request;

    private QueryStockLedgerEventHandler eventHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        eventHandler = new QueryStockLedgerEventHandler(stockTransactionService, eventRelay);
        event = prepareEvent();
        request = prepareRequest();
    }

    @Test
    public void shouldHandleEventProperly() throws Exception {

        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        MotechEvent expectedEventOne = prepareExpectedEventOne();
        MotechEvent expectedEventTwo = prepareExpectedEventTwo();

        when(stockTransactionService.getStockTransactions(eq(request), eq(CONFIG_NAME)))
                .thenReturn(prepareStockTransactionsList());

        eventHandler.handleEvent(event);

        verify(eventRelay, times(2)).sendEventMessage(eventCaptor.capture());

        List<MotechEvent> capturedEvents = eventCaptor.getAllValues();

        assertEquals(expectedEventOne, capturedEvents.get(0));
        assertEquals(expectedEventTwo, capturedEvents.get(1));

    }

    @Test(expected = JsonParseException.class)
    public void shouldFailToHandleEventIfResponseIsMalformed() throws Exception {

        when(stockTransactionService.getStockTransactions(eq(request), eq(CONFIG_NAME)))
                .thenThrow(new JsonParseException("Failure"));

        try {
            eventHandler.handleEvent(event);
        } finally {
            verify(eventRelay, never()).sendEventMessage(any(MotechEvent.class));
        }
    }

    private MotechEvent prepareEvent() {

        String subject = EventSubjects.QUERY_STOCK_LEDGER + "." + CONFIG_NAME;

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(EventDataKeys.CASE_ID, RequestTestUtils.CASE_ID);
        params.put(EventDataKeys.SECTION_ID, RequestTestUtils.SECTION_ID);
        params.put(EventDataKeys.START_DATE, RequestTestUtils.START_DATE);
        params.put(EventDataKeys.END_DATE, RequestTestUtils.END_DATE);

        return new MotechEvent(subject, params);
    }

    private MotechEvent prepareExpectedEventOne() {

        Map<String, Object> params = new LinkedHashMap<>();

        params.put(EventDataKeys.PRODUCT_ID, "p1");
        params.put(EventDataKeys.PRODUCT_NAME, null);
        params.put(EventDataKeys.QUANTITY, 0.0);
        params.put(EventDataKeys.SECTION_ID, "s1");
        params.put(EventDataKeys.STOCK_ON_HAND, 13.0);
        params.put(EventDataKeys.TRANSACTION_DATE, "2015-08-10T14:59:55.029219");
        params.put(EventDataKeys.TYPE, "soh");

        return new MotechEvent(EventSubjects.RECEIVED_STOCK_TRANSACTION + "." + CONFIG_NAME, params);
    }

    private MotechEvent prepareExpectedEventTwo() {

        Map<String, Object> params = new LinkedHashMap<>();

        params.put(EventDataKeys.PRODUCT_ID, "p3");
        params.put(EventDataKeys.PRODUCT_NAME, null);
        params.put(EventDataKeys.QUANTITY, 0.0);
        params.put(EventDataKeys.SECTION_ID, "s1");
        params.put(EventDataKeys.STOCK_ON_HAND, 17.0);
        params.put(EventDataKeys.TRANSACTION_DATE, "2015-08-10T14:59:55.029219");
        params.put(EventDataKeys.TYPE, "soh");

        return new MotechEvent(EventSubjects.RECEIVED_STOCK_TRANSACTION + "." + CONFIG_NAME, params);
    }
}