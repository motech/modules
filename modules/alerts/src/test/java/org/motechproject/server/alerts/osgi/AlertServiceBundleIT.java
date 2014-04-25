package org.motechproject.server.alerts.osgi;

import org.junit.Test;
import org.motechproject.server.alerts.contract.AlertCriteria;
import org.motechproject.server.alerts.contract.UpdateCriteria;
import org.motechproject.server.alerts.domain.Alert;
import org.motechproject.server.alerts.domain.AlertStatus;
import org.motechproject.server.alerts.domain.AlertType;

import java.util.HashMap;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class AlertServiceBundleIT extends AlertsBaseIT {

    @Test
    public void shouldRegisterSuccessfully() {
        HashMap<String, String> alertData = new HashMap<String, String>();
        alertData.put("Status", "Open");
        alertData.put("Note", "This is an Alert!");
        String externalId = UUID.randomUUID().toString();
        alertService.create(externalId, null, "description", AlertType.CRITICAL, AlertStatus.NEW, 1, alertData);

        Alert alert = alertService.search(new AlertCriteria().byExternalId(externalId)).get(0);
        assertNotNull(alert);
        assertEquals(externalId, alert.getExternalId());
        assertEquals("Open", alert.getData().get("Status"));
        assertEquals("This is an Alert!", alert.getData().get("Note"));
    }

    @Test
    public void shouldUpdateAlert() {
        String externalId = UUID.randomUUID().toString();
        HashMap<String, String> alertData = new HashMap<String, String>();
        alertData.put("blah", "blah");
        alertService.create(externalId, "name", "description", AlertType.CRITICAL, AlertStatus.NEW, 1, alertData);
        Alert alert = alertService.search(new AlertCriteria().byExternalId(externalId)).get(0);

        HashMap<String, String> newData = new HashMap<String, String>();
        newData.put("newKey", "newValue");

        UpdateCriteria updateCriteria = new UpdateCriteria().status(AlertStatus.READ)
                .name("newName").description("newDescription")
                .priority(2).data(newData);
        alertService.update(alert.getId(), updateCriteria);

        Alert updatedAlert = alertService.search(new AlertCriteria().byExternalId(externalId)).get(0);
        assertEquals(AlertStatus.READ, updatedAlert.getStatus());
        assertEquals("newName", updatedAlert.getName());
        assertEquals("newDescription", updatedAlert.getDescription());
        assertEquals(2, updatedAlert.getPriority().intValue());
        assertEquals(2, updatedAlert.getData().size());
        assertEquals("blah", updatedAlert.getData().get("blah"));
        assertEquals("newValue", updatedAlert.getData().get("newKey"));
    }

}
