package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang.Validate;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.ConceptListResult;
import org.motechproject.openmrs.resource.ConceptResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.List;

@Component
public class ConceptResourceImpl extends BaseResource implements ConceptResource {

    private static final String GET_CONCEPTS_PATH = "/concept?v=full&limit={pageSize}&startIndex={startIndex}";

    @Autowired
    public ConceptResourceImpl(RestOperations restOperations, HttpClient httpClient) {
        super(restOperations, httpClient);
    }

    @Override
    public ConceptListResult queryForConceptsByName(Config config, String name) {
        String responseJson = getJson(config, "/concept?v=full&q={conceptName}", name);
        return (ConceptListResult) JsonUtils.readJson(responseJson, ConceptListResult.class);
    }

    @Override
    public Concept getConceptByName(Config config, String name) {
        List<Concept> concepts = queryForConceptsByName(config, name).getResults();

        for (Concept concept : concepts) {
            if (concept.getDisplay().equals(name)) {
                return concept;
            }
        }

        return null;
    }

    @Override
    public Concept getConceptById(Config config, String uuid) {
        String responseJson = getJson(config, "/concept/{uuid}", uuid);
        return (Concept) JsonUtils.readJson(responseJson, Concept.class);
    }

    @Override
    public Concept createConcept(Config config, Concept concept) {
        String requestJson = buildGson(false).toJson(concept);
        String responseJson = postForJson(config, requestJson, "/concept");
        return (Concept) JsonUtils.readJson(responseJson, Concept.class);
    }

    @Override
    public ConceptListResult getAllConcepts(Config config) {
        String responseJson = getJson(config, "/concept?v=full");
        return (ConceptListResult) JsonUtils.readJson(responseJson, ConceptListResult.class);
    }

    @Override
    public Concept updateConcept(Config config, Concept concept) {
        String requestJson = buildGson(true).toJson(concept);
        String responseJson = postForJson(config, requestJson, "/concept/{uuid}", concept.getUuid());
        return (Concept) JsonUtils.readJson(responseJson, Concept.class);
    }

    @Override
    public void deleteConcept(Config config, String uuid) {
        delete(config, "/concept/{uuid}?purge", uuid);
    }

    @Override
    public ConceptListResult getConcepts(Config config, int page, int pageSize) {
        Validate.isTrue(page > 0, "Page number must be a positive value!");
        Validate.isTrue(pageSize > 0, "Page size must be a positive value!");

        String json = getJson(config, GET_CONCEPTS_PATH, pageSize, (page - 1) * pageSize);

        return (ConceptListResult) JsonUtils.readJson(json, ConceptListResult.class);
    }

    private Gson buildGson(boolean excludeFieldsWithoutExposeAnnotation) {
        GsonBuilder builder = new GsonBuilder();

        if (excludeFieldsWithoutExposeAnnotation) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }

        builder.registerTypeAdapter(Concept.DataType.class, new Concept.DataTypeSerializer());
        builder.registerTypeAdapter(Concept.ConceptClass.class, new Concept.ConceptClassSerializer());
        return builder.create();
    }
}
