package org.motechproject.openmrs19.tasks;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.tasks.impl.OpenMRSActionProxyServiceImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSActionProxyServiceTest {

    @Mock
    private OpenMRSFacilityService facilityService;

    @Mock
    private OpenMRSPatientService patientService;

    @Captor
    private ArgumentCaptor<OpenMRSPatient> patientCaptor;

    @InjectMocks
    private OpenMRSActionProxyServiceImpl openMRSActionProxyService = new OpenMRSActionProxyServiceImpl();

    @Test
    public void shouldCreatePatientWithGivenParameters() {
        OpenMRSPerson person = createTestPerson();

        OpenMRSFacility facility = new OpenMRSFacility();
        facility.setName("testLocation");

        Map<String, String> identifiers = new HashMap<>();
        identifiers.put("CommCare CaseID", "1000");

        OpenMRSPatient patient = new OpenMRSPatient("500", person, facility, identifiers);

        doReturn(Collections.singletonList(facility)).when(facilityService).getFacilities(facility.getName());

        openMRSActionProxyService.createPatient(person.getFirstName(), person.getMiddleName(), person.getLastName(),
                person.getAddress(), person.getDateOfBirth(), person.getBirthDateEstimated(), person.getGender(),
                person.getDead(), patient.getMotechId(), facility.getName(), identifiers);

        verify(patientService).createPatient(patientCaptor.capture());

        assertEquals(patient, patientCaptor.getValue());
    }

    @Test
    public void shouldCreatePatientWithDefaultLocationWhenLocationNameIsNotProvided() {
        OpenMRSPerson person = createTestPerson();

        OpenMRSFacility facility = new OpenMRSFacility();
        facility.setName(OpenMRSActionProxyServiceImpl.DEFAULT_LOCATION_NAME);

        Map<String, String> identifiers = new HashMap<>();
        identifiers.put("CommCare CaseID", "1000");

        OpenMRSPatient patient = new OpenMRSPatient("500", person, facility, identifiers);

        doReturn(Collections.singletonList(facility)).when(facilityService).getFacilities(OpenMRSActionProxyServiceImpl.DEFAULT_LOCATION_NAME);

        openMRSActionProxyService.createPatient(person.getFirstName(), person.getMiddleName(), person.getLastName(),
                person.getAddress(), person.getDateOfBirth(), person.getBirthDateEstimated(), person.getGender(),
                person.getDead(), patient.getMotechId(), "", identifiers);

        verify(patientService).createPatient(patientCaptor.capture());

        assertEquals(patient, patientCaptor.getValue());
    }

    @Test
    public void shouldNotUsedDefaultLocationWhenLocationForGivenNameIsNotFound() {
        OpenMRSPerson person = createTestPerson();

        OpenMRSFacility facility = new OpenMRSFacility();
        facility.setName("testLocationNameForNotExistingLocation");

        Map<String, String> identifiers = new HashMap<>();
        identifiers.put("CommCare CaseID", "1000");

        OpenMRSPatient patient = new OpenMRSPatient("500", person, null, identifiers);

        doReturn(Collections.emptyList()).when(facilityService).getFacilities(facility.getName());

        openMRSActionProxyService.createPatient(person.getFirstName(), person.getMiddleName(), person.getLastName(),
                person.getAddress(), person.getDateOfBirth(), person.getBirthDateEstimated(), person.getGender(),
                person.getDead(), patient.getMotechId(), facility.getName(), identifiers);

        verify(patientService).createPatient(patientCaptor.capture());

        // the expected patient object has location value set to null, the actual object should be the same
        assertEquals(patient, patientCaptor.getValue());
    }

    private OpenMRSPerson createTestPerson() {
        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName("John");
        person.setMiddleName("Robert");
        person.setLastName("Smith");
        person.setGender("M");
        person.setAddress("Gdynia");
        person.setDateOfBirth(new DateTime("2000-08-16T07:22:05Z"));
        person.setBirthDateEstimated(true);
        person.setDead(false);

        return person;
    }
}
