package org.motechproject.openmrs19.tasks;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.tasks.builder.OpenMRSTaskDataProviderBuilder;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.UUID;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSTaskDataProviderTest {

    @Mock
    private OpenMRSEncounterService encounterService;

    @Mock
    private OpenMRSPatientService patientService;

    @Mock
    private OpenMRSProviderService providerService;

    @Mock
    private OpenMRSConfigService configService;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private OpenMRSTaskDataProviderBuilder taskDataProviderBuilder = new OpenMRSTaskDataProviderBuilder();

    private OpenMRSTaskDataProvider taskDataProvider;

    @Before
    public void setUp() {
        taskDataProvider = new OpenMRSTaskDataProvider(taskDataProviderBuilder, encounterService, patientService, providerService);
        when(configService.getConfigs()).thenReturn(new ArrayList<>());
    }

    @Test
    public void shouldReturnNullWhenClassIsNotSupported() {
        String className = "testClass";
        String configName = "configName";

        Object object = taskDataProvider.lookup(className + '-' + configName, "lookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForEncounter() {
        String className = Encounter.class.getSimpleName();
        String configName = "configName";

        Object object = taskDataProvider.lookup(className + '-' + configName, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(encounterService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();

        when(encounterService.getEncounterByUuid(configName, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className+ '-' + configName, BY_UUID, lookupFields);

        assertNull(object);
        verify(encounterService).getEncounterByUuid(configName, null);
    }

    @Test
    public void shouldReturnNullWhenEncounterNotFoundForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(encounterService.getEncounterByUuid(configName, "4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_UUID, lookupFields);

        assertNull(object);
        verify(encounterService).getEncounterByUuid(configName, "4");
    }

    @Test
    public void shouldReturnEncounterForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        Encounter encounter = new Encounter();
        encounter.setEncounterType(new EncounterType("encounterTypeTest"));

        when(encounterService.getEncounterByUuid(configName, "5")).thenReturn(encounter);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_UUID, lookupFields);

        assertEquals(encounter, object);
        verify(encounterService).getEncounterByUuid(configName, "5");
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForPatient() {
        String className = Patient.class.getSimpleName();
        String configName = "configName";

        Object object = taskDataProvider.lookup(className + '-' + configName, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(patientService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();

        when(patientService.getPatientByUuid(configName, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_UUID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByUuid(configName, null);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();

        when(patientService.getPatientByMotechId(configName, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_MOTECH_ID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByMotechId(configName, null);
    }

    @Test
    public void shouldReturnNullWhenPatientNotFoundForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(patientService.getPatientByUuid(configName, "4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_UUID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByUuid(configName, "4");
    }

    @Test
    public void shouldReturnNullWhenPatientNotFoundForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, "4");

        when(patientService.getPatientByMotechId(configName, "4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_MOTECH_ID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByMotechId(configName, "4");
    }

    @Test
    public void shouldReturnPatientForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        Patient patient = new Patient();
        patient.setMotechId("10");

        when(patientService.getPatientByUuid(configName, "5")).thenReturn(patient);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_UUID, lookupFields);

        assertEquals(patient, object);
        verify(patientService).getPatientByUuid(configName, "5");
    }

    @Test
    public void shouldReturnPatientForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, "5");

        Patient patient = new Patient();
        patient.setUuid("10");

        when(patientService.getPatientByMotechId(configName, "5")).thenReturn(patient);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_MOTECH_ID, lookupFields);

        assertEquals(patient, object);
        verify(patientService).getPatientByMotechId(configName, "5");
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForProvider() {
        String className = Provider.class.getSimpleName();
        String configName = "configName";

        Object object = taskDataProvider.lookup(className + '-' + configName, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();

        when(providerService.getProviderByUuid(configName, null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid(configName, null);
    }

    @Test
    public void shouldReturnNullWhenProviderNotFoundForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(providerService.getProviderByUuid(configName, "4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid(configName, "4");
    }

    @Test
    public void shouldReturnProviderForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();
        String configName = "configName";
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        Provider provider = new Provider();
        provider.setIdentifier("testIdentifier");

        when(providerService.getProviderByUuid(configName, "5")).thenReturn(provider);

        Object object = taskDataProvider.lookup(className + '-' + configName, BY_UUID, lookupFields);

        assertEquals(provider, object);
        verify(providerService).getProviderByUuid(configName, "5");
    }

}
