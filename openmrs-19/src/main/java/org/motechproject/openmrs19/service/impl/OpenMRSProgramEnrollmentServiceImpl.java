package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.ProgramEnrollment;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.PatientResource;
import org.motechproject.openmrs19.resource.ProgramEnrollmentResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.service.OpenMRSProgramEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service("programEnrollmentService")
public class OpenMRSProgramEnrollmentServiceImpl implements OpenMRSProgramEnrollmentService {

    private final OpenMRSConfigService configService;

    private final PatientResource patientResource;

    private final ProgramEnrollmentResource programEnrollmentResource;

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSProgramEnrollmentServiceImpl(OpenMRSConfigService configService, PatientResource patientResource,
                                               ProgramEnrollmentResource programEnrollmentResource, EventRelay eventRelay) {
        this.configService = configService;
        this.patientResource = patientResource;
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
            throw new OpenMRSException("Could not create program enrollment for patient with uuid: "
                    + programEnrollment.getPatient().getUuid(), e);
        }
    }

    @Override
    public ProgramEnrollment updateProgramEnrollment(String configName, ProgramEnrollment programEnrollment) {
        validateProgramEnrollmentToUpdate(programEnrollment);

        try {
            Config config = configService.getConfigByName(configName);
            return programEnrollmentResource.updateProgramEnrollment(config, programEnrollment);
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Could not update program enrollment with uuid: " + programEnrollment.getUuid(), e);
        }
    }

    @Override
    public List<ProgramEnrollment> getProgramEnrollmentByPatientUuid(String configName, String patientUuid) {
        return programEnrollmentResource.getProgramEnrollmentByPatientUuid(configService.getConfigByName(configName), patientUuid);
    }

    @Override
    public List<ProgramEnrollment> getProgramEnrollmentByPatientMotechId(String configName, String patientMotechId) {
        Config config = configService.getConfigByName(configName);
        Patient patient = patientResource.queryForPatient(config, patientMotechId).getResults().get(0);

        return programEnrollmentResource.getProgramEnrollmentByPatientUuid(config, patient.getUuid());
    }

    private void validateProgramEnrollment(ProgramEnrollment programEnrollment) {
        Validate.notNull(programEnrollment, "Program Enrollment cannot be null");
        Validate.notNull(programEnrollment.getPatient(), "Patient cannot be null");
        Validate.notNull(programEnrollment.getProgram(), "Program cannot be null");
        Validate.notNull(programEnrollment.getDateEnrolled(), "Enrolled date cannot be null");
    }

    private void validateProgramEnrollmentToUpdate(ProgramEnrollment programEnrollment) {
        Validate.notNull(programEnrollment, "Program Enrollment cannot be null");
        Validate.notEmpty(programEnrollment.getUuid(), "Program Enrollment UUID cannot be null");
    }
}
