package org.motechproject.openmrs19.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.openmrs19.config.Configs;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.domain.Program;
import org.motechproject.openmrs19.domain.ProgramEnrollment;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.domain.Relationship;
import org.motechproject.openmrs19.domain.RelationshipType;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSProgramEnrollmentService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.service.OpenMRSRelationshipService;
import org.motechproject.openmrs19.tasks.builder.OpenMRSTaskDataProviderBuilder;
import org.osgi.framework.BundleContext;
import org.springframework.core.io.ResourceLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_MOTECH_ID_AND_PROGRAM_NAME;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_PERSON_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_UUID_AMD_PROGRAM_NAME;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.PERSON_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.PROGRAM_NAME;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.RELATIONSHIP_TYPE_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.UUID;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSTaskDataProviderTest {

    private static final String CONFIG_NAME = "configName";
    private static final String PROGRAM_DEFAULT_NAME = "program";
    private static final String PROGRAM_ANOTHER_NAME = "anotherName";
    private static final String DEFAULT_UUID = "495b10c4-56bd-11df-a35e-0027136865c4";
    private static final String DEFAULT_MOTECH_ID = "3";

    @Mock
    private OpenMRSEncounterService encounterService;

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
    private BundleContext bundleContext;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private OpenMRSTaskDataProviderBuilder taskDataProviderBuilder = new OpenMRSTaskDataProviderBuilder();

    private OpenMRSTaskDataProvider taskDataProvider;

    @Before
    public void setUp() {
        when(configService.getConfigs()).thenReturn(new Configs());
        taskDataProvider = new OpenMRSTaskDataProvider(taskDataProviderBuilder, encounterService, patientService,
                providerService, relationshipService, programEnrollmentService, bundleContext);
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
        encounter.setEncounterType(new EncounterType("encounterTypeTest"));

        when(encounterService.getEncounterByUuid(CONFIG_NAME, DEFAULT_UUID)).thenReturn(encounter);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        assertEquals(encounter, object);
        verify(encounterService).getEncounterByUuid(CONFIG_NAME, DEFAULT_UUID);
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
    public void shouldReturnNullWhenWrongLookupNameForProgramEnrollment() {
        String className = ProgramEnrollment.class.getSimpleName();

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(programEnrollmentService);
    }

    @Test
    public void shouldReturnProgramEnrollmentForPatientUuid() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);

        List<ProgramEnrollment> expected = prepareProgramEnrollments();
        when(programEnrollmentService.getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(expected);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertEquals(expected.get(0), object);
    }

    @Test
    public void shouldReturnProgramEnrollmentForPatientMotechId() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, DEFAULT_MOTECH_ID);

        List<ProgramEnrollment> expected = prepareProgramEnrollments();
        when(programEnrollmentService.getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID)))
                .thenReturn(expected);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID));

        assertEquals(expected.get(0), object);
    }

    @Test
    public void shouldReturnProgramEnrollmentForPatientUuidAndProgramName() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);

        List<ProgramEnrollment> expected = prepareProgramEnrollments();
        when(programEnrollmentService.getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(expected);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID_AMD_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertEquals(expected.get(0), object);
    }

    @Test
    public void shouldReturnProgramEnrollmentForPatientMotechIdAndProgramName() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, DEFAULT_MOTECH_ID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_DEFAULT_NAME);

        List<ProgramEnrollment> expected = prepareProgramEnrollments();
        when(programEnrollmentService.getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID)))
                .thenReturn(expected);

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID_AND_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID));

        assertEquals(expected.get(0), object);
    }

    @Test
    public void shouldReturnNullWhenProgramEnrollmentNotFonundForLookupByUuid() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);

        when(programEnrollmentService.getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(Collections.emptyList());

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertNull(object);
    }

    @Test
    public void shouldReturnNullWhenProgramEnrollmentNotFoundForLookupByMotechId() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, DEFAULT_MOTECH_ID);

        when(programEnrollmentService.getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID)))
                .thenReturn(Collections.emptyList());

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID));

        assertNull(object);
    }

    @Test
    public void shouldReturnNullWhenProgramEnrollmentNotFoundForLookupByUuidAndProgramName() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, DEFAULT_UUID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_ANOTHER_NAME);

        when(programEnrollmentService.getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID)))
                .thenReturn(prepareProgramEnrollments());

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_UUID_AMD_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientUuid(eq(CONFIG_NAME), eq(DEFAULT_UUID));

        assertNull(object);
    }

    @Test
    public void shouldReturnNullWhenProgramEnrollmentNotFoundForLookupByMotechIdAndProgramName() {
        String className = ProgramEnrollment.class.getSimpleName();

        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, DEFAULT_MOTECH_ID);
        lookupFields.put(PROGRAM_NAME, PROGRAM_ANOTHER_NAME);

        when(programEnrollmentService.getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID)))
                .thenReturn(prepareProgramEnrollments());

        Object object = taskDataProvider.lookup(className + '-' + CONFIG_NAME, BY_MOTECH_ID_AND_PROGRAM_NAME, lookupFields);

        verify(programEnrollmentService).getProgramEnrollmentByPatientMotechId(eq(CONFIG_NAME), eq(DEFAULT_MOTECH_ID));

        assertNull(object);
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

    private List<ProgramEnrollment> prepareProgramEnrollments() {
        Program program = new Program();
        program.setName(PROGRAM_DEFAULT_NAME);
        ProgramEnrollment programEnrollment = new ProgramEnrollment();
        programEnrollment.setProgram(program);

        return Collections.singletonList(programEnrollment);
    }
}
