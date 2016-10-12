package org.motechproject.openmrs.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.ConceptListResult;
import org.motechproject.openmrs.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.resource.ConceptResource;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConceptService;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("conceptService")
public class OpenMRSConceptServiceImpl implements OpenMRSConceptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSConceptServiceImpl.class);

    private final OpenMRSConfigService configService;

    private final ConceptResource conceptResource;

    private final EventRelay eventRelay;

    private final Map<String, String> conceptCache = new HashMap<>();

    @Autowired
    public OpenMRSConceptServiceImpl(ConceptResource conceptResource, EventRelay eventRelay,
                                     OpenMRSConfigService configService) {
        this.conceptResource = conceptResource;
        this.configService = configService;
        this.eventRelay = eventRelay;
    }

    @Override
    public String resolveConceptUuidFromConceptName(String configName, String name) {
        if (conceptCache.containsKey(name)) {
            return conceptCache.get(name);
        }

        ConceptListResult results;
        try {
            Config config = configService.getConfigByName(configName);
            results = conceptResource.queryForConceptsByName(config, name);
        } catch (HttpClientErrorException e) {
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
    public Concept createConcept(String configName, Concept concept) throws ConceptNameAlreadyInUseException {
        Config config = configService.getConfigByName(configName);

        validateConceptForSave(concept);
        validateConceptNameUsage(config, concept);

        Concept created;
        try {
            created = conceptResource.createConcept(config, concept);
            conceptCache.put(created.getName().getName(), created.getUuid());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_CONCEPT_SUBJECT, EventHelper.conceptParameters(created)));

        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to create a concept in OpenMRS with name: " + concept.getName());
            created = null;
        }

        return created;
    }

    @Override
    public Concept getConceptByUuid(String configName, String uuid) {
        Validate.notEmpty(uuid, "Concept Id cannot be empty");

        Concept concept;

        try {
            Config config = configService.getConfigByName(configName);
            concept = conceptResource.getConceptById(config, uuid);
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not get Concept for uuid: %s. %s %s", uuid, e.getMessage(), e.getResponseBodyAsString()), e);
        }

        return concept;
    }

    @Override
    public List<Concept> search(String configName, String phrase) {
        return search(configService.getConfigByName(configName), phrase);
    }

    @Override
    public List<Concept> getAllConcepts(String configName) {
        List<Concept> concepts;

        try {
            Config config = configService.getConfigByName(configName);
            concepts = conceptResource.getAllConcepts(config).getResults();
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to retrieve all concepts");
            concepts = Collections.emptyList();
        }

        return concepts;
    }

    @Override
    public List<Concept> getConcepts(String configName, int page, int pageSize) {
        List<Concept> concepts;

        try {
            Config config = configService.getConfigByName(configName);
            concepts = conceptResource.getConcepts(config, page, pageSize).getResults();
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching concepts with pagination!");
            concepts = Collections.emptyList();
        }

        return concepts;
    }

    @Override
    public void deleteConcept(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            Concept concept = conceptResource.getConceptById(config, uuid);
            conceptResource.deleteConcept(config, uuid);
            conceptCache.remove(concept.getName().getName());
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_CONCEPT_SUBJECT, EventHelper.conceptParameters(concept)));
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to remove concept with ID " + uuid);
        }
    }

    @Override
    public Concept updateConcept(String configName, Concept concept) {
        validateConceptForUpdate(concept);
        Concept updatedConcept;

        try {
            Config config = configService.getConfigByName(configName);
            updatedConcept = conceptResource.updateConcept(config, concept);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_CONCEPT_SUBJECT, EventHelper.conceptParameters(updatedConcept)));
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to update concept with name " + concept.getName());
            updatedConcept = null;
        }

        return updatedConcept;
    }

    private List<Concept> search(Config config, String phrase) {
        Validate.notEmpty(phrase, "Name cannot be empty");

        List<Concept> concepts;

        try {
            concepts = conceptResource.queryForConceptsByName(config, phrase).getResults();
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed search for concept: " + phrase);
            concepts = Collections.emptyList();
        }

        return concepts;
    }

    private void validateConceptForSave(Concept concept) {
        Validate.notNull(concept, "Concept cannot be null");
    }

    private void validateConceptForUpdate(Concept concept) {
        Validate.notNull(concept);
        Validate.notNull(concept.getConceptClass());
        Validate.notNull(concept.getDatatype());
    }

    private void validateConceptNameUsage(Config config, Concept concept) throws ConceptNameAlreadyInUseException {
        List<Concept> concepts = search(config, concept.getNames().get(0).getName());

        for (Concept existingConcept : concepts) {
            if (existingConcept.getDisplay().equals(concept.getNames().get(0).getName())) {
                throw new ConceptNameAlreadyInUseException(String.format("Name \"%s\" already in use!", concept.getNames().get(0).getName()));
            }
        }
    }
}
