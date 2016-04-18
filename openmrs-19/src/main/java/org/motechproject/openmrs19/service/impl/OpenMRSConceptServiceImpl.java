package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.ConceptListResult;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.ConceptResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
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

    @Override
    public String resolveConceptUuidFromConceptName(String name) {
        if (conceptCache.containsKey(name)) {
            return conceptCache.get(name);
        }

        ConceptListResult results;
        try {
            results = conceptResource.queryForConceptsByName(name);
        } catch (HttpException e) {
            throw new OpenMRSException("There was an error retrieving the uuid of the concept with concept name: " + name, e);
        }

        if (results.getResults().isEmpty()) {
            throw new OpenMRSException("Can't create an encounter because no concept was found with name: " + name);
        }

        for (Concept concept : results.getResults()) {
            if (concept.getDisplay().equals(name)) {
                conceptCache.put(name, concept.getUuid());
                return concept.getUuid();
            }
        }

        return null;
    }

    @Override
    public Concept createConcept(Concept concept) throws ConceptNameAlreadyInUseException {
        validateConceptForSave(concept);
        validateConceptNameUsage(concept);

        Concept created;
        try {
            created = conceptResource.createConcept(concept);
            conceptCache.put(created.getName().getName(), created.getUuid());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_CONCEPT_SUBJECT, EventHelper.conceptParameters(created)));

        } catch (HttpException e) {
            LOGGER.error("Failed to create a concept in OpenMRS with name: " + concept.getName());
            created = null;
        }

        return created;
    }

    @Override
    public Concept getConceptByUuid(String uuid) {
        Validate.notEmpty(uuid, "Concept Id cannot be empty");

        Concept concept;

        try {
            concept = conceptResource.getConceptById(uuid);
        } catch (HttpException e) {
            LOGGER.error("Failed to get concept with ID " + uuid);
            concept = null;
        }

        return concept;
    }

    @Override
    public List<Concept> search(String phrase) {
        Validate.notEmpty(phrase, "Name cannot be empty");

        List<Concept> concepts;

        try {
            concepts = conceptResource.queryForConceptsByName(phrase).getResults();
        } catch (HttpException e) {
            LOGGER.error("Failed search for concept: " + phrase);
            concepts = Collections.emptyList();
        }

        return concepts;
    }

    @Override
    public List<Concept> getAllConcepts() {
        List<Concept> concepts;

        try {
            concepts = conceptResource.getAllConcepts().getResults();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve all concepts");
            concepts = Collections.emptyList();
        }

        return concepts;
    }

    @Override
    public List<Concept> getConcepts(int page, int pageSize) {
        List<Concept> concepts;

        try {
            concepts = conceptResource.getConcepts(page, pageSize).getResults();
        } catch (HttpException e) {
            LOGGER.error("Error while fetching concepts with pagination!");
            concepts = Collections.emptyList();
        }

        return concepts;
    }

    @Override
    public void deleteConcept(String uuid) {
        try {
            Concept concept = conceptResource.getConceptById(uuid);
            conceptResource.deleteConcept(uuid);
            conceptCache.remove(concept.getName().getName());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_CONCEPT_SUBJECT, EventHelper.conceptParameters(concept)));
        } catch (HttpException e) {
            LOGGER.error("Failed to remove concept with ID " + uuid);
        }
    }

    @Override
    public Concept updateConcept(Concept concept) {
        validateConceptForUpdate(concept);
        Concept updatedConcept;

        try {
            updatedConcept = conceptResource.updateConcept(concept);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_CONCEPT_SUBJECT, EventHelper.conceptParameters(updatedConcept)));
        } catch (HttpException e) {
            LOGGER.error("Failed to update concept with name " + concept.getName());
            updatedConcept = null;
        }

        return updatedConcept;
    }

    private void validateConceptForSave(Concept concept) {
        Validate.notNull(concept, "Concept cannot be null");
    }

    private void validateConceptForUpdate(Concept concept) {
        Validate.notNull(concept);
        Validate.notNull(concept.getConceptClass());
        Validate.notNull(concept.getDatatype());
    }

    private void validateConceptNameUsage(Concept concept) throws ConceptNameAlreadyInUseException {
        List<Concept> concepts = search(concept.getNames().get(0).getName());

        for (Concept existingConcept : concepts) {
            if (existingConcept.getDisplay().equals(concept.getNames().get(0).getName())) {
                throw new ConceptNameAlreadyInUseException(String.format("Name \"%s\" already in use!", concept.getNames().get(0).getName()));
            }
        }
    }
}
