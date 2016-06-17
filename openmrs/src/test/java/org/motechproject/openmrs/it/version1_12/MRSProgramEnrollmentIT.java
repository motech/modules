package org.motechproject.openmrs.it.version1_12;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.domain.Program;
import org.motechproject.openmrs.domain.ProgramEnrollment;
import org.motechproject.openmrs.exception.PatientNotFoundException;
import org.motechproject.openmrs.service.OpenMRSLocationService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.motechproject.openmrs.service.OpenMRSProgramEnrollmentService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.motechproject.openmrs.tasks.OpenMRSActionProxyService.DEFAULT_LOCATION_NAME;
import static org.motechproject.openmrs.util.TestConstants.DEFAULT_CONFIG_NAME;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSProgramEnrollmentIT extends BasePaxIT {

    @Inject
    private OpenMRSProgramEnrollmentService programEnrollmentAdapter;

    @Inject
    private OpenMRSPatientService patientAdapter;

    @Inject
    private OpenMRSLocationService locationAdapter;

    private Patient createdPatient;
    private ProgramEnrollment programEnrollment;
    private ProgramEnrollment createdProgramEnrollment;

    @Before
    public void initialize() {
        Patient patient = preparePatient();

        createdPatient = patientAdapter.createPatient(DEFAULT_CONFIG_NAME, patient);


        //TODO: Add default Program to the test platform or create Service for Program
        Program program = new Program();
        program.setUuid("187af646-373b-4459-8114-4724d7e07fd5");

        DateTime dateEnrolled = new DateTime("2010-01-16T00:00:00Z");
        DateTime dateCompleted = new DateTime("2016-01-16T00:00:00Z");
        DateTime stateStartDate = new DateTime("2011-01-16T00:00:00Z");

        Location location = locationAdapter.getLocations(DEFAULT_CONFIG_NAME, DEFAULT_LOCATION_NAME).get(0);

        Program.State state = new Program.State();
        state.setUuid("6ac1bb86-f7ef-438c-8ea6-33050caa350d");

        ProgramEnrollment.StateStatus stateStatus = new ProgramEnrollment.StateStatus();
        stateStatus.setState(state);
        stateStatus.setStartDate(stateStartDate.toDate());

        programEnrollment = new ProgramEnrollment();
        programEnrollment.setProgram(program);
        programEnrollment.setPatient(createdPatient);
        programEnrollment.setDateEnrolled(dateEnrolled.toDate());
        programEnrollment.setDateCompleted(dateCompleted.toDate());
        programEnrollment.setLocation(location);
        programEnrollment.setStates(Collections.singletonList(stateStatus));

        createdProgramEnrollment = programEnrollmentAdapter.createProgramEnrollment(DEFAULT_CONFIG_NAME, programEnrollment);
    }

    @Test
    public void shouldCreateProgramEnrollment() {
        assertNotNull(createdProgramEnrollment.getUuid());

        programEnrollment.setUuid(createdProgramEnrollment.getUuid());

        compareProgramEnrollments(programEnrollment, createdProgramEnrollment);
    }

    @Test
    public void shouldUpdateProgramEnrollment() {
        DateTime newDateEnrolled = new DateTime("2012-01-16T00:00:00Z");
        DateTime newDateCompleted = new DateTime("2015-01-16T00:00:00Z");
        DateTime newStateStartDate = new DateTime("2013-01-01T00:00:00Z");

        Program.State newState = new Program.State();
        newState.setUuid("4b812ac8-421c-470f-b4b7-88187cdbd2a5");

        ProgramEnrollment.StateStatus newStateStatus = new ProgramEnrollment.StateStatus();
        newStateStatus.setState(newState);
        newStateStatus.setStartDate(newStateStartDate.toDate());

        List<ProgramEnrollment.StateStatus> statuses = new ArrayList<>();
        statuses.add(newStateStatus);

        ProgramEnrollment programEnrollmentToUpdate = new ProgramEnrollment();
        programEnrollmentToUpdate.setUuid(createdProgramEnrollment.getUuid());
        programEnrollmentToUpdate.setDateEnrolled(newDateEnrolled.toDate());
        programEnrollmentToUpdate.setDateCompleted(newDateCompleted.toDate());
        programEnrollmentToUpdate.setStates(statuses);

        ProgramEnrollment updated = programEnrollmentAdapter.updateProgramEnrollment(DEFAULT_CONFIG_NAME, programEnrollmentToUpdate);

        createdProgramEnrollment.setDateEnrolled(newDateEnrolled.toDate());
        createdProgramEnrollment.setDateCompleted(newDateCompleted.toDate());
        createdProgramEnrollment.getStates().addAll(statuses);

        compareProgramEnrollments(createdProgramEnrollment, updated);
    }

    @Test
    public void shouldGetProgramEnrollmentByPatientUuid() {
        List<ProgramEnrollment> fetched = programEnrollmentAdapter.getProgramEnrollmentByPatientUuid(DEFAULT_CONFIG_NAME, createdPatient.getUuid());

        compareProgramEnrollments(createdProgramEnrollment, fetched.get(0));
    }

    @Test
    public void shouldGetProgramEnrollmentByPatientMotechId() {
        Patient patient = patientAdapter.getPatientByUuid(DEFAULT_CONFIG_NAME, createdPatient.getUuid());
        List<ProgramEnrollment> fetched = programEnrollmentAdapter.getProgramEnrollmentByPatientMotechId(DEFAULT_CONFIG_NAME, patient.getMotechId());

        compareProgramEnrollments(createdProgramEnrollment, fetched.get(0));
    }

    @After
    public void tearDown() throws PatientNotFoundException {
        programEnrollmentAdapter.deleteProgramEnrollment(DEFAULT_CONFIG_NAME, createdProgramEnrollment.getUuid());
        patientAdapter.deletePatient(DEFAULT_CONFIG_NAME, createdPatient.getUuid());
    }

    private Patient preparePatient() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("John");
        name.setFamilyName("Smith");
        person.setNames(Collections.singletonList(name));

        Person.Address address = new Person.Address();
        address.setAddress1("10 Fifth Avenue");
        person.setAddresses(Collections.singletonList(address));

        person.setBirthdateEstimated(false);
        person.setGender("M");

        Location location = locationAdapter.getLocations(DEFAULT_CONFIG_NAME, DEFAULT_LOCATION_NAME).get(0);

        assertNotNull(location);

        return new Patient(person, "602", location);
    }

    private void compareProgramEnrollments(ProgramEnrollment expected, ProgramEnrollment actual) {
        assertEquals(expected.getUuid(), actual.getUuid());
        assertEquals(expected.getPatient().getUuid(), actual.getPatient().getUuid());
        assertEquals(expected.getProgram().getUuid(), actual.getProgram().getUuid());
        assertEquals(expected.getDateEnrolled(), actual.getDateEnrolled());
        assertEquals(expected.getDateCompleted(), actual.getDateCompleted());
        assertEquals(expected.getLocation().getUuid(), actual.getLocation().getUuid());

        checkStatesEquality(expected.getStates(), actual.getStates());
    }

    private void checkStatesEquality(List<ProgramEnrollment.StateStatus> expected, List<ProgramEnrollment.StateStatus> actual) {
        assertEquals(expected.size(), actual.size());

        Comparator<ProgramEnrollment.StateStatus> comparator = new Comparator<ProgramEnrollment.StateStatus>() {
            @Override
            public int compare(ProgramEnrollment.StateStatus o1, ProgramEnrollment.StateStatus o2) {
                int o1Hash = Objects.hash(o1.getState().getUuid(), o1.getStartDate());
                int o2Hash = Objects.hash(o2.getState().getUuid(), o2.getStartDate());

                return o2Hash - o1Hash;
            }
        };

        expected.sort(comparator);
        actual.sort(comparator);

        for (ProgramEnrollment.StateStatus expectedState : expected) {
            assertTrue(0 <= Collections.binarySearch(actual, expectedState, comparator));
        }
    }
}

