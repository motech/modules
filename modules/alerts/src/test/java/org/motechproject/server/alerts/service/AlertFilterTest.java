package org.motechproject.server.alerts.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.server.alerts.contract.AlertCriteria;
import org.motechproject.server.alerts.contract.AlertsDataService;
import org.motechproject.server.alerts.domain.Alert;
import org.motechproject.server.alerts.domain.AlertStatus;
import org.motechproject.server.alerts.domain.AlertType;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AlertFilterTest {

    private AlertFilter alertFilter;

    @Mock
    private AlertsDataService alertsDataService;

    @Before
    public void setup() {
        initMocks(this);
        alertFilter = new AlertFilter(alertsDataService);
    }

    @Test
    public void shouldMakeRepositoryCallForPrimaryCriteriaOnly() {
        alertFilter.search(new AlertCriteria().byStatus(AlertStatus.NEW).byType(AlertType.LOW).byExternalId("entity_id").byPriority(2));
        verify(alertsDataService).findByStatus(AlertStatus.NEW);
        verify(alertsDataService, times(0)).findByAlertType(Matchers.<AlertType>any());
        verify(alertsDataService, times(0)).findByExternalId(anyString());
        verify(alertsDataService, times(0)).findByPriority(anyLong());
    }

    @Test
    public void shouldSearchByPrimaryCriterion() {
        List<Alert> alerts = (List<Alert>) mock(List.class);
        when(alertsDataService.findByAlertType(AlertType.HIGH)).thenReturn(alerts);
        assertEquals(alerts, alertFilter.search(new AlertCriteria().byType(AlertType.HIGH)));
    }

    @Test
    public void shouldSearchByPrimaryCriterion_AsDateRange() {
        assertEquals(0, alertFilter.search(new AlertCriteria().byDateRange(null, null)).size());
    }
}
