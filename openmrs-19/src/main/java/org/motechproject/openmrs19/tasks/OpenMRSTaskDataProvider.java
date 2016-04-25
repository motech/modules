package org.motechproject.openmrs19.tasks;

import org.motechproject.commons.api.AbstractDataProvider;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.tasks.builder.OpenMRSTaskDataProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.ENCOUNTER;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.NAME;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.PACKAGE_ROOT;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.PATIENT;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.PROVIDER;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.UUID;

/**
 * This is the OpenMRS task data provider that is registered with the task module as a data source.
 * It allows retrieving objects from OpenMRS model and using it in tasks.
 */
@Service("openMRSTaskDataProvider")
public class OpenMRSTaskDataProvider extends AbstractDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSTaskDataProvider.class);
    private static final List<Class<?>> SUPPORTED_CLASSES = Arrays.asList(Patient.class, Provider.class, Encounter.class);

    private OpenMRSEncounterService encounterService;
    private OpenMRSPatientService patientService;
    private OpenMRSProviderService providerService;
    private OpenMRSTaskDataProviderBuilder dataProviderBuilder;

    @Autowired
    public OpenMRSTaskDataProvider(OpenMRSTaskDataProviderBuilder taskDataProviderBuilder, OpenMRSEncounterService encounterService,
                                   OpenMRSPatientService patientService, OpenMRSProviderService providerService) {

        this.encounterService = encounterService;
        this.patientService = patientService;
        this.providerService = providerService;
        this.dataProviderBuilder = taskDataProviderBuilder;

        String body = dataProviderBuilder.generateDataProvider();
        if (body != null) {
            setBody(body);
        } else {
            LOGGER.warn("OpenMRS configuration is empty.");
        }
    }

    @Override
    public List<Class<?>> getSupportClasses() {
        return SUPPORTED_CLASSES;
    }

    @Override
    public String getPackageRoot() {
        return PACKAGE_ROOT;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object lookup(String type, String lookupName, Map<String, String> lookupFields) {
        Object obj = null;

        String objectType = type.substring(0, type.lastIndexOf('-'));
        String configName = type.substring(type.lastIndexOf('-')+1);

        //In case of any trouble with the type, 'supports' method logs an error
        if (supports(objectType)) {
            switch (objectType) {
                case ENCOUNTER: obj = getEncounter(lookupName, lookupFields, configName);
                    break;
                case PATIENT: obj = getPatient(lookupName, lookupFields, configName);
                    break;
                case PROVIDER: obj = getProvider(lookupName, lookupFields, configName);
                    break;
            }
        }

        return obj;
    }

    private Encounter getEncounter(String lookupName, Map<String, String> lookupFields, String configName) {
        Encounter encounter = null;

        switch (lookupName) {
            case BY_UUID: encounter = encounterService.getEncounterByUuid(configName, lookupFields.get(UUID));
                break;
            default: LOGGER.error("Lookup with name {} doesn't exist for encounter object", lookupName);
                break;
        }

        return encounter;
    }

    private Patient getPatient(String lookupName, Map<String, String> lookupFields, String configName) {
        Patient patient = null;

        switch (lookupName) {
            case BY_MOTECH_ID: patient = patientService.getPatientByMotechId(configName, lookupFields.get(MOTECH_ID));
                break;
            case BY_UUID: patient = patientService.getPatientByUuid(configName, lookupFields.get(UUID));
                break;
            default: LOGGER.error("Lookup with name {} doesn't exist for patient object", lookupName);
                break;
        }

        return patient;
    }

    private Provider getProvider(String lookupName, Map<String, String> lookupFields, String configName) {
        Provider provider = null;

        switch (lookupName) {
            case BY_UUID: provider = providerService.getProviderByUuid(configName, lookupFields.get(UUID));
                break;
            default: LOGGER.error("Lookup with name {} doesn't exist for provider object", lookupName);
                break;
        }

        return provider;
    }

}
