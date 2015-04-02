package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.Validate;
import org.motechproject.openmrs19.OpenMrsInstance;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.ConceptResource;
import org.motechproject.openmrs19.resource.model.Concept;
import org.motechproject.openmrs19.resource.model.ConceptListResult;
import org.motechproject.openmrs19.rest.RestClient;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConceptResourceImpl implements ConceptResource {

    private final OpenMrsInstance openmrsInstance;
    private final RestClient restClient;

    @Autowired
    public ConceptResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public ConceptListResult queryForConceptsByName(String name) throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/concept?v=full&q={conceptName}",
                name));
        return (ConceptListResult) JsonUtils.readJson(responseJson, ConceptListResult.class);
    }

    @Override
    public Concept getConceptByName(String name) throws HttpException {
        List<Concept> concepts = queryForConceptsByName(name).getResults();
        for (Concept concept : concepts) {
            if (concept.getDisplay().equals(name)) {
                return concept;
            }
        }
        return null;
    }

    @Override
    public Concept getConceptById(String uuid) throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/concept/{uuid}",
                uuid));
        return (Concept) JsonUtils.readJson(responseJson, Concept.class);
    }

    @Override
    public Concept createConcept(Concept concept) throws HttpException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Concept.DataType.class, new Concept.DataTypeSerializer())
                .registerTypeAdapter(Concept.ConceptClass.class, new Concept.ConceptClassSerializer())
                .create();

        String requestJson = gson.toJson(concept);
        String responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/concept"), requestJson);

        return (Concept) JsonUtils.readJson(responseJson, Concept.class);
    }

    @Override
    public ConceptListResult getAllConcepts() throws HttpException {
        String json = restClient.getJson(openmrsInstance.toInstancePath("/concept?v=full"));
        return (ConceptListResult) JsonUtils.readJson(json, ConceptListResult.class);
    }

    @Override
    public Concept updateConcept(Concept concept) throws HttpException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Concept.DataType.class, new Concept.DataTypeSerializer())
                .registerTypeAdapter(Concept.ConceptClass.class, new Concept.ConceptClassSerializer())
                .create();
        // uuid cannot be set on an update call
        String uuid = concept.getUuid();
        concept.setUuid(null);
        String jsonRequest = gson.toJson(concept);
        String json = restClient.postForJson(openmrsInstance.toInstancePathWithParams("/concept/{uuid}", uuid),
                jsonRequest);

        return (Concept) JsonUtils.readJson(json, Concept.class);
    }

    @Override
    public void deleteConcept(String uuid) throws HttpException {
        restClient.delete(openmrsInstance.toInstancePathWithParams("/concept/{uuid}?purge", uuid));
    }

    @Override
    public ConceptListResult getConcepts(int page, int pageSize) throws HttpException {

        Validate.isTrue(page > 0, "Page number must be a positive value!");
        Validate.isTrue(pageSize > 0, "Page size must be a positive value!");

        int startIndex = (page - 1) * pageSize;
        String json = restClient.getJson(openmrsInstance
                .toInstancePathWithParams("/concept?v=full&limit={pageSize}&startIndex={startIndex}", pageSize, startIndex));
        return (ConceptListResult) JsonUtils.readJson(json, ConceptListResult.class);
    }

}
