package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.EventKeys;
import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSConceptName;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.ConceptResource;
import org.motechproject.openmrs19.resource.model.Concept;
import org.motechproject.openmrs19.resource.model.ConceptListResult;
import org.motechproject.openmrs19.rest.HttpException;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
            LOGGER.error("There was an error retrieving the uuid of the concept with concept name: " + conceptName);
            throw new OpenMRSException(e);
        }

        if (results.getResults().isEmpty()) {
            LOGGER.error("Could not find a concept with name: " + conceptName);
            throw new OpenMRSException("Can't create an encounter because no concept was found with name: " + conceptName);
        }

        Concept concept = results.getResults().get(0);
        conceptCache.put(conceptName, concept.getUuid());
        return concept.getUuid();
    }

    @Override
    public OpenMRSConcept saveConcept(OpenMRSConcept concept) {
        validateConceptBeforeSave(concept);
        Concept converted = fromMrsConcept(concept);
        Concept created;
        try {
            created = conceptResource.createConcept(converted);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_CONCEPT_SUBJECT, EventHelper.conceptParameters(concept)));
        } catch (HttpException e) {
            LOGGER.error("Failed to create a concept in OpenMRS with name: " + concept.getName());
            return null;
        }

        return new OpenMRSConcept(new OpenMRSConceptName(created.getName().getName()));
    }

    private Concept fromMrsConcept(OpenMRSConcept concept) {
        Concept converted = new Concept();
        converted.setName(new Concept.ConceptName());
        converted.getName().setName(concept.getName().getName());

        return converted;
    }

    private void validateConceptBeforeSave(OpenMRSConcept concept) {
        Validate.notNull(concept, "Concept cannot be null");
        Validate.notNull(concept.getName(), "Concept name cannot be null");
    }

    @Override
    public OpenMRSConcept getConcept(String conceptId) {
        Validate.notEmpty(conceptId, "Concept Id cannot be empty");

        Concept concept;
        try {
            concept = conceptResource.getConceptById(conceptId);
        } catch (HttpException e) {
            LOGGER.error("Failed to get patient by id: " + conceptId);
            return null;
        }

        return convertToMrsConcept(concept);
    }

    private OpenMRSConcept convertToMrsConcept(Concept concept) {
        return new OpenMRSConcept(new OpenMRSConceptName(concept.getName().getName()));
    }

    @Override
    public List<OpenMRSConcept> search(String name) {
        Validate.notEmpty(name, "Name cannot be empty");

        ConceptListResult result;
        try {
            result = conceptResource.queryForConceptsByName(name);
        } catch (HttpException e) {
            LOGGER.error("Failed search for concept: " + name);
            return Collections.emptyList();
        }

        List<OpenMRSConcept> searchResults = new ArrayList<>();

        for (Concept partialConcept : result.getResults()) {
            OpenMRSConcept concept = getConcept(partialConcept.getUuid());
            searchResults.add(concept);
        }

        if (searchResults.size() > 0) {
            sortResults(searchResults);
        }

        return searchResults;
    }

    private void sortResults(List<OpenMRSConcept> searchResults) {
        Collections.sort(searchResults, new Comparator<OpenMRSConcept>() {
            @Override
            public int compare(OpenMRSConcept concept1, OpenMRSConcept concept2) {
                if (StringUtils.isNotEmpty(concept1.getName().getName()) && StringUtils.isNotEmpty(concept2.getName().getName())) {
                    return concept1.getName().getName().compareTo(concept2.getName().getName());
                } else if (StringUtils.isNotEmpty(concept1.getName().getName())) {
                    return -1;
                } else if (StringUtils.isNotEmpty(concept2.getName().getName())) {
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public List<OpenMRSConcept> getAllConcepts() {
        ConceptListResult result;
        try {
            result = conceptResource.getAllConcepts();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve all concepts");
            return Collections.emptyList();
        }

        return toMrsConcept(result.getResults());
    }

    @Override
    public void deleteConcept(String conceptId) {
        try {
            OpenMRSConcept concept = convertToMrsConcept(conceptResource.getConceptById(conceptId));
            conceptResource.deleteConcept(conceptId);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_CONCEPT_SUBJECT, EventHelper.conceptParameters(concept)));
        } catch (HttpException e) {
            LOGGER.error("Failed to remove concept for: " + conceptId);
        }
    }

    @Override
    public OpenMRSConcept updateConcept(OpenMRSConcept concept) {
        Validate.notNull(concept, "Concept cannot be null");

        OpenMRSConcept updatedConcept;
        OpenMRSConcept openMRSConcept = new OpenMRSConcept(concept.getName());

        try {
            conceptResource.updateConcept(fromMrsConcept(concept));
            updatedConcept = new OpenMRSConcept(openMRSConcept.getName());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_CONCEPT_SUBJECT, EventHelper.conceptParameters(updatedConcept)));
        } catch (HttpException e) {
            LOGGER.error("Failed to update concept with name " + concept.getName());
            return null;
        }
        return updatedConcept;
    }

    private List<OpenMRSConcept> toMrsConcept(List<Concept> results) {
        List<OpenMRSConcept> mrsConcepts = new ArrayList<>();
        for (Concept concept : results) {
            mrsConcepts.add(convertToMrsConcept(concept));
        }

        return mrsConcepts;
    }
}
