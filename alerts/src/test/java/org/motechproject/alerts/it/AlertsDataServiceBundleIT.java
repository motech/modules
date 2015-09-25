package org.motechproject.alerts.it;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.alerts.domain.Alert;
import org.motechproject.alerts.domain.AlertStatus;
import org.motechproject.alerts.domain.AlertType;
import org.motechproject.commons.api.Range;
import org.motechproject.commons.date.util.DateUtil;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlertsDataServiceBundleIT extends AlertsBaseIT {

    @Test
    public void shouldReturnAllAlertsWithGivenExternalIdOnly() throws Exception {
        createAlert("included_entity", AlertType.HIGH, AlertStatus.CLOSED, 3, null, DateUtil.now());
        createAlert("excluded_entity", AlertType.LOW, AlertStatus.CLOSED, 8, null, DateUtil.now());
        createAlert("included_entity", AlertType.LOW, AlertStatus.NEW, 2, null, DateUtil.now());

        List<Alert> alerts = alertsDataService.findByExternalId("included_entity");

        assertResults(alerts, "externalId", "included_entity", "included_entity");
    }

    @Test
    public void shouldReturnAllAlertsWithGivenAlertTypeOnly() throws Exception {
        createAlert("entity1", AlertType.HIGH, AlertStatus.CLOSED, 3, null, DateUtil.now());
        createAlert("entity2", AlertType.LOW, AlertStatus.CLOSED, 8, null, DateUtil.now());
        createAlert("entity3", AlertType.LOW, AlertStatus.NEW, 2, null, DateUtil.now());

        List<Alert> alerts = alertsDataService.findByAlertType(AlertType.LOW);

        assertResults(alerts, "alertType", AlertType.LOW, AlertType.LOW);
    }

    @Test
    public void shouldReturnAllAlertsWithGivenAlertStatusOnly() throws Exception {
        createAlert("entity1", AlertType.HIGH, AlertStatus.CLOSED, 3, null, DateUtil.now());
        createAlert("entity2", AlertType.LOW, AlertStatus.CLOSED, 8, null, DateUtil.now());
        createAlert("entity3", AlertType.LOW, AlertStatus.NEW, 2, null, DateUtil.now());

        List<Alert> alerts = alertsDataService.findByStatus(AlertStatus.CLOSED);

        assertResults(alerts, "status", AlertStatus.CLOSED, AlertStatus.CLOSED);
    }

    @Test
    public void shouldReturnAllAlertsWithGivenPriorityOnly() throws Exception {
        createAlert("entity1", AlertType.HIGH, AlertStatus.CLOSED, 1, null, DateUtil.now());
        createAlert("entity2", AlertType.LOW, AlertStatus.CLOSED, 3, null, DateUtil.now());
        createAlert("entity3", AlertType.LOW, AlertStatus.NEW, 2, null, DateUtil.now());

        List<Alert> alerts = alertsDataService.findByPriority(1L);

        assertResults(alerts, "priority", 1L);
    }

    @Test
    public void shouldFilterAlertsBasedOnDateRange() {
        DateTime now = DateUtil.now();

        createAlert("111", AlertType.HIGH, AlertStatus.NEW, 2, null, now.minusDays(2));
        Alert alert2 = createAlert("112", AlertType.HIGH, AlertStatus.NEW, 2, null, now.minusDays(1));
        Alert alert3 = createAlert("113", AlertType.HIGH, AlertStatus.NEW, 2, null, now);
        Alert alert4 = createAlert("114", AlertType.HIGH, AlertStatus.NEW, 2, null, now.plusDays(1));
        createAlert("115", AlertType.HIGH, AlertStatus.NEW, 2, null, now.plusDays(2));

        List<Alert> listAlerts = alertsDataService.findByDateTime(new Range<>(now.minusDays(1).minusHours(1),
                now.plusDays(1).plusHours(1)));
        assertEquals(asList(alert2, alert3, alert4), listAlerts);
    }

    @Test
    public void verifyCount() {
        createAlert("111", AlertType.HIGH, AlertStatus.NEW, 2, null, null);
        createAlert("112", AlertType.HIGH, AlertStatus.READ, 2, null, null);
        createAlert("113", AlertType.HIGH, AlertStatus.READ, 2, null, null);

        assertEquals(1, alertsDataService.countFindByStatus(AlertStatus.NEW));
        assertEquals(2, alertsDataService.countFindByStatus(AlertStatus.READ));
        assertEquals(0, alertsDataService.countFindByStatus(AlertStatus.CLOSED));
    }

    @Test
    public void shouldNotChangeDateTimeWhenChangingTheStatus() {
        final Alert alert1 = createAlert("111", AlertType.HIGH, AlertStatus.NEW, 2, null, DateUtil.now());

        DateTime alert1DateTime = alert1.getDateTime();

        alertsDataService.doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                final Alert alert = alertsDataService.retrieve("id", alert1.getId());
                alert.setStatus(AlertStatus.CLOSED);
                alertsDataService.update(alert);
            }
        });

        assertEquals(alert1DateTime, alertsDataService.findById(alert1.getId()).getDateTime());
    }

    @Test
    public void shouldAllowLargeDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i=0 ; i<100 ; i++) {
            sb.append(String.format("%10d", i*10));
        }
        sb.append("]");
        String desc = sb.toString();
        assertTrue(desc.length() > 1000);
        Alert a = alertsDataService.create(new Alert("externalId123", "name", desc, AlertType.CRITICAL,
                AlertStatus.NEW, 0, null));

        List<Alert> alerts = alertsDataService.findByExternalId("externalId123");
        assertEquals(alerts.size(), 1);
        assertEquals(alerts.get(0).getDescription(), desc);
    }

    private Alert createAlert(String externalId, AlertType alertType, AlertStatus alertStatus, int priority, Map<String, String> data, DateTime dateTime) {
        Alert alert = new Alert(externalId, alertType, alertStatus, priority, data);
        alert.setDateTime(dateTime);

        return alertsDataService.create(alert);
    }
}
