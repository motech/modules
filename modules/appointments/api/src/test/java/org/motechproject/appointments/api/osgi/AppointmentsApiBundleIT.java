package org.motechproject.appointments.api.osgi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.appointments.api.service.AppointmentService;
import org.motechproject.appointments.api.service.contract.CreateVisitRequest;
import org.motechproject.appointments.api.service.contract.VisitResponse;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class AppointmentsApiBundleIT extends BasePaxIT {

    @Inject
    private AppointmentService appointmentService;

    @Override
    protected boolean shouldFakeModuleStartupEvent() {
        return true;
    }

    @Test
    public void testAppointmentService() {
        final String externalId = "AppointmentsApiBundleIT-" + UUID.randomUUID();
        String visitName = "Visit-" + externalId;
        CreateVisitRequest request = new CreateVisitRequest().setVisitName(visitName);
        appointmentService.addVisit(externalId, request);
        VisitResponse response = appointmentService.findVisit(externalId, visitName);
        assertNotNull(response);
        assertEquals(visitName, response.getName());
        // Delete the doc in the post-integration phase
    }
}
