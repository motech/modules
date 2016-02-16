package org.motechproject.openmrs19.tasks.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.openmrs19.domain.OpenMRSEncounter;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSProvider;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.tasks.OpenMRSActionProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("openMRSActionProxyService")
public class OpenMRSActionProxyServiceImpl implements OpenMRSActionProxyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSActionProxyServiceImpl.class);

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
