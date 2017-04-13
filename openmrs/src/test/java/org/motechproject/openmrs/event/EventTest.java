package org.motechproject.openmrs.event;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.repository.FeedRecordDataService;
import org.motechproject.openmrs.resource.impl.AbstractResourceImplTest;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.config.FeedConfig;
import org.motechproject.openmrs.service.impl.OpenMRSAtomFeedServiceImpl;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.tasks.constants.EventSubjects;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.testing.osgi.http.SimpleHttpServer;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventTest extends AbstractResourceImplTest {

    @Mock
    private EventRelay eventRelay;
    @Mock
    private FeedRecordDataService feedRecordDataService;
    @Mock
    private OpenMRSConfigService configService;
    @Mock
    private MotechSchedulerService motechSchedulerService;

    private OpenMRSAtomFeedServiceImpl atomFeedService;
    private String feedURL;

    static final String ATOM_FEED_DATA_XML = "xml/atom-feed-data.xml";
    static final String PATIENT_LAST_PAGE_ID = "36";
    static final String CONFIG_NAME = "configName";

    @Before
    public void setup() throws Exception {
        initMocks(this);
        atomFeedService = new OpenMRSAtomFeedServiceImpl(feedRecordDataService, eventRelay, configService, motechSchedulerService);

        ResponseEntity<String> response = getResponseFromFile(ATOM_FEED_DATA_XML);
        feedURL = SimpleHttpServer.getInstance().start("foo", HttpStatus.SC_OK, response.getBody());
        Config config = prepareConfigWithOnePage();

        when(configService.getConfigByName(eq(CONFIG_NAME))).thenReturn(config);
    }

    @Test
    public void shouldFetchNewPatientEntryFromAtomFeed() {
        ArgumentCaptor<MotechEvent> event = ArgumentCaptor.forClass(MotechEvent.class);

        atomFeedService.fetch(CONFIG_NAME);

        verify(eventRelay).sendEventMessage(event.capture());
        MotechEvent capturedEvent = event.getValue();

        assertNotNull(capturedEvent.getParameters());
        assertEquals(EventSubjects.PATIENT_FEED_CHANGE_MESSAGE, capturedEvent.getSubject());

        assertTrue(capturedEvent.getParameters().containsKey("uuid"));
        assertEquals("aca97062-35c5-4a23-baf8-56e6eec71111", capturedEvent.getParameters().get("uuid"));
    }

    private Config prepareConfigWithOnePage() {
        Map<String, String> atomFeeds = new HashMap<>();
        atomFeeds.put(EventKeys.PATIENT_SCHEDULE_KEY, null);
        atomFeeds.put(EventKeys.ATOM_FEED_PATIENT_PAGE_ID, PATIENT_LAST_PAGE_ID);

        return prepareConfig(atomFeeds);
    }

    private Config prepareConfig(Map<String, String> atomFeeds) {
        Config result = new Config();

        FeedConfig feedConfig = new FeedConfig();
        feedConfig.setPatientAtomFeed(true);
        feedConfig.setAtomFeeds(atomFeeds);

        result.setName(CONFIG_NAME);
        result.setOpenMrsUrl(feedURL);
        result.setFeedConfig(feedConfig);

        return result;
    }
}
