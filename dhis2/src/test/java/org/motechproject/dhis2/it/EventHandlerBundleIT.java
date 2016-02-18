package org.motechproject.dhis2.it;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.dhis2.domain.OrgUnit;
import org.motechproject.dhis2.domain.TrackedEntityInstanceMapping;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.repository.OrgUnitDataService;
import org.motechproject.dhis2.rest.domain.AttributeDto;
import org.motechproject.dhis2.service.Settings;
import org.motechproject.dhis2.service.SettingsService;
import org.motechproject.dhis2.service.TrackedEntityInstanceMappingService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.http.SimpleHttpServer;
import org.motechproject.testing.osgi.wait.Wait;
import org.motechproject.testing.osgi.wait.WaitCondition;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class EventHandlerBundleIT extends BaseDhisIT {
    private final Object waitLock = new Object();

    private static final String ENTITY_TYPE_ID = "entityUUID"; // Person
    private static final String INSTANCE_EXT_ID = "externalId";
    private static final String ORGUNIT_NAME = "OrgUnitName";
    private static final String ORGUNIT_ID = "OrgUnitID";
    private static final String ATTRIBUTE_UUID_1 = "attributeUuid1";
    private static final String ATTRIBUTE_VALUE_1 = "attributeValue1";
    private static final String ATTRIBUTE_UUID_2 = "attributeUuid2";
    private static final String ATTRIBUTE_VALUE_2 = "attributeValue2";

    @Inject
    private TrackedEntityInstanceMappingService trackedEntityInstanceMappingService;

    @Inject
    private SettingsService settingsService;

    @Inject
    private EventRelay eventRelay;

    @Inject
    private OrgUnitDataService orgUnitDataService;

    @Inject
    private EventListenerRegistryService eventListenerRegistry;

    @Before
    public void setUp() {
        orgUnitDataService.create(new OrgUnit(ORGUNIT_NAME,ORGUNIT_ID));
    }

    @Test
    // TODO: MOTECH-2149 (either redesign this test or kill it with fire)
    @Ignore
    public void testHandleRegistration() throws InterruptedException {
        getLogger().debug("Running testHandleRegistration()");

        final String responseBody = "{\"status\":\"SUCCESS\",\"importCount\":{\"imported\":1,\"updated\":0,\"ignored\"" +
                ":0,\"deleted\":0},\"reference\":\"IbqmvQFz0zW\"}{\"status\":\"SUCCESS\",\"importCount\":{\"imported\"" +
                ":1,\"updated\":0,\"ignored\":0,\"deleted\":0},\"reference\":\"GmHEBGJtymq\"}";

        SimpleHttpServer simpleServer = SimpleHttpServer.getInstance();

        String url = simpleServer.start("api/trackedEntityInstances",201,responseBody).replace("/api/trackedEntityInstances", "");
        Settings settings = new Settings(url, "name","password");

        settingsService.updateSettings(settings);

        AttributeDto testAttributeDto1 = new AttributeDto();
        testAttributeDto1.setAttribute(ATTRIBUTE_UUID_1);
        testAttributeDto1.setValue(ATTRIBUTE_VALUE_1);

        AttributeDto testAttributeDto2 = new AttributeDto();
        testAttributeDto2.setAttribute(ATTRIBUTE_UUID_2);
        testAttributeDto2.setValue(ATTRIBUTE_VALUE_2);

        Map<String, Object> params = new HashMap<>();
        params.put(EventParams.ENTITY_TYPE, ENTITY_TYPE_ID);
        params.put(EventParams.EXTERNAL_ID, INSTANCE_EXT_ID);
        params.put(EventParams.LOCATION, ORGUNIT_NAME);
        params.put(testAttributeDto1.getAttribute(), testAttributeDto1.getValue());
        params.put(testAttributeDto2.getAttribute(), testAttributeDto2.getValue());

        MotechEvent event = new MotechEvent(EventSubjects.CREATE_ENTITY, params);

        waitForListener();
        getLogger().debug("Sending create_entity Event - listener registered: {}",
                eventListenerRegistry.hasListener(EventSubjects.CREATE_ENTITY));
        eventRelay.sendEventMessage(event);
        wait2s();

        TrackedEntityInstanceMapping mapper = trackedEntityInstanceMappingService.findByExternalId(INSTANCE_EXT_ID);

        assertNotNull(mapper);
        assertEquals(mapper.getExternalName(),INSTANCE_EXT_ID);
        assertNotNull(mapper.getDhis2Uuid());
        assertEquals(mapper.getDhis2Uuid(),"IbqmvQFz0zW");
    }

    private void wait2s() throws InterruptedException {
        synchronized (waitLock) {
            waitLock.wait(2000);
        }
    }

    private void waitForListener() throws InterruptedException {
        new Wait(waitLock, new WaitCondition() {
            @Override
            public boolean needsToWait() {
                return !eventListenerRegistry.hasListener(EventSubjects.CREATE_ENTITY);
            }
        }, 250, 5000).start();
    }
}
