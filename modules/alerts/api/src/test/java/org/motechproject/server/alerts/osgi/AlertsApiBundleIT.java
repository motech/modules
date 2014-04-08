package org.motechproject.server.alerts.osgi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.server.alerts.contract.AlertCriteria;
import org.motechproject.server.alerts.contract.AlertService;
import org.motechproject.server.alerts.domain.Alert;
import org.motechproject.server.alerts.domain.AlertStatus;
import org.motechproject.server.alerts.domain.AlertType;
import org.motechproject.testing.osgi.BasePaxIT;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class AlertsApiBundleIT extends BasePaxIT {

    @Inject
    private AlertService alertService;

    @Test
    public void testAppointmentService() {
        final String externalId = "AlertsApiBundleIT-" + UUID.randomUUID();

        alertService.create(externalId, null, "Description", AlertType.CRITICAL, AlertStatus.NEW, 1, null);

        List<Alert> alerts = alertService.search(new AlertCriteria().byExternalId(externalId));

        assertTrue(alerts != null && alerts.size() == 1);
        assertEquals(externalId, alerts.get(0).getExternalId());
        // Delete the doc in the post-integration phase
    }
}
