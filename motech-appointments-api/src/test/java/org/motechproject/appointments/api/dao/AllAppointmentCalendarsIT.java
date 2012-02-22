package org.motechproject.appointments.api.dao;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.appointments.api.model.Appointment;
import org.motechproject.appointments.api.model.AppointmentCalendar;
import org.motechproject.appointments.api.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class AllAppointmentCalendarsIT extends AppointmentsBaseIntegrationTest {

    @Autowired
    private AllAppointmentCalendars allAppointmentCalendars;

    @After
    public void tearDown() {
        markForDeletion(allAppointmentCalendars.getAll().toArray());
    }

    @Test
    public void testSaveAppointmentCalender() {
        AppointmentCalendar appointmentCalendar = new AppointmentCalendar().externalId("externalId");

        allAppointmentCalendars.saveAppointmentCalendar(appointmentCalendar);

        assertNotNull(appointmentCalendar.getId());

        allAppointmentCalendars.remove(appointmentCalendar);
    }

    @Test
    public void testFindByExternalId() {
        Visit visit1 = new Visit().name("Visit 1");
        Visit visit2 = new Visit().name("Visit 2");

        AppointmentCalendar appointmentCalendar = new AppointmentCalendar().externalId("foo").addVisit(visit1).addVisit(visit2);
        allAppointmentCalendars.saveAppointmentCalendar(appointmentCalendar);

        AppointmentCalendar savedCalender = allAppointmentCalendars.findByExternalId("foo");
        assertNotNull(savedCalender);
        assertEquals(appointmentCalendar.getId(), savedCalender.getId());

        allAppointmentCalendars.remove(savedCalender);
    }

    @Test
    public void findByAppointmentId() {
        Appointment appointment = new Appointment();
        Visit visit1 = new Visit().name("Visit 1").appointment(appointment);
        Visit visit2 = new Visit().name("Visit 2").appointment(new Appointment());

        AppointmentCalendar appointmentCalendar = new AppointmentCalendar().externalId("foo").addVisit(visit1).addVisit(visit2);
        allAppointmentCalendars.saveAppointmentCalendar(appointmentCalendar);

        assertEquals(appointment.id(), allAppointmentCalendars.findAppointmentById(appointment.id()).id());
    }
}
