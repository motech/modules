package org.motechproject.openmrs.resource.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Identifier;
import org.motechproject.openmrs.domain.IdentifierListResult;
import org.motechproject.openmrs.domain.IdentifierType;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.PatientIdentifierListResult;
import org.motechproject.openmrs.domain.PatientListResult;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.resource.PatientResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.List;

@Component
public class PatientResourceImpl extends BaseResource implements PatientResource {

    private String motechIdTypeUuid;
    private BiMap<String, String> identifierTypeUuidByName = HashBiMap.create();

    @Autowired
    public PatientResourceImpl(RestOperations restOperations) {
        super(restOperations);
    }

    @Override
    public Patient createPatient(Config config, Patient patient) {
        String requestJson = buildGsonWithAdapters(true).toJson(patient);
        String responseJson = postForJson(config, requestJson, "/patient");
        return (Patient) JsonUtils.readJson(responseJson, Patient.class);
    }

    @Override
    public PatientListResult queryForPatient(Config config, String motechId) {
        String responseJson = getJson(config, "/patient?q={motechId}", motechId);
        return (PatientListResult) JsonUtils.readJson(responseJson, PatientListResult.class);
    }

    @Override
    public Patient getPatientById(Config config, String patientId) {
        String responseJson = getJson(config, "/patient/{uuid}?v=full", patientId);
        return (Patient) JsonUtils.readJson(responseJson, Patient.class);
    }

    @Override
    public String getMotechPatientIdentifierUuid(Config config) {
        if (StringUtils.isNotEmpty(motechIdTypeUuid)) {
            return motechIdTypeUuid;
        }

        PatientIdentifierListResult result = getAllPatientIdentifierTypes(config);
        String motechPatientIdentifierTypeName = config.getMotechPatientIdentifierTypeName();
        for (IdentifierType type : result.getResults()) {
            if (motechPatientIdentifierTypeName.equals(type.getName())) {
                motechIdTypeUuid = type.getUuid();
                break;
            }
        }

        return motechIdTypeUuid;
    }

    @Override
    public String getPatientIdentifierTypeNameByUuid(Config config, String identifierTypeUuid) {
        // Firstly, we try to retrieve a name from the cache
        String identifierTypeName = identifierTypeUuidByName.get(identifierTypeUuid);

        if (identifierTypeName == null) {
            PatientIdentifierListResult result = getAllPatientIdentifierTypes(config);
            for (IdentifierType type : result.getResults()) {
                if (StringUtils.equals(identifierTypeUuid, type.getUuid())) {
                    if (isIdentifierTypeSupportedInMotech(config, type.getName())) {
                        identifierTypeName = type.getName();
                        // After retrieving an identifierType from an OpenMRS server, the uuid and name are stored in cache
                        identifierTypeUuidByName.put(identifierTypeUuid, identifierTypeName);
                    }

                    break;
                }
            }
        }

        return identifierTypeName;
    }

    @Override
    public String getPatientIdentifierTypeUuidByName(Config config, String identifierTypeName) {
        // Firstly, we try to retrieve a uuid from the cache
        String identifierTypeUuid = identifierTypeUuidByName.inverse().get(identifierTypeName);

        if (identifierTypeUuid == null) {
            PatientIdentifierListResult result = getAllPatientIdentifierTypes(config);
            for (IdentifierType type : result.getResults()) {
                if (StringUtils.equals(identifierTypeName, type.getName())) {
                    if (isIdentifierTypeSupportedInMotech(config, type.getName())) {
                        identifierTypeUuid = type.getUuid();
                        // After retrieving an identifierType from an OpenMRS server, the uuid and name are stored in cache
                        identifierTypeUuidByName.put(identifierTypeUuid, identifierTypeName);
                    }

                    break;
                }
            }
        }

        return identifierTypeUuid;
    }

    @Override
    public List<Identifier> getPatientIdentifierList(Config config, String patientUuid) {
        String responseJson = getJson(config, "/patient/{patientUuid}/identifier?v=full", patientUuid);
        return ((IdentifierListResult) JsonUtils.readJson(responseJson, IdentifierListResult.class)).getResults();
    }

    @Override
    public void deletePatient(Config config, String uuid) {
        delete(config, "/patient/{uuid}?purge", uuid);
    }

    @Override
    public void updatePatientMotechId(Config config, String patientUuid, String newMotechId) {
        List<Identifier> patientIdentifiers = getPatientIdentifierList(config, patientUuid);
        for (Identifier patientIdentifier : patientIdentifiers) {
            if (config.getMotechPatientIdentifierTypeName().equals(patientIdentifier.getIdentifierType().getName())) {
                patientIdentifier.setIdentifier(newMotechId);

                updatePatientIdentifier(config, patientUuid, patientIdentifier);
                break;
            }
        }
    }

    @Override
    public void updatePatientIdentifier(Config config, String patientUuid, Identifier identifier) {
        String requestJson = buildGson().toJson(identifier);
        postForJson(config, requestJson, "/patient/{patientUuid}/identifier/{identifierUuid}", patientUuid,
                identifier.getUuid());
    }

    @Override
    public void addPatientIdentifier(Config config, String patientUuid, Identifier identifier) {
        String requestJson = buildGson().toJson(identifier);
        postForJson(config, requestJson, "/patient/{patientUuid}/identifier/", patientUuid);
    }

    private PatientIdentifierListResult getAllPatientIdentifierTypes(Config config) {
        String responseJson = getJson(config, "/patientidentifiertype?v=full");
        return (PatientIdentifierListResult) JsonUtils.readJson(responseJson, PatientIdentifierListResult.class);
    }

    private boolean isIdentifierTypeSupportedInMotech(Config config, String identifierTypeName) {
        return config.getPatientIdentifierTypeNames().contains(identifierTypeName);
    }

    private Gson buildGsonWithAdapters(boolean excludeFieldsWithoutExposeAnnotation) {
        GsonBuilder builder = new GsonBuilder();

        if (excludeFieldsWithoutExposeAnnotation) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }

        builder.registerTypeAdapter(Person.class, new Person.PersonSerializer());
        builder.registerTypeAdapter(IdentifierType.class, new IdentifierType.IdentifierTypeSerializer());
        builder.registerTypeAdapter(Location.class, new Location.LocationSerializer());

        return builder.create();
    }

    private Gson buildGson() {
        return new GsonBuilder().create();
    }
}
