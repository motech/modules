package org.motechproject.openmrs.tasks;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.openmrs.config.Configs;
import org.motechproject.openmrs.domain.*;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSEncounterService;
import org.motechproject.openmrs.service.OpenMRSGeneratedIdentifierService;
import org.motechproject.openmrs.service.OpenMRSObservationService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.motechproject.openmrs.service.OpenMRSProgramEnrollmentService;
import org.motechproject.openmrs.service.OpenMRSProviderService;
import org.motechproject.openmrs.service.OpenMRSRelationshipService;
import org.motechproject.openmrs.tasks.builder.OpenMRSTaskDataProviderBuilder;
import org.osgi.framework.BundleContext;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.motechproject.openmrs.tasks.OpenMRSTasksConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSTaskDataProviderTest {

    private static final String CONFIG_NAME = "configName";
    private static final String PROGRAM_DEFAULT_NAME = "program";
    private static final String PROGRAM_ANOTHER_NAME = "anotherName";
    private static final String DEFAULT_UUID = "495b10c4-56bd-11df-a35e-0027136865c4";
    private static final String DEFAULT_MOTECH_ID = "3";
    private static final String DEFAULT_IDENTIFIER_ID = "4";
    private static final String DEFAULT_IDENTIFIER_NAME = "identifierName";
    private static final String ATTRIBUTE_UUID = "51f41ccf-dca8-48e3-bcf3-5e0981948b1e";
    private static final String ATTRIBUTE_VALUE = "attributeValue";
    private static final String ATTRIBUTE_TYPE_UUID = "2c41f832-f3ed-47f1-92e2-53143ee71626";
    private static final String ATTRIBUTE_TYPE_DISPLAY = "displayName";

    @Mock
    private OpenMRSEncounterService encounterService;

    @Mock
    private OpenMRSObservationService observationService;

    @Mock
    private OpenMRSPatientService patientService;

    @Mock
    private OpenMRSProviderService providerService;

    @Mock
    private OpenMRSConfigService configService;

    @Mock
    private OpenMRSRelationshipService relationshipService;

    @Mock
    private OpenMRSProgramEnrollmentService programEnrollmentService;

    @Mock
    private OpenMRSGeneratedIdentifierService identifierService;

    @Mock
    private BundleContext bundleContext;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private OpenMRSTaskDataProviderBuilder taskDataProviderBuilder = new OpenMRSTaskDataProviderBuilder();

    private OpenMRSTaskDataProvider taskDataProvider;

    @Before
    public void setUp() {
        when(configService.getConfigs()).thenReturn(new Configs());
        taskDataProvider = new OpenMRSTaskDataProvider(taskDataProviderBuilder, encounterService, observationService, patientService,
                providerService, relationshipService, programEnrollmentService, identifierService, bundleContext);
    }

    @Test
    public void shouldReturnNullWhenClassIsNotSupported() {
        String className = "testClass";

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, "lookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForEncounter() {
        String className = Encounter.class.getSimpleName();

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(encounterService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();

        when(encounterService.getEncounterByUuid(CONFIG_NAME, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className+ '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertNull(object);
        verify(encounterService).getEncounterByUuid(CONFIG_NAME, null);
    }

    @Test
    public void shouldReturnNullWhenEncounterNotFoundForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);

        when(encounterService.getEncounterByUuid(CONFIG_NAME, DEFAULT_UUID)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertNull(object);
        verify(encounterService).getEncounterByUuid(CONFIG_NAME, DEFAULT_UUID);
    }

    @Test
    public void shouldReturnEncounterForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);

        Encounter encounter = new Encounter();
        encounter.setEncounterType(new EncounterType("encounterTypeTest", null));

        when(encounterService.getEncounterByUuid(CONFIG_NAME, DEFAULT_UUID)).thenReturn(encounter);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertEquals(encounter, object);
        verify(encounterService).getEncounterByUuid(CONFIG_NAME, DEFAULT_UUID);
    }

    @Test
    public void shouldReturnObservationForPatientUuidAndConceptUuid() {
        String className = Observation.class.getSimpleName();
        String conceptUuid = "sampleConceptUuid";

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_UUID, DEFAULT_UUID);
        lookupFields.put(CONCEPT_UUID, conceptUuid);

        Observation observation = new Observation();
        observation.setUuid("10");

        when(observationService.getLatestObservationByPatientUUIDAndConceptUUID(CONFIG_NAME, DEFAULT_UUID, conceptUuid)).thenReturn(observation);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_PATIENT_UUID_AND_CONCEPT_UUID, lookupFields);

        assertEquals(observation, object);

        verify(observationService).getLatestObservationByPatientUUIDAndConceptUUID(CONFIG_NAME, DEFAULT_UUID, conceptUuid);
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForPatient() {
        String className = Patient.class.getSimpleName();

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(patientService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();

        when(patientService.getPatientByUuid(CONFIG_NAME, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByUuid(CONFIG_NAME, null);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();

        when(patientService.getPatientByMotechId(CONFIG_NAME, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByMotechId(CONFIG_NAME, null);
    }

    @Test
    public void shouldReturnNullWhenPatientNotFoundForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);

        when(patientService.getPatientByUuid(CONFIG_NAME, DEFAULT_UUID)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByUuid(CONFIG_NAME, DEFAULT_UUID);
    }

    @Test
    public void shouldReturnNullWhenPatientNotFoundForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, DEFAULT_MOTECH_ID);

        when(patientService.getPatientByMotechId(CONFIG_NAME, DEFAULT_MOTECH_ID)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByMotechId(CONFIG_NAME, DEFAULT_MOTECH_ID);
    }

    @Test
    public void shouldReturnPatientForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);

        Patient patient = new Patient();
        patient.setMotechId("10");

        when(patientService.getPatientByUuid(CONFIG_NAME, DEFAULT_UUID)).thenReturn(patient);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertEquals(patient, object);
        verify(patientService).getPatientByUuid(CONFIG_NAME, DEFAULT_UUID);
    }

    @Test
    public void shouldReturnPatientForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, DEFAULT_MOTECH_ID);

        Patient patient = new Patient();
        patient.setUuid("10");

        when(patientService.getPatientByMotechId(CONFIG_NAME, DEFAULT_MOTECH_ID)).thenReturn(patient);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID, lookupFields);

        assertEquals(patient, object);
        verify(patientService).getPatientByMotechId(CONFIG_NAME, DEFAULT_MOTECH_ID);
    }

    @Test
    public void shouldReturnPatientForLookupGetPatientByOtherId() {
        String className = Patient.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(IDENTIFIER_ID, DEFAULT_IDENTIFIER_ID);
        lookupFields.put(IDENTIFIER_NAME, DEFAULT_IDENTIFIER_NAME);

        Patient patient = new Patient();
        patient.setUuid("10");

        when(patientService.getPatientByIdentifier(CONFIG_NAME, DEFAULT_IDENTIFIER_ID, DEFAULT_IDENTIFIER_NAME)).thenReturn(patient);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, DEFAULT_IDENTIFIER_NAME, lookupFields);

        assertEquals(patient, object);
        verify(patientService).getPatientByIdentifier(CONFIG_NAME, DEFAULT_IDENTIFIER_ID, DEFAULT_IDENTIFIER_NAME);
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForProvider() {
        String className = Provider.class.getSimpleName();

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();

        when(providerService.getProviderByUuid(CONFIG_NAME, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid(CONFIG_NAME, null);
    }

    @Test
    public void shouldReturnNullWhenProviderNotFoundForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);

        when(providerService.getProviderByUuid(CONFIG_NAME, DEFAULT_UUID)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid(CONFIG_NAME, DEFAULT_UUID);
    }

    @Test
    public void shouldReturnProviderForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);

        Provider provider = new Provider();
        provider.setIdentifier("testIdentifier");

        when(providerService.getProviderByUuid(CONFIG_NAME, DEFAULT_UUID)).thenReturn(provider);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertEquals(provider, object);
        verify(providerService).getProviderByUuid(CONFIG_NAME, DEFAULT_UUID);
    }

    @Test
    public void shouldReturnRelationshipForLookupGetRelationshipByTypeUuidAndPersonBUuid() {
        String relationshipTypeUuid = "relationshipTypeUuid";
        String personUuid = "personUuid";
        String objectType = "Relationship-" + CONFIG_NAME;

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(RELATIONSHIP_TYPE_UUID, relationshipTypeUuid);
        lookupFields.put(PERSON_UUID, personUuid);

        List<Relationship> expected = prepareRelationship();

        when(relationshipService.getByTypeUuidAndPersonUuid(eq(CONFIG_NAME), eq(relationshipTypeUuid), eq(personUuid))).thenReturn(expected);

        Object object = taskDataProvider.lookup(objectType, BY_PERSON_UUID, lookupFields);

        verify(relationshipService).getByTypeUuidAndPersonUuid(eq(CONFIG_NAME), eq(relationshipTypeUuid), eq(personUuid));

        assertEquals(expected.get(0), object);
    }

    @Test
    public void shouldReturnEmptyListWhenWrongLookupNameForProgramEnrollment() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_UUID, DEFAULT_UUID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, "wrongLookupName", lookupFields);

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertTrue(actual.getResults().isEmpty());
        assertEquals(0, actual.getNumberOfPrograms());

        verifyZeroInteractions(programEnrollmentService);
    }

    @Test
    public void shouldReturnProgramEnrollmentForPatientUuidAndProgramName() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_UUID, DEFAULT_UUID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);


        List<ProgramEnrollment> expected = prepareProgramEnrollments(false);
        when(programEnrollmentService.getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(expected);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID_AMD_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertEquals(expected.get(0), actual.getResults().get(0));

        //Get state without endDate - this is what getCurrentState() should return
        assertEquals(expected.get(0).getStates().get(1), actual.getResults().get(0).getCurrentState());
    }

    @Test
    public void shouldReturnOnlyActiveProgramEnrollmentForPatientUuidAndProgramName() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_UUID, DEFAULT_UUID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);
        lookupFields.put(ACTIVE_PROGRAM, "true");


        List<ProgramEnrollment> expected = prepareProgramEnrollments(false);
        when(programEnrollmentService.getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(expected);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID_AMD_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertEquals(expected.get(0), actual.getResults().get(0));
        assertEquals(1, actual.getNumberOfPrograms());
    }

    @Test
    public void shouldReturnProgramEnrollmentForPatientMotechIdAndProgramName() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_MOTECH_ID, DEFAULT_MOTECH_ID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);

        List<ProgramEnrollment> expected = prepareProgramEnrollments(false);
        when(programEnrollmentService.getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID)))
                .thenReturn(expected);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID_AND_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertEquals(expected.get(0), actual.getResults().get(0));

        //Get state without endDate - this is what getCurrentState() should return
        assertEquals(expected.get(0).getStates().get(1), actual.getResults().get(0).getCurrentState());
    }

    @Test
    public void shouldReturnEmptyListWhenProgramEnrollmentNotFoundForLookupByUuidAndProgramName() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_UUID, DEFAULT_UUID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_ANOTHER_NAME);

        when(programEnrollmentService.getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(prepareProgramEnrollments(false));

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID_AMD_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertTrue(actual.getResults().isEmpty());
        assertEquals(0, actual.getNumberOfPrograms());
    }

    @Test
    public void shouldReturnEmptyListWhenProgramEnrollmentNotFoundForLookupByMotechIdAndProgramName() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_MOTECH_ID, DEFAULT_MOTECH_ID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_ANOTHER_NAME);

        when(programEnrollmentService.getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID)))
                .thenReturn(prepareProgramEnrollments(false));

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID_AND_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertTrue(actual.getResults().isEmpty());
        assertEquals(0, actual.getNumberOfPrograms());
    }

    @Test
    public void shouldReturnOnlyActiveBahmniProgramEnrollmentForPatientMotechIdAndProgramName() {
        String className = BAHMNI_PROGRAM_ENROLLMENT;

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_MOTECH_ID, DEFAULT_MOTECH_ID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);
        lookupFields.put(ACTIVE_PROGRAM, "true");

        List<ProgramEnrollment> expected = prepareProgramEnrollments(true);
        when(programEnrollmentService.getBahmniProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID)))
                .thenReturn(prepareProgramEnrollments(true));

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID_AND_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getBahmniProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertEquals(expected.get(0), actual.getResults().get(0));
        assertEquals(1, actual.getNumberOfPrograms());
    }

    @Test
    public void shouldReturnAllBahmniProgramEnrollmentForPatientMotechIdAndProgramName() {
        String className = BAHMNI_PROGRAM_ENROLLMENT;

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_MOTECH_ID, DEFAULT_MOTECH_ID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);
        lookupFields.put(ACTIVE_PROGRAM, "false");

        List<ProgramEnrollment> expected = prepareProgramEnrollments(true);
        when(programEnrollmentService.getBahmniProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID)))
                .thenReturn(prepareProgramEnrollments(true));

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID_AND_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getBahmniProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertEquals(expected.get(0), actual.getResults().get(0));
    }

    @Test
    public void shouldReturnEmptyListWhenBahmniProgramEnrollmentNotFoundForPatientMotechIdAndProgramName() {
        String className = BAHMNI_PROGRAM_ENROLLMENT;

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_MOTECH_ID, DEFAULT_MOTECH_ID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_ANOTHER_NAME);
        lookupFields.put(ACTIVE_PROGRAM, "false");

        List<ProgramEnrollment> expected = prepareProgramEnrollments(true);
        when(programEnrollmentService.getBahmniProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID)))
                .thenReturn(prepareProgramEnrollments(true));

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID_AND_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getBahmniProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertTrue(actual.getResults().isEmpty());
        assertEquals(0, actual.getNumberOfPrograms());
    }

    @Test
    public void shouldReturnOnlyActiveBahmniProgramEnrollmentForPatientUuidAndProgramName() {
        String className = BAHMNI_PROGRAM_ENROLLMENT;

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_UUID, DEFAULT_UUID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);
        lookupFields.put(ACTIVE_PROGRAM, "true");

        List<ProgramEnrollment> expected = prepareProgramEnrollments(true);
        when(programEnrollmentService.getBahmniProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(prepareProgramEnrollments(true));

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID_AMD_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getBahmniProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertEquals(expected.get(0), actual.getResults().get(0));
        assertEquals(1, actual.getNumberOfPrograms());
    }

    @Test
    public void shouldReturnAllBahmniProgramEnrollmentForPatientUuidAndProgramName() {
        String className = BAHMNI_PROGRAM_ENROLLMENT;

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_UUID, DEFAULT_UUID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);
        lookupFields.put(ACTIVE_PROGRAM, "false");

        List<ProgramEnrollment> expected = prepareProgramEnrollments(true);
        when(programEnrollmentService.getBahmniProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(prepareProgramEnrollments(true));

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID_AMD_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getBahmniProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertEquals(expected.get(0), actual.getResults().get(0));
    }

    @Test
    public void shouldReturnEmptyListWhenBahmniProgramEnrollmentNotFoundForPatientUuidAndProgramName() {
        String className = BAHMNI_PROGRAM_ENROLLMENT;

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(PATIENT_UUID, DEFAULT_UUID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_ANOTHER_NAME);
        lookupFields.put(ACTIVE_PROGRAM, "false");

        List<ProgramEnrollment> expected = prepareProgramEnrollments(true);
        when(programEnrollmentService.getBahmniProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(prepareProgramEnrollments(true));

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID_AMD_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getBahmniProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertTrue(object instanceof ProgramEnrollmentListResult);

        ProgramEnrollmentListResult actual = (ProgramEnrollmentListResult) object;

        assertTrue(actual.getResults().isEmpty());
        assertEquals(0, actual.getNumberOfPrograms());
    }

    private List<Relationship> prepareRelationship() {
        Relationship relationship = new Relationship();
        relationship.setUuid("relationShipUuid");
        relationship.setEndDate("endDate");
        relationship.setStartDate("startDate");

        Person personA = new Person();
        personA.setUuid("personAUuid");

        Person personB = new Person();
        personB.setUuid("personBUuid");

        RelationshipType type = new RelationshipType();
        type.setUuid("relationshipTypeUuid");

        relationship.setPersonA(personA);
        relationship.setPersonB(personB);
        relationship.setRelationshipType(type);

        return Collections.singletonList(relationship);
    }

    private List<ProgramEnrollment> prepareProgramEnrollments(boolean forBahmni) {
        List<ProgramEnrollment> result = new ArrayList<>();

        Program program = new Program();
        program.setName(PROGRAM_DEFAULT_NAME);

        DateTime startDate = new DateTime("2000-08-16T07:22:05Z");
        DateTime endDate = new DateTime("2010-08-16T07:22:05Z");

        ProgramEnrollment.StateStatus firstState = new ProgramEnrollment.StateStatus();
        firstState.setStartDate(startDate.toDate());
        firstState.setEndDate(endDate.toDate());

        ProgramEnrollment.StateStatus lastState = new ProgramEnrollment.StateStatus();
        lastState.setStartDate(endDate.toDate());

        ProgramEnrollment programEnrollmentActive = new ProgramEnrollment();
        programEnrollmentActive.setProgram(program);
        programEnrollmentActive.setStates(Arrays.asList(firstState, lastState));

        ProgramEnrollment programEnrollmentNotActive = new ProgramEnrollment();
        programEnrollmentNotActive.setProgram(program);
        programEnrollmentNotActive.setDateCompleted(endDate.toDate());

        if (forBahmni) {
            Attribute.AttributeType attributeType = new Attribute.AttributeType();
            attributeType.setUuid(ATTRIBUTE_TYPE_UUID);
            attributeType.setDisplay(ATTRIBUTE_TYPE_DISPLAY);

            Attribute attribute = new Attribute();
            attribute.setUuid(ATTRIBUTE_UUID);
            attribute.setValue(ATTRIBUTE_VALUE);
            attribute.setAttributeType(attributeType);
            attribute.setDisplay(attribute.getAttributeType().getDisplay() + ": " + attribute.getValue());

            List<Attribute> attributes = new ArrayList<>();
            attributes.add(attribute);

            programEnrollmentActive.setAttributes(attributes);
            programEnrollmentNotActive.setAttributes(attributes);
        }

        result.add(programEnrollmentActive);
        result.add(programEnrollmentNotActive);

        return result;
    }
}
