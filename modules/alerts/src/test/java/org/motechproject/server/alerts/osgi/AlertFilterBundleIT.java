package org.motechproject.server.alerts.osgi;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.server.alerts.contract.AlertCriteria;
import org.motechproject.server.alerts.domain.Alert;
import org.motechproject.server.alerts.domain.AlertStatus;
import org.motechproject.server.alerts.domain.AlertType;
import org.motechproject.server.alerts.service.AlertFilter;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlertFilterBundleIT extends AlertsBaseIT {
    private AlertFilter alertFilter;

    @Before
    public void setup() {
        alertFilter = new AlertFilter(alertsDataService);
    }

    @Test
    public void shouldSearchByPrimaryCriterion() throws Exception {
        createAlert("entity_id1", AlertType.CRITICAL, AlertStatus.CLOSED, 3, null);
        createAlert("entity_id2", AlertType.CRITICAL, AlertStatus.CLOSED, 3, null);
        createAlert("entity_id1", AlertType.MEDIUM, AlertStatus.READ, 2, null);
        createAlert("entity_id1", AlertType.MEDIUM, AlertStatus.NEW, 1, null);

        List<Alert> alerts = alertFilter.search(new AlertCriteria().byExternalId("entity_id1"));

        assertResults(alerts, "externalId", "entity_id1", "entity_id1", "entity_id1");
    }

    @Test
    public void shouldSearchByOneSecondaryCriterion() throws Exception {
        createAlert("entity_id1", AlertType.CRITICAL, AlertStatus.CLOSED, 3, null);
        createAlert("entity_id2", AlertType.CRITICAL, AlertStatus.CLOSED, 3, null);
        createAlert("entity_id1", AlertType.MEDIUM, AlertStatus.READ, 2, null);
        createAlert("entity_id1", AlertType.MEDIUM, AlertStatus.NEW, 1, null);

        List<Alert> alerts = alertFilter.search(new AlertCriteria().byExternalId("entity_id1").byType(AlertType.MEDIUM));

        assertResults(alerts, "externalId", "entity_id1", "entity_id1");
        assertResults(alerts, "alertType", AlertType.MEDIUM, AlertType.MEDIUM);
    }

    @Test
    public void shouldSearchByTwoSecondaryCriteria() throws Exception {
        createAlert("entity_id1", AlertType.CRITICAL, AlertStatus.NEW, 3, null);
        createAlert("entity_id2", AlertType.CRITICAL, AlertStatus.CLOSED, 3, null);
        createAlert("entity_id1", AlertType.MEDIUM, AlertStatus.READ, 2, null);
        createAlert("entity_id1", AlertType.MEDIUM, AlertStatus.NEW, 1, null);

        List<Alert> alerts = alertFilter.search(new AlertCriteria().byExternalId("entity_id1").byType(AlertType.MEDIUM).byStatus(AlertStatus.NEW));

        assertResults(alerts, "externalId", "entity_id1");
        assertResults(alerts, "alertType", AlertType.MEDIUM);
        assertResults(alerts, "status", AlertStatus.NEW);
    }

    @Test
    public void shouldSearchByNoCriteria() throws Exception {
        createAlert("entity_id1", AlertType.CRITICAL, AlertStatus.CLOSED, 3, null);
        createAlert("entity_id2", AlertType.CRITICAL, AlertStatus.CLOSED, 3, null);

        List<Alert> alerts = alertFilter.search(new AlertCriteria());

        assertResults(alerts, "externalId", "entity_id1", "entity_id2");
    }

    private Alert createAlert(String externalId, AlertType critical, AlertStatus aNew, Integer priority, HashMap<String, String> alertData) {
        Alert alert = new Alert(externalId, critical, aNew, priority, alertData);
        return alertsDataService.create(alert);
    }
}
