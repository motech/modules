package org.motechproject.openmrs19.it;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.openmrs19.EventKeys;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSUserService;
import org.motechproject.openmrs19.domain.OpenMRSEncounter;
import org.motechproject.openmrs19.domain.OpenMRSEncounter.OpenMRSEncounterBuilder;
import org.motechproject.openmrs19.domain.OpenMRSObservation;
import org.motechproject.openmrs19.domain.OpenMRSProvider;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.rest.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractEncounterIT {
    @Autowired
    private OpenMRSUserService userAdapter;

    @Autowired
    private OpenMRSFacilityService facilityAdapter;

    @Autowired
    private OpenMRSEncounterService encounterAdapter;

    @Autowired
    private OpenMRSPatientService patientAdapter;

    @Autowired
    private EventListenerRegistryService eventListenerRegistryService;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    MrsListener mrsListener;
    final Object lock = new Object();

    @Test
    public void shouldCreateEncounter() throws UserAlreadyExistsException, HttpException, ParseException, InterruptedException {
        mrsListener = new MrsListener();
        eventListenerRegistryService.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_ENCOUNTER_SUBJECT));
        OpenMRSUser user = (OpenMRSUser) userAdapter.getUserByUserName("chuck");
        String obsDate = "2012-09-05";
        OpenMRSObservation ob = new OpenMRSObservation<>(format.parse(obsDate), "Motech Concept", "Test");
        Set<OpenMRSObservation> obs = new HashSet<>();
        obs.add(ob);
        
        OpenMRSPerson person = user.getPerson();
        OpenMRSProvider provider = new OpenMRSProvider(person);
        provider.setProviderId(person.getPersonId());

        OpenMRSFacility facility =  facilityAdapter.getFacilities("Clinic 1").get(0);
        OpenMRSPatient patient =  patientAdapter.getPatientByMotechId("700");
        OpenMRSEncounter encounter = new OpenMRSEncounterBuilder().withDate(format.parse(obsDate))
                .withEncounterType("ADULTINITIAL").withFacility(facility).withObservations(obs).withPatient(patient)
                .withProvider(provider).withCreator(user).build();

        OpenMRSEncounter saved;

        synchronized (lock) {
            saved = (OpenMRSEncounter) encounterAdapter.createEncounter(encounter);
            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertEquals(saved.getEncounterId(), mrsListener.eventParameters.get(EventKeys.ENCOUNTER_ID));
        assertEquals(saved.getProvider().getProviderId(), mrsListener.eventParameters.get(EventKeys.PROVIDER_ID));
        assertEquals(saved.getCreator().getUserId(), mrsListener.eventParameters.get(EventKeys.USER_ID));
        assertEquals(saved.getFacility().getFacilityId(), mrsListener.eventParameters.get(EventKeys.FACILITY_ID));
        assertEquals(saved.getDate(), mrsListener.eventParameters.get(EventKeys.ENCOUNTER_DATE));
        assertEquals(saved.getEncounterType(), mrsListener.eventParameters.get(EventKeys.ENCOUNTER_TYPE));
    }

    @Test
    public void shouldGetLatestEncounter() {
        OpenMRSEncounter encounter = (OpenMRSEncounter) encounterAdapter.getLatestEncounterByPatientMotechId("700", "ADULTINITIAL");

        assertNotNull(encounter);
        assertEquals(new LocalDate("2012-09-07"), encounter.getDate().toLocalDate());
    }

    public class MrsListener implements EventListener {

        private boolean created = false;
        private Map<String, Object> eventParameters;

        @MotechListener(subjects = {EventKeys.CREATED_NEW_ENCOUNTER_SUBJECT})
        public void handle(MotechEvent event) {
            created = true;
            eventParameters = event.getParameters();
            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public String getIdentifier() {
            return "mrsTestListener";
        }
    }
}
