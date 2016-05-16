package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.ProgramEnrollmentListResult;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Program;
import org.motechproject.openmrs19.domain.ProgramEnrollment;
import org.motechproject.openmrs19.resource.ProgramEnrollmentResource;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.List;

@Component
public class ProgramEnrollmentResourceImpl extends BaseResource implements ProgramEnrollmentResource {

    @Autowired
    protected ProgramEnrollmentResourceImpl(RestOperations restOperations) {
        super(restOperations);
    }

    @Override
    public ProgramEnrollment createProgramEnrollment(Config config, ProgramEnrollment programEnrollment) {
        String requestJson = buildGsonWithAdapters().toJson(programEnrollment);
        String responseJson = postForJson(config, requestJson, "/programenrollment");
        return (ProgramEnrollment) JsonUtils.readJson(responseJson, ProgramEnrollment.class);
    }

    @Override
    public ProgramEnrollment updateProgramEnrollment(Config config, ProgramEnrollment programEnrollment) {
        String requestJson = buildGsonWithAdapters().toJson(programEnrollment);
        String responseJson = postForJson(config, requestJson, "/programenrollment/{uuid}", programEnrollment.getUuid());
        return (ProgramEnrollment) JsonUtils.readJson(responseJson, ProgramEnrollment.class);
    }

    @Override
    public List<ProgramEnrollment> getProgramEnrollmentByPatientUuid(Config config, String patientUuid) {
        String responseJson = getJson(config, "/programenrollment?patient={uuid}&v=full", patientUuid);
        ProgramEnrollmentListResult programEnrollmentListResult = (ProgramEnrollmentListResult) JsonUtils.readJson(responseJson, ProgramEnrollmentListResult.class);
        return programEnrollmentListResult.getResults();
    }

    private Gson buildGsonWithAdapters() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(Patient.class, new Patient.PatientSerializer())
                .registerTypeAdapter(Program.class, new Program.ProgramSerializer())
                .registerTypeAdapter(Program.State.class, new Program.State.ProgramSerializer())
                .registerTypeAdapter(Location.class, new Location.LocationSerializer())
                .create();
    }
}
