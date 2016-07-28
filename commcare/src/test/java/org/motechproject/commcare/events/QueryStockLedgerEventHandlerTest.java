package org.motechproject.commcare.events;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.QueryStockLedgerActionService;
import org.motechproject.commcare.testutil.RequestTestUtils;
import org.motechproject.event.MotechEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class QueryStockLedgerEventHandlerTest {

    private static final List<DateTime> DATES = Arrays.asList(
            new DateTime(2012, 11, 1, 10, 20, 33),
            new DateTime(2012, 12, 24, 1, 1, 59)
    );

    private static final String CONFIG_NAME = "FooConfig";

    @Mock
    private QueryStockLedgerActionService queryStockLedgerActionService;

    private QueryStockLedgerEventHandler eventHandler;

    @Before
    public void setUp() {
        initMocks(this);

        eventHandler = new QueryStockLedgerEventHandler(queryStockLedgerActionService);
    }

    @Test
    public void shouldCallQueryStockLedgerWithoutExtraDataProperly() {
        testQueryStockLedger(false);
    }

    @Test
    public void shouldCallQueryStockLedgerWithExtraDataProperly() {
        testQueryStockLedger(true);
    }

    private void testQueryStockLedger(boolean withExtraData) {
        Class<Map<String, Object>> mapClass = (Class<Map<String, Object>>)(Class)Map.class;
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(mapClass);

        MotechEvent event = prepareEvent(withExtraData);
        eventHandler.handleEvent(event);

        verify(queryStockLedgerActionService).queryStockLedger(
                eq(CONFIG_NAME),
                eq(RequestTestUtils.CASE_ID),
                eq(RequestTestUtils.SECTION_ID),
                eq(DATES.get(0)),
                eq(DATES.get(1)),
                captor.capture()
        );

        Map<String, Object> actual = captor.getValue();

        if (withExtraData) {
            assertEquals(2, actual.size());
        }
    }

    private MotechEvent prepareEvent(boolean withExtraData) {

        String subject = EventSubjects.QUERY_STOCK_LEDGER + "." + CONFIG_NAME;

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(EventDataKeys.CASE_ID, RequestTestUtils.CASE_ID);
        params.put(EventDataKeys.SECTION_ID, RequestTestUtils.SECTION_ID);
        params.put(EventDataKeys.START_DATE, DATES.get(0));
        params.put(EventDataKeys.END_DATE, DATES.get(1));

        if (withExtraData) {
            Map<String, Object> extraData = new HashMap<>();

            extraData.put("key1", "val1");
            extraData.put("key2", "val2");

            params.put(EventDataKeys.EXTRA_DATA, extraData);
        }

        return new MotechEvent(subject, params);
    }
}
