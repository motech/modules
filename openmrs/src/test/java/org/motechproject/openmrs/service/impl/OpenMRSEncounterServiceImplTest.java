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
import org.motechproject.openmrs.resource.EncounterResource;
import org.motechproject.openmrs.service.OpenMRSConceptService;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSEncounterServiceImplTest {

    private static final String CONFIG_NAME = "Configuration name";

    @Mock
    private OpenMRSPatientService patientService;

    @Mock
    private OpenMRSConceptService conceptService;

    @Mock
    private OpenMRSConfigService configService;

    @Mock
    private EncounterResource encounterResource;

    @Mock
    private EventRelay eventRelay;

    @Mock
    private Config config;

    @InjectMocks
    private OpenMRSEncounterServiceImpl encounterServiceImpl = new OpenMRSEncounterServiceImpl(encounterResource, patientService, eventRelay, configService);


    @Test(expected = OpenMRSException.class)
    public void shouldThrowOpenMRSExceptionIfEncounterIsNotCreated() throws Exception {
        Location location = new Location();
        location.setName("testLocation");

        Patient patient = new Patient();
        patient.setUuid("10");

        Provider provider = new Provider();
        provider.setUuid("20");

        Person person = new Person();
        person.setUuid("30");
        provider.setPerson(person);

        Visit visit = new Visit();
        visit.setUuid("40");

        DateTime encounterDatetime = new DateTime("2000-08-16T07:22:05Z");

        List<Observation> obsList = new ArrayList<>();

        Encounter encounter = new Encounter(location, new EncounterType("testEncounterType", null), encounterDatetime.toDate(), patient, visit, Collections.singletonList(provider.getPerson()), obsList);

        when(configService.getConfigByName(CONFIG_NAME)).thenReturn(config);
        when(encounterResource.createEncounter(config,encounter)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        try {
            encounterServiceImpl.createEncounter(CONFIG_NAME, encounter);
        } finally {
            verify(encounterResource).createEncounter(config, encounter);
        }
    }
}
