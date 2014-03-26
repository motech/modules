package org.motechproject.appointments.api.osgi;

import org.motechproject.appointments.api.service.AppointmentService;
import org.motechproject.appointments.api.service.contract.CreateVisitRequest;
import org.motechproject.appointments.api.service.contract.VisitResponse;
import org.motechproject.testing.osgi.BaseOsgiIT;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;

public class AppointmentsApiBundleIT extends BaseOsgiIT {

    public void testAppointmentService() {
        AppointmentService appointmentService = getService(AppointmentService.class);

        final String externalId = "AppointmentsApiBundleIT-" + UUID.randomUUID();
        String visitName = "Visit-" + externalId;
        CreateVisitRequest request = new CreateVisitRequest().setVisitName(visitName);
        appointmentService.addVisit(externalId, request);
        VisitResponse response = appointmentService.findVisit(externalId, visitName);
        assertNotNull(response);
        assertEquals(visitName, response.getName());
        // Delete the doc in the post-integration phase
    }

    @Override
    protected List<String> getImports() {
        return asList(
                "org.motechproject.appointments.api.service.contract",
                "org.motechproject.appointments.api.service"
        );
    }
}
