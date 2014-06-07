package org.motechproject.alerts.osgi;

import org.junit.Test;
import org.motechproject.alerts.contract.AlertCriteria;
import org.motechproject.alerts.domain.Alert;
import org.motechproject.alerts.domain.AlertStatus;
import org.motechproject.alerts.domain.AlertType;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlertsBundleIT extends AlertsBaseIT {

    @Test
    public void testAppointmentService() {
        final String externalId = "AlertsApiBundleIT-" + UUID.randomUUID();

        alertService.create(externalId, null, "Description", AlertType.CRITICAL, AlertStatus.NEW, 1, null);

        List<Alert> alerts = alertService.search(new AlertCriteria().byExternalId(externalId));

        assertTrue(alerts != null && alerts.size() == 1);
        assertEquals(externalId, alerts.get(0).getExternalId());
    }
}
