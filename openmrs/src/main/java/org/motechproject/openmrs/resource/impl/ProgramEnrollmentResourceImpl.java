package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Program;
import org.motechproject.openmrs.domain.ProgramEnrollment;
import org.motechproject.openmrs.resource.ProgramEnrollmentResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

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
