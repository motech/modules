package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Attribute;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Program;
import org.motechproject.openmrs.domain.ProgramEnrollment;
import org.motechproject.openmrs.domain.ProgramEnrollmentListResult;
import org.motechproject.openmrs.resource.ProgramEnrollmentResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProgramEnrollmentResourceImpl extends BaseResource implements ProgramEnrollmentResource {

    @Autowired
    protected ProgramEnrollmentResourceImpl(RestOperations restOperations, HttpClient httpClient) {
        super(restOperations, httpClient);
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

    @Override
    public void deleteProgramEnrollment(Config config, String uuid) {
        delete(config, "/programenrollment/{uuid}?purge", uuid);
    }

    @Override
    public ProgramEnrollment createBahmniProgramEnrollment(Config config, ProgramEnrollment programEnrollment) {
        String requestJson = buildGsonWithAdapters().toJson(programEnrollment);
        String responseJson = postForJson(config, requestJson, "/bahmniprogramenrollment");
        return (ProgramEnrollment) JsonUtils.readJson(responseJson, ProgramEnrollment.class);
    }

    @Override
    public ProgramEnrollment updateBahmniProgramEnrollment(Config config, ProgramEnrollment programEnrollment) {
        String requestJson = buildGsonWithAdapters().toJson(programEnrollment);
        String responseJson = postForJson(config, requestJson, "/bahmniprogramenrollment/{uuid}", programEnrollment.getUuid());
        return (ProgramEnrollment) JsonUtils.readJson(responseJson, ProgramEnrollment.class);
    }

    @Override
    public List<ProgramEnrollment> getBahmniProgramEnrollmentByPatientUuid(Config config, String patientUuid) {
        String responseJson = getJson(config, "/bahmniprogramenrollment?patient={uuid}&v=full", patientUuid);
        Map<Type, Object> attributeAdapter = new HashMap<Type, Object>();
        attributeAdapter.put(Attribute.class, new Attribute.AttributeSerializer());
        ProgramEnrollmentListResult programEnrollmentListResult = (ProgramEnrollmentListResult) JsonUtils.readJsonWithAdapters(responseJson, ProgramEnrollmentListResult.class, attributeAdapter);
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
                .registerTypeAdapter(Attribute.class, new Attribute.AttributeSerializer())
                .create();
    }
}
