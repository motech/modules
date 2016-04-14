package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.Identifier;
import org.motechproject.openmrs19.domain.IdentifierType;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.PatientListResult;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.exception.PatientNotFoundException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.PatientResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service("patientService")
public class OpenMRSPatientServiceImpl implements OpenMRSPatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSPatientServiceImpl.class);

    private final PatientResource patientResource;
    private final OpenMRSPersonServiceImpl personAdapter;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSPatientServiceImpl(PatientResource patientResource, OpenMRSPersonServiceImpl personAdapter,
                                     EventRelay eventRelay) {
        this.patientResource = patientResource;
        this.personAdapter = personAdapter;
        this.eventRelay = eventRelay;
    }

    @Override
    public Patient createPatient(Patient patient) {
        validatePatientBeforeSave(patient);

        if (patient.getPerson().getUuid() == null) {
            patient.setPerson(personAdapter.createPerson(patient.getPerson()));
        }

        IdentifierType identifierType = new IdentifierType();
        identifierType.setUuid(getMotechPatientIdentifierTypeUuid());

        Identifier motechIdentifier = new Identifier(patient.getMotechId(), identifierType, patient.getLocationForMotechId());

        List<Identifier> parsedPatientIdentifiers = parsePatientIdentifiers(patient.getIdentifiers());
        parsedPatientIdentifiers.add(motechIdentifier);

        patient.setIdentifiers(parsedPatientIdentifiers);

        try {
            Patient savedPatient = patientResource.createPatient(patient);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_PATIENT_SUBJECT, EventHelper.patientParameters(savedPatient)));

            return savedPatient;
        } catch (HttpException e) {
            LOGGER.error("Failed to create a patient in OpenMRS with MOTECH Id: " + patient.getMotechId());
            return null;
        }
    }

    @Override
    public Patient updatePatient(Patient patient, String currentMotechId) {
        if (!patient.getMotechId().equals(currentMotechId) && getPatientByMotechId(patient.getMotechId()) != null) {
            throw new OpenMRSException("Patient with Motech ID" + patient.getMotechId() + "already exists.");
        }
        return updatePatient(patient);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        Validate.notNull(patient, "Patient cannot be null");
        Validate.notEmpty(patient.getUuid(), "Patient Id may not be empty");

        Person person = patient.getPerson();

        personAdapter.updatePerson(person);
        // the openmrs web service requires an explicit delete request to remove
        // attributes. delete all previous attributes, and then
        // create any attributes attached to the patient
        personAdapter.deleteAllAttributes(person);
        personAdapter.saveAttributesForPerson(person);

        Patient updatedPatient;
        try {
            updatedPatient = patientResource.updatePatientMotechId(patient.getUuid(), patient.getMotechId());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_PATIENT_SUBJECT, EventHelper.patientParameters(updatedPatient)));
        } catch (HttpException e) {
            throw new OpenMRSException("Failed to update OpenMRS patient with id: " + patient.getUuid(), e);
        }

        return updatedPatient;
    }

    @Override
    public Patient getPatientByUuid(String uuid) {
        Validate.notEmpty(uuid, "Patient Id cannot be empty");

        Patient patient;
        try {
            patient = patientResource.getPatientById(uuid);
        } catch (HttpException e) {
            LOGGER.error("Failed to get patient by id: " + uuid);
            return null;
        }

        Identifier motechIdentifier = getMotechIdentifier(patient);
        patient.setMotechIdentifierValues(motechIdentifier);

        List<Identifier> identifiers = patient.getIdentifiers();
        identifiers.remove(motechIdentifier);

        List<Identifier> supportedIdentifierTypeList = getSupportedIdentifierTypeList(identifiers);
        patient.setIdentifiers(supportedIdentifierTypeList);

        return patient;
    }

    @Override
    public Patient getPatientByMotechId(String motechId) {
        Validate.notEmpty(motechId, "Motech Id cannot be empty");

        PatientListResult patientList;
        try {
            patientList = patientResource.queryForPatient(motechId);
        } catch (HttpException e) {
            LOGGER.error("Failed search for patient by MOTECH Id: " + motechId);
            return null;
        }

        if (patientList.getResults().size() == 0) {
            return null;
        } else if (patientList.getResults().size() > 1) {
            LOGGER.warn("Search for patient by id returned more than 1 result");
        }

        return getPatientByUuid(patientList.getResults().get(0).getUuid());
    }

    @Override
    public List<Patient> search(String name, String motechId) {
        Validate.notEmpty(name, "Name cannot be empty");

        List<Patient> result;

        try {
            result = patientResource.queryForPatient(name).getResults();
        } catch (HttpException e) {
            LOGGER.error("Failed search for patient name: " + name);
            return Collections.emptyList();
        }

        List<Patient> patients = new ArrayList<>();

        for (Patient partialPatient : result) {
            Patient patient = getPatientByUuid(partialPatient.getUuid());
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
    public void deceasePatient(String motechId, Concept causeOfDeath, Date dateOfDeath, String comment)
            throws PatientNotFoundException {
        Validate.notEmpty(motechId, "MoTeCh id cannot be empty");

        Patient patient = getPatientByMotechId(motechId);
        if (patient == null) {
            throw new PatientNotFoundException("Cannot decease patient because no patient found with Motech Id: " + motechId);
        }

        personAdapter.savePersonCauseOfDeath(patient.getUuid(), dateOfDeath, causeOfDeath);
        eventRelay.sendEventMessage(new MotechEvent(EventKeys.PATIENT_DECEASED_SUBJECT, EventHelper.patientParameters(patient)));
    }

    @Override
    public void deletePatient(String uuid) throws PatientNotFoundException {
        try {
            patientResource.deletePatient(uuid);

            Patient patient = new Patient();
            patient.setUuid(uuid);

            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_PATIENT_SUBJECT, EventHelper.patientParameters(patient)));
        } catch (HttpException e) {
            throw new PatientNotFoundException(e);
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

    private Identifier getMotechIdentifier(Patient patient) {
        String motechIdentifierUuid = getMotechPatientIdentifierTypeUuid();
        return patient.getIdentifierByTypeUuid(motechIdentifierUuid);
    }

    private String getMotechPatientIdentifierTypeUuid() {
        String motechPatientIdentifierTypeUuid;

        try {
            motechPatientIdentifierTypeUuid = patientResource.getMotechPatientIdentifierUuid();
        } catch (HttpException e) {
            LOGGER.error("There was an exception retrieving the MOTECH Identifier Type UUID");
            return null;
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

    private List<Identifier> getSupportedIdentifierTypeList(List<Identifier> patientIdentifierList) {
        List<Identifier> supportedIdentifierTypeList = new ArrayList<>();

        for (Identifier identifier : patientIdentifierList) {
            String identifierTypeUuid =  identifier.getIdentifierType().getUuid();

            try {
                String identifierTypeName = patientResource.getPatientIdentifierTypeNameByUuid(identifierTypeUuid);
                if (identifierTypeName == null) {
                    LOGGER.warn("The identifier type with UUID {} is not supported", identifierTypeUuid);
                } else {
                    identifier.getIdentifierType().setName(identifierTypeName);
                    supportedIdentifierTypeList.add(identifier);
                }
            } catch (HttpException e) {
                LOGGER.error("There was an exception retrieving the identifier type with UUID {}", identifierTypeUuid);
                return null;
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
    private List<Identifier> parsePatientIdentifiers(List<Identifier> identifiers) {
        List<Identifier> parsedIdentifiers = new ArrayList<>();

        for (Identifier identifier : identifiers) {
            String identifierTypeName = identifier.getIdentifierType().getName();

            try {
                String identifierTypeUuid = patientResource.getPatientIdentifierTypeUuidByName(identifierTypeName);
                if (identifierTypeUuid == null) {
                    LOGGER.warn("The identifier type with name {} is not supported", identifierTypeName);
                } else {
                    identifier.getIdentifierType().setUuid(identifierTypeUuid);
                    parsedIdentifiers.add(identifier);
                }
            } catch (HttpException e) {
                LOGGER.error("There was an exception retrieving the identifier type with name {}", identifierTypeName);
            }
        }

        return parsedIdentifiers;
    }
}
