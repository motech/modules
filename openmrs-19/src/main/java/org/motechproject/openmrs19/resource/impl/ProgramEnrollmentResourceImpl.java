package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.domain.Program;
import org.motechproject.openmrs19.domain.ProgramEnrollment;
import org.motechproject.openmrs19.resource.ProgramEnrollmentResource;
import org.motechproject.openmrs19.util.JsonUtils;
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

    private Gson buildGsonWithAdapters() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(Patient.class, new Patient.PatientSerializer())
                .registerTypeAdapter(Program.class, new Program.ProgramSerializer())
                .create();
    }
}
