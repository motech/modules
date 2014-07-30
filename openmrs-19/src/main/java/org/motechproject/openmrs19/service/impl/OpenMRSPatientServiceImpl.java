package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.EventKeys;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.exception.PatientNotFoundException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.PatientResource;
import org.motechproject.openmrs19.resource.model.Identifier;
import org.motechproject.openmrs19.resource.model.IdentifierType;
import org.motechproject.openmrs19.resource.model.Location;
import org.motechproject.openmrs19.resource.model.Patient;
import org.motechproject.openmrs19.resource.model.PatientListResult;
import org.motechproject.openmrs19.resource.model.Person;
import org.motechproject.openmrs19.rest.HttpException;
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

    private static Logger logger = LoggerFactory.getLogger(OpenMRSPatientServiceImpl.class);

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
            logger.error("Failed search for patient by MoTeCH Id: " + motechId);
            return null;
        }

        if (patientList.getResults().size() == 0) {
            return null;
        } else if (patientList.getResults().size() > 1) {
            logger.warn("Search for patient by id returned more than 1 result");
        }

        return getPatient(patientList.getResults().get(0).getUuid());
    }

    @Override
    public OpenMRSPatient getPatient(String patientId) {
        Validate.notEmpty(patientId, "Patient Id cannot be empty");

        Patient patient;
        try {
            patient = patientResource.getPatientById(patientId);
        } catch (HttpException e) {
            logger.error("Failed to get patient by id: " + patientId);
            return null;
        }

        String motechIdentifierUuid;
        try {
            motechIdentifierUuid = patientResource.getMotechPatientIdentifierUuid();
        } catch (HttpException e) {
            logger.error("There was an exception retrieving the MoTeCH Identifier Type UUID");
            return null;
        }

        Identifier identifier = patient.getIdentifierByIdentifierType(motechIdentifierUuid);
        if (identifier == null) {
            logger.warn("No MoTeCH Id found on Patient with id: " + patient.getUuid());
        }

        // since OpenMRS 1.9, patient identifiers no longer require an explicit
        // location
        // this means the identifier's location could be null
        // this is a guard against this situation
        if (identifier != null && identifier.getLocation() != null) {
            String facililtyUuid = identifier.getLocation().getUuid();
            return convertToMrsPatient(patient, identifier.getIdentifier(), facilityAdapter.getFacility(facililtyUuid));
        } else {
            String motechId = null;
            if (identifier != null) {
                motechId = identifier.getIdentifier();
            }

            return convertToMrsPatient(patient, motechId, null);
        }
    }

    private OpenMRSPatient convertToMrsPatient(Patient patient, String identifier, OpenMRSFacility facility) {
        OpenMRSPatient converted = new OpenMRSPatient(patient.getUuid(), identifier,
            ConverterUtils.convertToMrsPerson(patient.getPerson()), facility);
        return converted;
    }

    @Override
    public OpenMRSPatient savePatient(OpenMRSPatient patient) {
        validatePatientBeforeSave(patient);
        OpenMRSPatient openMRSPatient = ConverterUtils.createPatient(patient);

        OpenMRSPerson savedPerson = personAdapter.addPerson(patient.getPerson());

        Patient converted = fromMrsPatient(openMRSPatient, savedPerson);
        if (converted == null) {
            return null;
        }

        OpenMRSPatient savedPatient;
        try {
            patientResource.createPatient(converted);
            savedPatient =  new OpenMRSPatient(openMRSPatient.getPatientId(), openMRSPatient.getMotechId(), savedPerson, 
                openMRSPatient.getFacility());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_PATIENT_SUBJECT, EventHelper.patientParameters(savedPatient)));
        } catch (HttpException e) {
            logger.error("Failed to create a patient in OpenMRS with MoTeCH Id: " + patient.getMotechId());
            return null;
        }

        return savedPatient;
    }

    private void validatePatientBeforeSave(OpenMRSPatient patient) {
        Validate.notNull(patient, "Patient cannot be null");
        Validate.isTrue(StringUtils.isNotEmpty(patient.getMotechId()), "You must provide a motech id to save a patient");
        Validate.notNull(patient.getPerson(), "Person cannot be null when saving a patient");
        Validate.notNull(patient.getFacility(), "Facility cannot be null when saving a patient");
    }

    private Patient fromMrsPatient(OpenMRSPatient patient, OpenMRSPerson savedPerson) {
        Patient converted = new Patient();
        Person person = new Person();
        person.setUuid(savedPerson.getPersonId());
        converted.setPerson(person);

        Location location = null;
        if (patient.getFacility() != null && StringUtils.isNotBlank(patient.getFacility().getFacilityId())) {
            location = new Location();
            location.setUuid(patient.getFacility().getFacilityId());
        }

        String motechPatientIdentiferTypeUuid;
        try {
            motechPatientIdentiferTypeUuid = patientResource.getMotechPatientIdentifierUuid();
        } catch (HttpException e) {
            logger.error("There was an exception retrieving the MoTeCH Identifier Type UUID");
            return null;
        }

        if (StringUtils.isBlank(motechPatientIdentiferTypeUuid)) {
            logger.error("Cannot save a patient until a MoTeCH Patient Idenitifer Type is created in the OpenMRS");
            return null;
        }

        IdentifierType type = new IdentifierType();
        type.setUuid(motechPatientIdentiferTypeUuid);

        Identifier identifier = new Identifier();
        identifier.setIdentifier(patient.getMotechId());
        identifier.setLocation(location);
        identifier.setIdentifierType(type);

        List<Identifier> identifiers = new ArrayList<>();
        identifiers.add(identifier);
        converted.setIdentifiers(identifiers);

        return converted;
    }

    @Override
    public List<OpenMRSPatient> search(String name, String id) {
        Validate.notEmpty(name, "Name cannot be empty");

        PatientListResult result;
        try {
            result = patientResource.queryForPatient(name);
        } catch (HttpException e) {
            logger.error("Failed search for patient name: " + name);
            return Collections.emptyList();
        }

        List<OpenMRSPatient> searchResults = new ArrayList<>();

        for (Patient partialPatient : result.getResults()) {
            OpenMRSPatient patient = getPatient(partialPatient.getUuid());
            if (id == null) {
                searchResults.add(patient);
            } else {
                if (patient.getMotechId() != null && patient.getMotechId().contains(id)) {
                    searchResults.add(patient);
                }
            }
        }

        if (searchResults.size() > 0) {
            sortResults(searchResults);
        }


        List<OpenMRSPatient> patientList = new ArrayList<>();

        patientList.addAll(searchResults);

        return patientList;
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

        OpenMRSPatient openMRSPatient = ConverterUtils.createPatient(patient);
        OpenMRSPerson person = ConverterUtils.createPerson(patient.getPerson());

        personAdapter.updatePerson(person);
        // the openmrs web service requires an explicit delete request to remove
        // attributes. delete all previous attributes, and then
        // create any attributes attached to the patient
        personAdapter.deleteAllAttributes(person);
        personAdapter.saveAttributesForPerson(person);

        try {
            patientResource.updatePatientMotechId(patient.getPatientId(), patient.getMotechId());
        } catch (HttpException e) {
            logger.error("Failed to update OpenMRS patient with id: " + patient.getPatientId());
            throw new OpenMRSException(e);
        }

        OpenMRSPatient updatedPatient = new OpenMRSPatient(openMRSPatient.getPatientId(), patient.getMotechId(), person, openMRSPatient.getFacility());
        eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_PATIENT_SUBJECT, EventHelper.patientParameters(updatedPatient)));
        return updatedPatient;
    }

    @Override
    public void deceasePatient(String motechId, String conceptName, Date dateOfDeath, String comment)
            throws PatientNotFoundException {
        Validate.notEmpty(motechId, "MoTeCh id cannot be empty");

        OpenMRSPatient patient = getPatientByMotechId(motechId);
        if (patient == null) {
            logger.error("Cannot decease patient because no patient exist with motech id: " + motechId);
            throw new PatientNotFoundException("No Patient found with Motech Id: " + motechId);
        }

        personAdapter.savePersonCauseOfDeath(patient.getPatientId(), dateOfDeath, conceptName);
        eventRelay.sendEventMessage(new MotechEvent(EventKeys.PATIENT_DECEASED_SUBJECT, EventHelper.patientParameters(patient)));
    }

    @Override
    public List<OpenMRSPatient> getAllPatients() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deletePatient(OpenMRSPatient patient) throws PatientNotFoundException {
        try {
            String id = patientResource.queryForPatient(patient.getMotechId()).getResults().get(0).getUuid();
            patientResource.deletePatient(id);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_PATIENT_SUBJECT, EventHelper.patientParameters(patient)));
        } catch (HttpException e) {
            throw new PatientNotFoundException(e);
        }
    }

}
