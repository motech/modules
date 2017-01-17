package org.motechproject.openmrs.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Attribute;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.Identifier;
import org.motechproject.openmrs.domain.IdentifierType;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.PatientListResult;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.exception.PatientNotFoundException;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.resource.PatientResource;
import org.motechproject.openmrs.resource.PersonResource;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.motechproject.openmrs.service.OpenMRSPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service("patientService")
public class OpenMRSPatientServiceImpl implements OpenMRSPatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSPatientServiceImpl.class);

    private final OpenMRSPersonService personService;
    private final OpenMRSConfigService configService;
    private final PatientResource patientResource;
    private final PersonResource personResource;

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSPatientServiceImpl(PatientResource patientResource, PersonResource personResource,
                                     OpenMRSPersonService personService, EventRelay eventRelay,
                                     OpenMRSConfigService configService) {
        this.patientResource = patientResource;
        this.personResource = personResource;
        this.configService = configService;
        this.personService = personService;
        this.eventRelay = eventRelay;
    }

    @Override
    public Patient createPatient(String configName, Patient patient) {
        validatePatientBeforeSave(patient);

        Config config = configService.getConfigByName(configName);

        if (patient.getPerson().getUuid() == null) {
            try {
                personResource.checkPersonAttributeTypes(config, patient.getPerson());
                patient.setPerson(personResource.createPerson(config, patient.getPerson()));
            } catch (HttpClientErrorException e) {
                throw new OpenMRSException(String.format("Could not create Person. %s %s", e.getMessage(), e.getResponseBodyAsString()), e);
            }
        }

        IdentifierType identifierType = new IdentifierType();
        identifierType.setUuid(getMotechPatientIdentifierTypeUuid(config));

        Identifier motechIdentifier = new Identifier(patient.getMotechId(), identifierType, patient.getLocationForMotechId());

        List<Identifier> parsedPatientIdentifiers = parsePatientIdentifiers(config, patient.getIdentifiers());
        parsedPatientIdentifiers.add(motechIdentifier);

        patient.setIdentifiers(parsedPatientIdentifiers);

        try {
            Patient savedPatient = patientResource.createPatient(config, patient);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_PATIENT_SUBJECT, EventHelper.patientParameters(savedPatient)));

            return savedPatient;
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not create Patient. %s %s", e.getMessage(), e.getResponseBodyAsString()), e);
        }
    }

    @Override
    public Patient updatePatient(String configName, Patient patient, String currentMotechId) {
        Config config = configService.getConfigByName(configName);
        if (!patient.getMotechId().equals(currentMotechId) && getPatientByMotechId(config, patient.getMotechId()) != null) {
            throw new OpenMRSException("Patient with Motech ID" + patient.getMotechId() + "already exists.");
        }
        return updatePatient(config, patient);
    }

    @Override
    public Patient updatePatient(String configName, Patient patient) {
        return updatePatient(configService.getConfigByName(configName), patient);
    }

    @Override
    public Patient updatePatientIdentifiers(String configName, Patient patient) {
        return updatePatientIdentifiers(configService.getConfigByName(configName), patient);
    }

    @Override
    public Patient getPatientByUuid(String configName, String uuid) {
        return getPatientByUuid(configService.getConfigByName(configName), uuid);
    }

    @Override
    public Patient getPatientByMotechId(String configName, String motechId) {
        return getPatientByMotechId(configService.getConfigByName(configName), motechId);
    }

    @Override
    public Patient getPatientByIdentifier(String configName, String identifierId, String identifierName) {
        return getPatientByIdentifier(configService.getConfigByName(configName), identifierId, identifierName);
    }

    @Override
    public List<Patient> search(String configName, String name, String motechId) {
        Validate.notEmpty(name, "Name cannot be empty");

        List<Patient> result;
        Config config = configService.getConfigByName(configName);

        try {
            result = patientResource.queryForPatient(config, name).getResults();
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed search for patient name: " + name);
            return Collections.emptyList();
        }

        List<Patient> patients = new ArrayList<>();

        for (Patient partialPatient : result) {
            Patient patient = getPatientByUuid(config, partialPatient.getUuid());
            if (motechId == null) {
                patients.add(patient);
            } else {
                if (patient.getMotechId() != null && patient.getMotechId().contains(motechId)) {
                    patients.add(patient);
                }
            }
        }

        if (patients.size() > 0) {
            sortResults(patients);
        }

        return patients;
    }

    @Override
    public void deceasePatient(String configName, String motechId, Concept causeOfDeath, Date dateOfDeath, String comment)
            throws PatientNotFoundException {
        Validate.notEmpty(motechId, "MoTeCh id cannot be empty");
        Config config = configService.getConfigByName(configName);

        Patient patient = getPatientByMotechId(config, motechId);
        if (patient == null) {
            throw new PatientNotFoundException("Cannot decease patient because no patient found with Motech Id: " + motechId);
        }

        savePersonCauseOfDeath(config, patient.getUuid(), dateOfDeath, causeOfDeath);
        eventRelay.sendEventMessage(new MotechEvent(EventKeys.PATIENT_DECEASED_SUBJECT, EventHelper.patientParameters(patient)));
    }

    @Override
    public void deletePatient(String configName, String uuid) throws PatientNotFoundException {
        try {
            Config config = configService.getConfigByName(configName);
            patientResource.deletePatient(config, uuid);

            Patient patient = new Patient();
            patient.setUuid(uuid);

            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_PATIENT_SUBJECT, EventHelper.patientParameters(patient)));
        } catch (HttpClientErrorException e) {
            throw new PatientNotFoundException(e);
        }
    }

    private Patient getPatientByUuid(Config config, String uuid) {
        Validate.notEmpty(uuid, "Patient Id cannot be empty");

        Patient patient;
        try {
            patient = patientResource.getPatientById(config, uuid);
        } catch (HttpStatusCodeException e) {
            throw new OpenMRSException(String.format("Could not get Patient with uuid: %s. %s %s", uuid, e.getMessage(), e.getResponseBodyAsString()), e);
        }

        Identifier motechIdentifier = getMotechIdentifier(config, patient);
        patient.setMotechIdentifierValues(motechIdentifier);

        List<Identifier> identifiers = patient.getIdentifiers();
        identifiers.remove(motechIdentifier);

        List<Identifier> supportedIdentifierTypeList = getSupportedIdentifierTypeList(config, identifiers);
        patient.setIdentifiers(supportedIdentifierTypeList);

        return patient;
    }

    private Patient getPatientByMotechId(Config config, String motechId) {
        Validate.notEmpty(motechId, "Motech Id cannot be empty");

        PatientListResult patientList;
        try {
            patientList = patientResource.queryForPatient(config, motechId);
        } catch (HttpStatusCodeException e) {
            throw new OpenMRSException(String.format("Could not get Patient for MOTECH Id: %s. %s %s", motechId, e.getMessage(), e.getResponseBodyAsString()), e);
        }

        if (patientList.getResults().size() == 0) {
            return null;
        } else if (patientList.getResults().size() > 1) {
            LOGGER.warn("Search for patient by id returned more than 1 result");
        }

        return getPatientByUuid(config.getName(), patientList.getResults().get(0).getUuid());
    }

    private Patient getPatientByIdentifier(Config config, String identifierId, String identifierName) {
        Validate.notEmpty(identifierId, "Identifier Id cannot be empty");
        Validate.notEmpty(identifierName, "Identifier Name cannot be empty");

        PatientListResult patientList;
        Patient fetchedPatient;
        try {
            patientList = patientResource.queryForPatient(config, identifierId);
        } catch (HttpStatusCodeException e) {
            throw new OpenMRSException(String.format("Could not get Patient for %s: %s. %s %s", identifierName, identifierId, e.getMessage(), e.getResponseBodyAsString()), e);
        }

        if (patientList.getResults().size() == 0) {
            return null;
        } else {
            fetchedPatient = getPatientByIdentifierName(config, identifierId, identifierName, patientList);
        }

        return fetchedPatient;
    }

    private Patient getPatientByIdentifierName(Config config, String identifierId, String identifierName, PatientListResult patientList) {
        List<Identifier> identifiersList;
        Patient result = null;
        for (Patient patient : patientList.getResults()) {
            identifiersList = patientResource.getPatientIdentifierList(config, patient.getUuid());

            for (Identifier identifier : identifiersList) {
                if (identifierName.equals(identifier.getIdentifierType().getName()) && identifierId.equals(identifier.getIdentifier())) {
                    result = getPatientByUuid(config.getName(), patient.getUuid());
                    break;
                }
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }

    private Patient updatePatient(Config config, Patient patient) {
        Validate.notNull(patient, "Patient cannot be null");
        Validate.notEmpty(patient.getUuid(), "Patient Id cannot be empty");

        Person person = patient.getPerson();

        personService.updatePerson(config.getName(), person);
        // the openmrs web service requires an explicit delete request to remove
        // attributes. delete all previous attributes, and then
        // create any attributes attached to the patient
        deleteAllAttributes(config, person);
        saveAttributesForPerson(config, person);

        Patient updatedPatient;
        try {
            patientResource.updatePatientMotechId(config, patient.getUuid(), patient.getMotechId());
            updatedPatient = getPatientByUuid(config, patient.getUuid());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_PATIENT_SUBJECT, EventHelper.patientParameters(updatedPatient)));
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Failed to update OpenMRS patient with uuid: %s. %s %s" + patient.getUuid(), e.getMessage(),
                    e.getResponseBodyAsString()), e);
        }

        return updatedPatient;
    }

    private Patient updatePatientIdentifiers(Config config, Patient patient) {
        Validate.notNull(patient, "Patient cannot be null");
        Validate.notEmpty(patient.getUuid(), "Patient Id cannot be empty");

        List<Identifier> identifiersList = patientResource.getPatientIdentifierList(config, patient.getUuid());
        Patient updatedPatient;
        try {
            //Patient Identifiers have to be update separately.
            updatePatientIdentifiers(config, patient, identifiersList);
            updatedPatient = getPatientByUuid(config, patient.getUuid());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_PATIENT_IDENTIFIERS_SUBJECT, EventHelper.patientParameters(updatedPatient)));
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Failed to update OpenMRS patient's identifiers for patient with uuid: %s. %s %s", patient.getUuid(),
                    e.getMessage(), e.getResponseBodyAsString()), e);
        }
        return updatedPatient;
    }

    private void updatePatientIdentifiers(Config config, Patient patient, List<Identifier> fetchedIdentifierList) {
        String fetchedIdentifierName;
        String newIdentifierName;

        for (Identifier newIdentifier : patient.getIdentifiers()) {
            boolean isUpdated = false;

            for (Identifier fetchedIdentifier : fetchedIdentifierList) {
                fetchedIdentifierName = fetchedIdentifier.getIdentifierType().getName();
                newIdentifierName = newIdentifier.getIdentifierType().getName();

                if (newIdentifierName.equals(fetchedIdentifierName)) {
                    fetchedIdentifier.setIdentifier(newIdentifier.getIdentifier());
                    patientResource.updatePatientIdentifier(config, patient.getUuid(), fetchedIdentifier);
                    isUpdated = true;
                }
            }
            if (!isUpdated) {
                addNewPatientIdentifier(config, patient, newIdentifier);
            }
        }
    }

    private void addNewPatientIdentifier(Config config, Patient patient, Identifier newIdentifier) {
        String uuid = patientResource.getPatientIdentifierTypeUuidByName(config, newIdentifier.getIdentifierType().getName());

        if (uuid == null) {
            LOGGER.warn("The identifier type with name {} is not supported", newIdentifier.getIdentifierType().getName());
        } else {
            newIdentifier.getIdentifierType().setUuid(uuid);
            patientResource.addPatientIdentifier(config, patient.getUuid(), newIdentifier);
        }
    }
    private void validatePatientBeforeSave(Patient patient) {
        Validate.notNull(patient, "Patient cannot be null");
        Validate.isTrue(StringUtils.isNotEmpty(patient.getMotechId()), "You must provide a motech id to save a patient");
        Validate.notNull(patient.getPerson(), "Person cannot be null when saving a patient");
        Validate.notNull(patient.getLocationForMotechId(), "Location cannot be null when saving a patient");
        if (Objects.equals(patient.getPerson().getDead(), true)) {
            Validate.notNull(patient.getPerson().getCauseOfDeath(), "Cause of death cannot be null when dead flag is set to true");
        }

    }

    private Identifier getMotechIdentifier(Config config, Patient patient) {
        String motechIdentifierUuid = getMotechPatientIdentifierTypeUuid(config);
        return patient.getIdentifierByTypeUuid(motechIdentifierUuid);
    }

    private String getMotechPatientIdentifierTypeUuid(Config config) {
        String motechPatientIdentifierTypeUuid;

        try {
            motechPatientIdentifierTypeUuid = patientResource.getMotechPatientIdentifierUuid(config);
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not get Patient Identifier Type uuid. %s, %s", e.getMessage(), e.getResponseBodyAsString()), e);
        }

        if (motechPatientIdentifierTypeUuid == null) {
            throw new OpenMRSException(String.format("Motech ID \"%s\" is not available on the OpenMRS server",
                    config.getMotechPatientIdentifierTypeName()));
        }

        return motechPatientIdentifierTypeUuid;
    }

    private void sortResults(List<Patient> searchResults) {
        Collections.sort(searchResults, new Comparator<Patient>() {
            @Override
            public int compare(Patient patient1, Patient patient2) {
                if (StringUtils.isNotEmpty(patient1.getMotechId()) && StringUtils.isNotEmpty(patient2.getMotechId())) {
                    return patient1.getMotechId().compareTo(patient2.getMotechId());
                } else if (StringUtils.isNotEmpty(patient1.getMotechId())) {
                    return -1;
                } else if (StringUtils.isNotEmpty(patient2.getMotechId())) {
                    return 1;
                }
                return 0;
            }
        });
    }

    private List<Identifier> getSupportedIdentifierTypeList(Config config, List<Identifier> patientIdentifierList) {
        List<Identifier> supportedIdentifierTypeList = new ArrayList<>();

        for (Identifier identifier : patientIdentifierList) {
            String identifierTypeUuid =  identifier.getIdentifierType().getUuid();

            try {
                String identifierTypeName = patientResource.getPatientIdentifierTypeNameByUuid(config, identifierTypeUuid);
                if (identifierTypeName == null) {
                    LOGGER.warn("The identifier type with UUID {} is not supported", identifierTypeUuid);
                } else {
                    identifier.getIdentifierType().setName(identifierTypeName);
                    supportedIdentifierTypeList.add(identifier);
                }
            } catch (HttpClientErrorException e) {
                throw new OpenMRSException(String.format("Could not get supported Identifier Type for uuid: %s. %s %s", identifierTypeUuid, e.getMessage(),
                        e.getResponseBodyAsString()), e);
            }
        }

        return supportedIdentifierTypeList;
    }

    /**
     * Parses patient identifiers. This method checks if the given identifier type name is supported by MOTECH
     * and swaps identifier type name for identifier type uuid.
     *
     * @param identifiers the identifiers of patient, key - identifier type name, value - identifier number
     * @return parsed patient identifiers, key - identifier type uuid, value - identifier number
     */
    private List<Identifier> parsePatientIdentifiers(Config config, List<Identifier> identifiers) {
        List<Identifier> parsedIdentifiers = new ArrayList<>();

        for (Identifier identifier : identifiers) {
            String identifierTypeName = identifier.getIdentifierType().getName();

            try {
                String identifierTypeUuid = patientResource.getPatientIdentifierTypeUuidByName(config, identifierTypeName);
                if (identifierTypeUuid == null) {
                    LOGGER.warn("The identifier type with name {} is not supported", identifierTypeName);
                } else {
                    identifier.getIdentifierType().setUuid(identifierTypeUuid);
                    parsedIdentifiers.add(identifier);
                }
            } catch (HttpClientErrorException e) {
                throw new OpenMRSException(String.format("Could not get Patient Identifier Type for name: %s. %s %s", identifierTypeName,
                        e.getMessage(), e.getResponseBodyAsString()), e);
            }
        }

        return parsedIdentifiers;
    }

    private void deleteAllAttributes(Config config, Person person) {
        Person saved;
        try {
            saved = personResource.getPersonById(config, person.getUuid());
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Failed to retrieve person when deleting attributes with uuid: " + person.getUuid(), e);
        }

        for (Attribute attribute : saved.getAttributes()) {
            try {
                personResource.deleteAttribute(config, person.getUuid(), attribute);
            } catch (HttpClientErrorException e) {
                throw new OpenMRSException(String.format("Could not delete Person's attribute for Person uuid: %s and Attribute uuid: %s. %s %s",
                        person.getUuid(), attribute.getUuid(), e.getMessage(), e.getResponseBodyAsString()), e);
            }
        }
    }

    private void savePersonCauseOfDeath(Config config, String patientId, Date deathDate, Concept causeOfDeath) {
        Person person = new Person();
        person.setUuid(patientId);
        person.setDead(true);
        person.setDeathDate(deathDate);
        person.setCauseOfDeath(causeOfDeath);

        try {
            personResource.updatePerson(config, person);
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Failed to save cause of death observation for patient id: " + patientId, e);
        }
    }

    private void saveAttributesForPerson(Config config, Person person) {
        for (Attribute attribute : person.getAttributes()) {
            try {
                personResource.createPersonAttribute(config, person.getUuid(), attribute);
            } catch (HttpClientErrorException e) {
                LOGGER.warn("Unable to add attribute to person with id: " + person.getUuid());
                throw new OpenMRSException(String.format("Could not create Person's attribute for Person uuid: %s and Attribute uuid: %s. %s %s",
                        person.getUuid(), attribute.getUuid(), e.getMessage(), e.getResponseBodyAsString()), e);
            }
        }
    }
}
