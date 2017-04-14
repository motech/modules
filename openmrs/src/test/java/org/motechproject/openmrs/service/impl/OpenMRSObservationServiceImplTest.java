package org.motechproject.openmrs.service.impl;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.*;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.resource.ObservationResource;
import org.motechproject.openmrs.service.OpenMRSConceptService;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSObservationServiceImplTest {

    private static final String CONFIG_NAME = "Configuration name";


    @Mock
    private OpenMRSPatientService patientService;

    @Mock
    private OpenMRSConfigService configService;

    @Mock
    private ObservationResource obsResource;

    @Mock
    private EventRelay eventRelay;

    @Mock
    private Config config;

    @InjectMocks
    private OpenMRSObservationServiceImpl observationService = new OpenMRSObservationServiceImpl(obsResource, patientService, eventRelay, configService);

    @Test
    public void shouldCreateObservation() {
        Observation expected = prepareObservation();

        when(configService.getConfigByName(CONFIG_NAME)).thenReturn(config);
        when(obsResource.createObservation(config, expected)).thenReturn(expected);

        Observation fetched = observationService.createObservation(CONFIG_NAME, prepareObservation());

        assertEquals(expected, fetched);
    }

    @Test
    public void shouldCreateObservationFromJson() {
        when(configService.getConfigByName(CONFIG_NAME)).thenReturn(config);
        when(obsResource.createObservationFromJson(config, "")).thenReturn(prepareObservation());

        Observation fetched = observationService.createOrUpdateObservationFromJson(CONFIG_NAME, null, "");

        assertEquals(prepareObservation(), fetched);
    }

    @Test
    public void shouldUpdateObservationFromJson() {
        String uuid = "obsUuid";
        when(configService.getConfigByName(CONFIG_NAME)).thenReturn(config);
        when(obsResource.updateObservation(config, uuid, "")).thenReturn(prepareObservation());

        Observation fetched = observationService.createOrUpdateObservationFromJson(CONFIG_NAME, uuid, "");

        assertEquals(prepareObservation(), fetched);
    }

    @Test(expected = OpenMRSException.class)
    public void shouldThrowOpenMRSExceptionIfObsercationIsNotCreated() throws Exception {
        Observation observation = prepareObservation();

        when(configService.getConfigByName(CONFIG_NAME)).thenReturn(config);
        when(obsResource.createObservation(config, observation)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        try {
            observationService.createObservation(CONFIG_NAME, observation);
        } finally {
            verify(obsResource).createObservation(config, observation);
        }
    }

    private Observation prepareObservation() {
        Concept concept = new Concept();
        concept.setUuid("1");

        DateTime obsDateTime = new DateTime("2000-08-16T07:22:05Z");

        return new Observation("obsDisplay", concept, new Person("2"), obsDateTime.toDate());
    }
}
