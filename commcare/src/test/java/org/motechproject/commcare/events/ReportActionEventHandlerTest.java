package org.motechproject.commcare.events;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.ReportActionService;
import org.motechproject.commcare.testutil.RequestTestUtils;
import org.motechproject.event.MotechEvent;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportActionEventHandlerTest {

    private static final String CONFIG_NAME = "FooConfig";
    private static final String REPORT_ID = "Dddddd";
    private static final String REPORT_NAME = "Report Name";

    @Mock
    private ReportActionService reportActionService;

    private ReportActionEventHandler eventHandler;

    @Before
    public void setUp() {
        initMocks(this);

        eventHandler = new ReportActionEventHandler(reportActionService);
    }

    @Test
    public void shouldConvertEventParametersAndReceiveReport() {
        MotechEvent event = prepareEvent();
        eventHandler.receiveReport(event);

        verify(reportActionService).queryReport(
                eq(CONFIG_NAME),
                eq(REPORT_ID),
                eq(REPORT_NAME),
                eq("&unicode_filter=Sample value&date_filter-start=2012-11-01&date_filter-end=2012-12-24&numeric_filter-operator==&numeric_filter-operand=100")
        );
    }

    private MotechEvent prepareEvent() {

        String subject = EventSubjects.REPORT_EVENT + "." + REPORT_NAME + "." + CONFIG_NAME;

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("unicode_filter", "Sample value");
        params.put("unicode_filter.type", "CHOICE_LIST");

        params.put("date_filter", RequestTestUtils.START_DATE);
        params.put("date_filter.commcare.endDate", RequestTestUtils.END_DATE);
        params.put("date_filter.type", "DATE");

        params.put("numeric_filter", "=");
        params.put("numeric_filter.value", "100");
        params.put("numeric_filter.type", "DECIMAL");

        params.put(EventDataKeys.REPORT_ID, REPORT_ID);
        params.put(EventDataKeys.REPORT_NAME, REPORT_NAME);

        return new MotechEvent(subject, params);
    }
}
