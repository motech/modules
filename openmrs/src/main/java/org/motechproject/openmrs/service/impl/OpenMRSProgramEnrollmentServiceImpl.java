package org.motechproject.openmrs.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.ProgramEnrollment;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.resource.ProgramEnrollmentResource;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.motechproject.openmrs.service.OpenMRSProgramEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("programEnrollmentService")
public class OpenMRSProgramEnrollmentServiceImpl implements OpenMRSProgramEnrollmentService {

    private final OpenMRSConfigService configService;

    private final OpenMRSPatientService patientService;

    private final ProgramEnrollmentResource programEnrollmentResource;

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSProgramEnrollmentServiceImpl(OpenMRSConfigService configService, OpenMRSPatientService patientService,
                                               ProgramEnrollmentResource programEnrollmentResource, EventRelay eventRelay) {
        this.configService = configService;
        this.patientService = patientService;
        this.programEnrollmentResource = programEnrollmentResource;
        this.eventRelay = eventRelay;
    }

    @Override
    public ProgramEnrollment createProgramEnrollment(String configName, ProgramEnrollment programEnrollment) {
        validateProgramEnrollment(programEnrollment);

        try {
            Config config = configService.getConfigByName(configName);

            ProgramEnrollment created;
            if (programEnrollment.getAttributes() == null) {
                created = programEnrollmentResource.createProgramEnrollment(config, programEnrollment);
            } else {
                created = programEnrollmentResource.createBahmniProgramEnrollment(config, programEnrollment);
            }

            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_PROGRAM_ENROLLMENT, EventHelper.programEnrollmentParameters(created)));
            return created;
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Could not create program enrollment for patient with uuid: "
                    + programEnrollment.getPatient().getUuid(), e);
        }
    }

    @Override
    public ProgramEnrollment updateProgramEnrollment(String configName, ProgramEnrollment programEnrollment) {
        ProgramEnrollment updatedProgramEnrollment = null;
        validateProgramEnrollmentToUpdate(programEnrollment);

        try {
            Config config = configService.getConfigByName(configName);

            if (CollectionUtils.isNotEmpty(programEnrollment.getAttributes())) {
                updatedProgramEnrollment = programEnrollmentResource.updateBahmniProgramEnrollment(config, programEnrollment);
            } else {
                updatedProgramEnrollment = programEnrollmentResource.updateProgramEnrollment(config, programEnrollment);
            }
            return updatedProgramEnrollment;
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Could not update program enrollment with uuid: " + programEnrollment.getUuid(), e);
        }
    }

    @Override
    public List<ProgramEnrollment> getBahmniProgramEnrollmentByPatientUuid(String configName, String patientUuid)  {
        return programEnrollmentResource.getBahmniProgramEnrollmentByPatientUuid(configService.getConfigByName(configName), patientUuid);
    }

    @Override
    public List<ProgramEnrollment> getBahmniProgramEnrollmentByPatientMotechId(String configName, String patientMotechId) {
        Patient patient = patientService.getPatientByMotechId(configName, patientMotechId);

        return Objects.nonNull(patient) ? getBahmniProgramEnrollmentByPatientUuid(configName, patient.getUuid()) : new ArrayList<>();
    }

    @Override
    public List<ProgramEnrollment> getProgramEnrollmentByPatientUuid(String configName, String patientUuid) {
        return programEnrollmentResource.getProgramEnrollmentByPatientUuid(configService.getConfigByName(configName), patientUuid);
    }

    @Override
    public List<ProgramEnrollment> getProgramEnrollmentByPatientMotechId(String configName, String patientMotechId) {
        Patient patient = patientService.getPatientByMotechId(configName, patientMotechId);

        return Objects.nonNull(patient) ? getProgramEnrollmentByPatientUuid(configName, patient.getUuid()) : new ArrayList<>();
    }

    @Override
    public void deleteProgramEnrollment(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            programEnrollmentResource.deleteProgramEnrollment(config, uuid);
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Could not delete program enrollment with uuid: " + uuid, e);
        }
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
