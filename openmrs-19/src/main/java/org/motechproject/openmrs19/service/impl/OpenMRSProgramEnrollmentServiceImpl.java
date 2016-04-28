package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.ProgramEnrollment;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.ProgramEnrollmentResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.service.OpenMRSProgramEnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service("programEnrollmentService")
public class OpenMRSProgramEnrollmentServiceImpl implements OpenMRSProgramEnrollmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSProgramEnrollmentServiceImpl.class);

    private final OpenMRSConfigService configService;

    private final ProgramEnrollmentResource programEnrollmentResource;

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSProgramEnrollmentServiceImpl(OpenMRSConfigService configService, ProgramEnrollmentResource programEnrollmentResource,
                                               EventRelay eventRelay) {
        this.configService = configService;
        this.programEnrollmentResource = programEnrollmentResource;
        this.eventRelay = eventRelay;
    }

    @Override
    public ProgramEnrollment createProgramEnrollment(String configName, ProgramEnrollment programEnrollment) {
        validateProgramEnrollment(programEnrollment);

        try {
            Config config = configService.getConfigByName(configName);
            ProgramEnrollment created = programEnrollmentResource.createProgramEnrollment(config, programEnrollment);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_PROGRAM_ENROLLMENT, EventHelper.programEnrollmentParameters(created)));
            return created;
        } catch (HttpClientErrorException e) {
            LOGGER.error("Could not create program enrollment: " + e.getMessage());
            return null;
        }
    }

    private void validateProgramEnrollment(ProgramEnrollment programEnrollment) {
        Validate.notNull(programEnrollment, "Program Enrollment cannot be null");
        Validate.notNull(programEnrollment.getPatient(), "Patient cannot be null");
        Validate.notNull(programEnrollment.getProgram(), "Program cannot be null");
        Validate.notNull(programEnrollment.getDateEnrolled(), "Enrolled date cannot be null");
    }
}
