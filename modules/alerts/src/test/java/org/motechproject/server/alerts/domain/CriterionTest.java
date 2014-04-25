package org.motechproject.server.alerts.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commons.api.Range;
import org.motechproject.server.alerts.contract.AlertCriteria;
import org.motechproject.server.alerts.contract.AlertsDataService;
import org.motechproject.server.alerts.contract.Criterion;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.setTimeZoneUTC;

public class CriterionTest {
    @Mock
    private AlertsDataService alertsDataService;
    private AlertCriteria alertCriteria;

    @Before
    public void setUp() {
        initMocks(this);
        alertCriteria = new AlertCriteria();
    }

    @Test
    public void fetchAlerts_GivenADateRangeCriteria() {
        DateTime fromDate = newDateTime(new LocalDate(2010, 10, 10), 0, 0, 0);
        DateTime toDate = newDateTime(new LocalDate(2010, 10, 20), 0, 0, 0);
        alertCriteria.byDateRange(fromDate, toDate);

        ArrayList<Alert> expectedAlerts = new ArrayList<Alert>() {{
            add(new Alert());
        }};
        when(alertsDataService.findByDateTime(new Range<>(fromDate, toDate))).thenReturn(expectedAlerts);

        List<Alert> actualAlerts = Criterion.DATE_RANGE.fetch(alertsDataService, alertCriteria);
        assertEquals(expectedAlerts, actualAlerts);
    }

    @Test
    public void filterAlerts_GivenADateRangeCriteria() {
        final DateTime dayOne = newDateTime(new LocalDate(2010, 10, 1), 0, 0, 0);
        final DateTime dayTwo = newDateTime(new LocalDate(2010, 10, 2), 0, 0, 0);
        final DateTime dayThree = newDateTime(new LocalDate(2010, 10, 3), 0, 0, 0);

        alertCriteria.byDateRange(dayTwo, dayThree);

        ArrayList<Alert> allAlerts = new ArrayList<Alert>() {{
            add(new Alert(){{ setDateTime(dayOne); }});
            add(new Alert(){{ setDateTime(dayTwo); }});
            add(new Alert(){{ setDateTime(dayThree); }});
        }};

        List<Alert> filteredAlerts = Criterion.DATE_RANGE.filter(allAlerts, alertCriteria);

        assertTrue(hasItem(filteredAlerts, setTimeZoneUTC(dayTwo)));
        assertTrue(hasItem(filteredAlerts, setTimeZoneUTC(dayThree)));
    }

    private boolean hasItem(List<Alert> list, DateTime dateTime) {
        for (Alert alert : list) {
            if (alert.getDateTime().isEqual(dateTime)) {
                return true;
            }
        }

        return false;
    }
}
