package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.ConceptResource;
import org.motechproject.openmrs19.resource.model.Concept;
import org.motechproject.openmrs19.resource.model.ConceptListResult;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("conceptService")
public class OpenMRSConceptServiceImpl implements OpenMRSConceptService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSConceptServiceImpl.class);

    private final Map<String, String> conceptCache = new HashMap<>();
    private final ConceptResource conceptResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSConceptServiceImpl(ConceptResource conceptResource, EventRelay eventRelay) {
        this.conceptResource = conceptResource;
        this.eventRelay = eventRelay;
    }

    public String resolveConceptUuidFromConceptName(String conceptName) {
        if (conceptCache.containsKey(conceptName)) {
            return conceptCache.get(conceptName);
        }

        ConceptListResult results;
        try {
            results = conceptResource.queryForConceptsByName(conceptName);
        } catch (HttpException e) {
            throw new OpenMRSException("There was an error retrieving the uuid of the concept with concept name: " + conceptName, e);
        }

        if (results.getResults().isEmpty()) {
            throw new OpenMRSException("Can't create an encounter because no concept was found with name: " + conceptName);
        }

        for (Concept concept : results.getResults()) {
            if (concept.getDisplay().equals(conceptName)) {
                conceptCache.put(conceptName, concept.getUuid());
                return concept.getUuid();
            }
        }

        return null;
    }

    @Override
    public OpenMRSConcept createConcept(OpenMRSConcept concept) throws ConceptNameAlreadyInUseException {

        validateConceptForSave(concept);
        validateConceptNameUsage(concept);

        Concept converted = ConverterUtils.toConcept(concept);

        OpenMRSConcept created;

        try {

            created = ConverterUtils.toOpenMRSConcept(conceptResource.createConcept(converted));
            conceptCache.put(created.getName().getName(), created.getUuid());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_CONCEPT_SUBJECT, EventHelper.conceptParameters(created)));

        } catch (HttpException e) {
            LOGGER.error("Failed to create a concept in OpenMRS with name: " + concept.getName());
            created = null;
        }

        return created;
    }

    @Override
    public OpenMRSConcept getConceptById(String uuid) {

        Validate.notEmpty(uuid, "Concept Id cannot be empty");

        OpenMRSConcept concept;

        try {

            concept = ConverterUtils.toOpenMRSConcept(conceptResource.getConceptById(uuid));

        } catch (HttpException e) {
            LOGGER.error("Failed to get concept with ID " + uuid);
            concept = null;
        }

        return concept;
    }

    @Override
    public List<OpenMRSConcept> search(String name) {

        Validate.notEmpty(name, "Name cannot be empty");

        List<OpenMRSConcept> concepts;

        try {

            concepts = ConverterUtils.toOpenMRSConcepts(conceptResource.queryForConceptsByName(name));

        } catch (HttpException e) {
            LOGGER.error("Failed search for concept: " + name);
            concepts = Collections.emptyList();
        }

        return concepts;
    }

    @Override
    public List<OpenMRSConcept> getAllConcepts() {

        List<OpenMRSConcept> concepts;

        try {

            concepts = ConverterUtils.toOpenMRSConcepts(conceptResource.getAllConcepts());

        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve all concepts");
            concepts = Collections.emptyList();
        }

        return concepts;
    }

    public List<OpenMRSConcept> getConcepts(int page, int pageSize) {

        List<OpenMRSConcept> concepts;

        try {

            concepts = ConverterUtils.toOpenMRSConcepts(conceptResource.getConcepts(page, pageSize));

        } catch (HttpException e) {
            LOGGER.error("Error while fetching concepts with pagination!");
            concepts = Collections.emptyList();
        }

        return concepts;
    }

    @Override
    public void deleteConcept(String uuid) {

        try {

            OpenMRSConcept concept = ConverterUtils.toOpenMRSConcept(conceptResource.getConceptById(uuid));
            conceptResource.deleteConcept(uuid);
            conceptCache.remove(concept.getName().getName());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_CONCEPT_SUBJECT, EventHelper.conceptParameters(concept)));

        } catch (HttpException e) {
            LOGGER.error("Failed to remove concept with ID " + uuid);
        }
    }

    @Override
    public OpenMRSConcept updateConcept(OpenMRSConcept openMRSConcept) {

        validateConceptForUpdate(openMRSConcept);

        OpenMRSConcept updatedConcept;

        Concept concept = ConverterUtils.toConcept(openMRSConcept);
        concept.setName(null);
        concept.setDisplay(null);
        concept.setNames(null);

        try {

            updatedConcept = ConverterUtils.toOpenMRSConcept(conceptResource.updateConcept(concept));
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_CONCEPT_SUBJECT, EventHelper.conceptParameters(updatedConcept)));

        } catch (HttpException e) {
            LOGGER.error("Failed to update concept with name " + openMRSConcept.getName());
            updatedConcept = null;
        }

        return updatedConcept;
    }

    private void validateConceptForSave(OpenMRSConcept concept) {
        Validate.notNull(concept, "Concept cannot be null");
    }

    private void validateConceptForUpdate(OpenMRSConcept concept) {
        Validate.notNull(concept);
        Validate.notNull(concept.getConceptClass());
        Validate.notNull(concept.getDataType());
    }

    private void validateConceptNameUsage(OpenMRSConcept concept) throws ConceptNameAlreadyInUseException {
        List<OpenMRSConcept> concepts = search(concept.getNames().get(0).getName());

        for (OpenMRSConcept existingConcept : concepts) {
            if (existingConcept.getDisplay().equals(concept.getNames().get(0).getName())) {
                throw new ConceptNameAlreadyInUseException(String.format("Name \"%s\" already in use!", concept.getNames().get(0).getName()));
            }
        }

    }
}
