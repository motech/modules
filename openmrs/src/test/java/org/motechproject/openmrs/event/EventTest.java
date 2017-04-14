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

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

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

    private static final String ATOM_FEED_DATA_XML = "xml/atom-feed-data.xml";
    private static final String PATIENT_LAST_PAGE_ID = "36";
    private static final String CONFIG_NAME = "configName";
    private static final String PATIENT_UUID = "aca97062-35c5-4a23-baf8-56e6eec71111";

    @Before
    public void setup() throws Exception {
        initMocks(this);
        atomFeedService = new OpenMRSAtomFeedServiceImpl(feedRecordDataService, eventRelay, configService, motechSchedulerService);

        ResponseEntity<String> response = getResponseFromFile(ATOM_FEED_DATA_XML);
        feedURL = SimpleHttpServer.getInstance().start("foo", HttpStatus.SC_OK, response.getBody());

        when(configService.getConfigByName(eq(CONFIG_NAME))).thenReturn(prepareConfigWithOnePage());
    }

    @Test
    public void shouldFetchNewPatientEntryFromAtomFeed() {
        ArgumentCaptor<MotechEvent> event = ArgumentCaptor.forClass(MotechEvent.class);
        String uuidKey = "uuid";

        atomFeedService.fetch(CONFIG_NAME);

        verify(eventRelay).sendEventMessage(event.capture());
        MotechEvent capturedEvent = event.getValue();

        assertNotNull(capturedEvent.getParameters());
        assertEquals(EventSubjects.PATIENT_FEED_CHANGE_MESSAGE, capturedEvent.getSubject());

        assertTrue(capturedEvent.getParameters().containsKey(uuidKey));
        assertEquals(PATIENT_UUID, capturedEvent.getParameters().get(uuidKey));
    }

    private Config prepareConfigWithOnePage() {
        Map<String, String> atomFeeds = new HashMap<>();
        atomFeeds.put(EventKeys.PATIENT_SCHEDULE_KEY, null);
        atomFeeds.put(EventKeys.ATOM_FEED_PATIENT_PAGE_ID, PATIENT_LAST_PAGE_ID);

        return new Config(CONFIG_NAME, feedURL,  new FeedConfig(atomFeeds, true, false));
    }
}
