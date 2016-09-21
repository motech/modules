package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Visit;
import org.motechproject.openmrs.domain.VisitType;
import org.motechproject.openmrs.resource.VisitResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class VisitResourceImpl extends BaseResource implements VisitResource {

    @Autowired
    public VisitResourceImpl(RestOperations restOperations, HttpClient httpClient) {
        super(restOperations, httpClient);
    }

    @Override
    public Visit createVisit(Config config, Visit visit) {
        String requestJson = buildGsonWithAdaptersSerialize().toJson(visit);
        String responseJson = postForJson(config, requestJson, "/visit");
        return (Visit) JsonUtils.readJson(responseJson, Visit.class);
    }

    @Override
    public Visit getVisitById(Config config, String uuid) {
        String responseJson = getJson(config, "/visit/{uuid}", uuid);
        return (Visit) JsonUtils.readJson(responseJson, Visit.class);
    }

    @Override
    public void deleteVisit(Config config, String uuid) {
        delete(config, "/visit/{uuid}?purge", uuid);
    }

    @Override
    public VisitType createVisitType(Config config, VisitType visitType) {
        String requestJson = buildGson().toJson(visitType, VisitType.class);
        String responseJson = postForJson(config, requestJson, "/visittype?v=full");
        return (VisitType) JsonUtils.readJson(responseJson, VisitType.class);
    }

    @Override
    public VisitType getVisitTypeById(Config config, String uuid) {
        String responseJson = getJson(config, "/visittype/{uuid}", uuid);
        return (VisitType) JsonUtils.readJson(responseJson, VisitType.class);
    }

    @Override
    public void deleteVisitType(Config config, String uuid) {
        delete(config, "/visittype/{uuid}?purge", uuid);
    }

    private Gson buildGson() {
        return new GsonBuilder().create();
    }

    private Gson buildGsonWithAdaptersSerialize() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(VisitType.class, new VisitType.VisitTypeSerializer())
                .registerTypeAdapter(Patient.class, new Patient.PatientSerializer())
                .registerTypeAdapter(Location.class, new Location.LocationSerializer())
                .create();
    }
}
