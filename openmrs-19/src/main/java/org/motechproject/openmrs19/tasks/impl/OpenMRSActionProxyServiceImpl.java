package org.motechproject.openmrs19.tasks.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
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

    public static final String DEFAULT_LOCATION_NAME = "Unknown Location";

    private OpenMRSFacilityService facilityService;
    private OpenMRSPatientService patientService;

    @Override
    public void createPatient(String firstName, String middleName, String lastName, String address, DateTime dateOfBirth,
                              Boolean birthDateEstimated, String gender, Boolean dead, String motechId, String locationName,
                              Map<String, String> identifiers) {
        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName(firstName);
        person.setMiddleName(middleName);
        person.setLastName(lastName);
        person.setGender(gender);
        person.setAddress(address);
        person.setDateOfBirth(dateOfBirth);
        person.setBirthDateEstimated(birthDateEstimated);
        person.setDead(dead);

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
    public void setFacilityService(OpenMRSFacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @Autowired
    public void setPatientService(OpenMRSPatientService patientService) {
        this.patientService = patientService;
    }
}
