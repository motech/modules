package org.motechproject.openmrs19.tasks.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSEncounter;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSProvider;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.tasks.OpenMRSActionProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link org.motechproject.openmrs19.tasks.OpenMRSActionProxyService} interface.
 */
@Service("openMRSActionProxyService")
public class OpenMRSActionProxyServiceImpl implements OpenMRSActionProxyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSActionProxyServiceImpl.class);

    private OpenMRSConceptService conceptService;
    private OpenMRSEncounterService encounterService;
    private OpenMRSFacilityService facilityService;
    private OpenMRSPatientService patientService;
    private OpenMRSProviderService providerService;

    @Override
    public void createEncounter(DateTime encounterDate, String encounterType, String locationName, String patientUuid, String providerUuid) {
        OpenMRSFacility facility = getFacilityByName(locationName);
        OpenMRSPatient patient = patientService.getPatientByUuid(patientUuid);
        OpenMRSProvider provider = providerService.getProviderByUuid(providerUuid);

        OpenMRSEncounter encounter = new OpenMRSEncounter.OpenMRSEncounterBuilder()
                .withDate(encounterDate.toDate())
                .withEncounterType(encounterType)
                .withFacility(facility)
                .withPatient(patient)
                .withProvider(provider)
                .build();

        encounterService.createEncounter(encounter);
    }

    @Override
    public void createPatient(String firstName, String middleName, String lastName, String address, DateTime dateOfBirth,
                              Boolean birthDateEstimated, String gender, Boolean dead, String causeOfDeathUUID, String motechId,
                              String locationName, Map<String, String> identifiers) {
        OpenMRSConcept causeOfDeath = StringUtils.isNotEmpty(causeOfDeathUUID) ? conceptService.getConceptByUuid(causeOfDeathUUID) : null;

        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName(firstName);
        person.setMiddleName(middleName);
        person.setLastName(lastName);
        person.setGender(gender);
        person.setAddress(address);
        person.setDateOfBirth(dateOfBirth);
        person.setBirthDateEstimated(birthDateEstimated);
        person.setDead(dead);
        person.setCauseOfDeath(causeOfDeath);

        OpenMRSFacility facility = StringUtils.isNotEmpty(locationName) ? getFacilityByName(locationName) : getDefaultFacility();

        OpenMRSPatient patient = new OpenMRSPatient(motechId, person, facility, identifiers);
        patientService.createPatient(patient);
    }

    private OpenMRSFacility getDefaultFacility() {
        return getFacilityByName(DEFAULT_LOCATION_NAME);
    }

    private OpenMRSFacility getFacilityByName(String locationName) {
        OpenMRSFacility facility = null;

        if (StringUtils.isNotEmpty(locationName)) {
            List<? extends OpenMRSFacility> facilities = facilityService.getFacilities(locationName);
            if (facilities.isEmpty()) {
                LOGGER.warn("There is no location with name {}", locationName);
            } else {
                if (facilities.size() > 1) {
                    LOGGER.warn("There is more than one location with name {}.", locationName);
                }
                facility = facilities.get(0);
            }
        }

        return facility;
    }

    @Autowired
    public void setConceptService(OpenMRSConceptService conceptService) {
        this.conceptService = conceptService;
    }

    @Autowired
    public void setEncounterService(OpenMRSEncounterService encounterService) {
        this.encounterService = encounterService;
    }

    @Autowired
    public void setFacilityService(OpenMRSFacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @Autowired
    public void setPatientService(OpenMRSPatientService patientService) {
        this.patientService = patientService;
    }

    @Autowired
    public void setProviderService(OpenMRSProviderService providerService) {
        this.providerService = providerService;
    }
}
