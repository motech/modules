package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.exception.PatientNotFoundException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.PatientResource;
import org.motechproject.openmrs19.resource.model.Identifier;
import org.motechproject.openmrs19.resource.model.Patient;
import org.motechproject.openmrs19.resource.model.PatientListResult;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service("patientService")
public class OpenMRSPatientServiceImpl implements OpenMRSPatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSPatientServiceImpl.class);

    private final PatientResource patientResource;
    private final OpenMRSPersonServiceImpl personAdapter;
    private final OpenMRSFacilityService facilityAdapter;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSPatientServiceImpl(PatientResource patientResource, OpenMRSPersonServiceImpl personAdapter,
            OpenMRSFacilityService facilityAdapter, EventRelay eventRelay) {
        this.patientResource = patientResource;
        this.personAdapter = personAdapter;
        this.facilityAdapter = facilityAdapter;
        this.eventRelay = eventRelay;
    }

    @Override
    public OpenMRSPatient getPatientByMotechId(String motechId) {
        Validate.notEmpty(motechId, "Motech Id cannot be empty");

        PatientListResult patientList;
        try {
            patientList = patientResource.queryForPatient(motechId);
        } catch (HttpException e) {
            LOGGER.error("Failed search for patient by MoTeCH Id: " + motechId);
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
    public OpenMRSPatient getPatientByUuid(String uuid) {
        Validate.notEmpty(uuid, "Patient Id cannot be empty");

        Patient patient;
        try {
            patient = patientResource.getPatientById(uuid);
        } catch (HttpException e) {
            LOGGER.error("Failed to get patient by id: " + uuid);
            return null;
        }

        String motechIdentifierUuid;
        try {
            motechIdentifierUuid = patientResource.getMotechPatientIdentifierUuid();
        } catch (HttpException e) {
            LOGGER.error("There was an exception retrieving the MoTeCH Identifier Type UUID");
            return null;
        }

        String motechId = null;
        OpenMRSFacility facility = null;
        Identifier motechIdentifier = patient.getIdentifierByIdentifierType(motechIdentifierUuid);

        if (motechIdentifier == null) {
            LOGGER.warn("No MoTeCH Id found on Patient with id: " + patient.getUuid());
        } else {

            if (motechIdentifier.getLocation() != null) {
                facility = facilityAdapter.getFacilityByUuid(motechIdentifier.getLocation().getUuid());
            }

            motechId = motechIdentifier.getIdentifier();
        }

        List<Identifier> supportedIdentifierTypeList = getSupportedIdentifierTypeList(patient.getIdentifiers(), motechIdentifierUuid);

        return ConverterUtils.toOpenMRSPatient(patient, facility, motechId, supportedIdentifierTypeList);
    }

    @Override
    public OpenMRSPatient createPatient(OpenMRSPatient patient) {

        validatePatientBeforeSave(patient);

        OpenMRSPatient openMRSPatient = ConverterUtils.copyPatient(patient);

        OpenMRSPerson person;

        if (patient.getPerson().getId() == null) {
            person = personAdapter.createPerson(patient.getPerson());
        } else {
            person = patient.getPerson();
        }

        Patient converted = ConverterUtils.toPatient(openMRSPatient, person, getMotechPatientIdentifierTypeUuid());

        try {
            OpenMRSPatient savedPatient = ConverterUtils.toOpenMRSPatient(patientResource.createPatient(converted));
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_PATIENT_SUBJECT, EventHelper.patientParameters(savedPatient)));

            return savedPatient;

        } catch (HttpException e) {
            LOGGER.error("Failed to create a patient in OpenMRS with MoTeCH Id: " + patient.getMotechId());
            return null;
        }
    }

    private void validatePatientBeforeSave(OpenMRSPatient patient) {
        Validate.notNull(patient, "Patient cannot be null");
        Validate.isTrue(StringUtils.isNotEmpty(patient.getMotechId()), "You must provide a motech id to save a patient");
        Validate.notNull(patient.getPerson(), "Person cannot be null when saving a patient");
        Validate.notNull(patient.getFacility(), "Facility cannot be null when saving a patient");
    }

    @Override
    public List<OpenMRSPatient> search(String name, String id) {

        Validate.notEmpty(name, "Name cannot be empty");

        PatientListResult result;

        try {
            result = patientResource.queryForPatient(name);
        } catch (HttpException e) {
            LOGGER.error("Failed search for patient name: " + name);
            return Collections.emptyList();
        }

        List<OpenMRSPatient> patients = new ArrayList<>();

        for (Patient partialPatient : result.getResults()) {
            OpenMRSPatient patient = getPatientByUuid(partialPatient.getUuid());
            if (id == null) {
                patients.add(patient);
            } else {
                if (patient.getMotechId() != null && patient.getMotechId().contains(id)) {
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
    public OpenMRSPatient updatePatient(OpenMRSPatient patient, String currentMotechId) {
        if (!patient.getMotechId().equals(currentMotechId) && getPatientByMotechId(patient.getMotechId()) != null) {
            throw new OpenMRSException("Patient with Motech ID" + patient.getMotechId() + "already exists.");
        }
        return updatePatient(patient);
    }

    @Override
    public OpenMRSPatient updatePatient(OpenMRSPatient patient) {
        Validate.notNull(patient, "Patient cannot be null");
        Validate.notEmpty(patient.getPatientId(), "Patient Id may not be empty");

        OpenMRSPatient openMRSPatient = ConverterUtils.copyPatient(patient);
        OpenMRSPerson person = ConverterUtils.copyPerson(patient.getPerson());

        personAdapter.updatePerson(person);
        // the openmrs web service requires an explicit delete request to remove
        // attributes. delete all previous attributes, and then
        // create any attributes attached to the patient
        personAdapter.deleteAllAttributes(person);
        personAdapter.saveAttributesForPerson(person);

        try {
            patientResource.updatePatientMotechId(patient.getPatientId(), patient.getMotechId());
        } catch (HttpException e) {
            throw new OpenMRSException("Failed to update OpenMRS patient with id: " + patient.getPatientId(), e);
        }

        OpenMRSPatient updatedPatient = new OpenMRSPatient(openMRSPatient.getPatientId(), patient.getMotechId(), person, openMRSPatient.getFacility(), null);
        eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_PATIENT_SUBJECT, EventHelper.patientParameters(updatedPatient)));
        return updatedPatient;
    }

    @Override
    public void deceasePatient(String motechId, OpenMRSConcept causeOfDeath, Date dateOfDeath, String comment)
            throws PatientNotFoundException {
        Validate.notEmpty(motechId, "MoTeCh id cannot be empty");

        OpenMRSPatient patient = getPatientByMotechId(motechId);
        if (patient == null) {
            throw new PatientNotFoundException("Cannot decease patient because no patient found with Motech Id: " + motechId);
        }

        personAdapter.savePersonCauseOfDeath(patient.getPatientId(), dateOfDeath, ConverterUtils.toConcept(causeOfDeath));
        eventRelay.sendEventMessage(new MotechEvent(EventKeys.PATIENT_DECEASED_SUBJECT, EventHelper.patientParameters(patient)));
    }

    @Override
    public void deletePatient(String uuid) throws PatientNotFoundException {

        try {
            patientResource.deletePatient(uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_PATIENT_SUBJECT, EventHelper.patientParameters(new OpenMRSPatient(uuid))));
        } catch (HttpException e) {
            throw new PatientNotFoundException(e);
        }
    }

    private String getMotechPatientIdentifierTypeUuid() {
        String motechPatientIdentifierTypeUuid;

        try {
            motechPatientIdentifierTypeUuid = patientResource.getMotechPatientIdentifierUuid();
        } catch (HttpException e) {
            LOGGER.error("There was an exception retrieving the MoTeCH Identifier Type UUID");
            return null;
        }

        if (StringUtils.isBlank(motechPatientIdentifierTypeUuid)) {
            LOGGER.error("Cannot save a patient until a MoTeCH Patient Identifier Type is created in the OpenMRS");
            return null;
        }

        return motechPatientIdentifierTypeUuid;
    }

    private void sortResults(List<OpenMRSPatient> searchResults) {
        Collections.sort(searchResults, new Comparator<OpenMRSPatient>() {
            @Override
            public int compare(OpenMRSPatient patient1, OpenMRSPatient patient2) {
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

    private List<Identifier> getSupportedIdentifierTypeList(List<Identifier> patientIdentifierList, String motechIdentifierUuid) {
        List<Identifier> supportedIdentifierTypeList = new ArrayList<>();

        for (Identifier identifier : patientIdentifierList) {
            String identifierTypeUuid =  identifier.getIdentifierType().getUuid();

            // we omit motechIdentifier, because this identifier is stored differently
            if (!StringUtils.equals(identifierTypeUuid, motechIdentifierUuid)) {
                try {
                    String identifierTypeName = patientResource.getPatientIdentifierTypeNameByUuid(identifierTypeUuid);
                    if (identifierTypeName == null) {
                        LOGGER.warn("The identifier with UUID {} is not supported", identifierTypeUuid);
                    } else {
                        identifier.getIdentifierType().setName(identifierTypeName);
                        supportedIdentifierTypeList.add(identifier);
                    }
                } catch (HttpException e) {
                    LOGGER.error("There was an exception retrieving the identifier with UUID {}", identifierTypeUuid);
                    return null;
                }
            }
        }

        return supportedIdentifierTypeList;
    }
}
