package org.motechproject.atomclient.unit;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.atomclient.domain.FeedRecord;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.motechproject.atomclient.service.AtomClientConfigService;
import org.motechproject.atomclient.service.Constants;
import org.motechproject.atomclient.service.FeedConfig;
import org.motechproject.atomclient.service.FeedConfigs;
import org.motechproject.atomclient.service.impl.AtomClientServiceImpl;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.testing.osgi.http.SimpleHttpServer;

import java.util.Arrays;
import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventTest {

    @Mock
    private EventRelay eventRelay;
    @Mock
    private FeedRecordDataService feedRecordDataService;
    @Mock
    private AtomClientConfigService atomClientConfigService;
    @Mock
    private MotechSchedulerService motechSchedulerService;

    private AtomClientServiceImpl atomClientService;
    private String feedURL;


    static final String ATOM_FEED_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" +
            "  <title>Patient AOP</title>\n" +
            "  <link rel=\"self\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/recent\" />\n" +
            "  <link rel=\"via\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/35\" />\n" +
            "  <link rel=\"prev-archive\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/34\" />\n" +
            "  <author>\n" +
            "    <name>OpenMRS</name>\n" +
            "  </author>\n" +
            "  <id>bec795b1-3d17-451d-b43e-a094019f6984+35</id>\n" +
            "  <generator uri=\"https://github.com/ICT4H/atomfeed\">OpenMRS Feed Publisher</generator>\n" +
            "  <updated>2016-02-23T03:31:13Z</updated>\n" +
            "  <entry>\n" +
            "    <title>Patient</title>\n" +
            "    <category term=\"patient\" />\n" +
            "    <id>tag:atomfeed.ict4h.org:9cc95d86-1b8e-4582-a774-83cf4b73c23e</id>\n" +
            "    <updated>2016-02-23T03:31:13Z</updated>\n" +
            "    <published>2016-02-23T03:31:13Z</published>\n" +
            "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/aca97062-35c5-4a23-baf8-56e6eec76320?v=full]]></content>\n" +
            "  </entry>\n" +
            "</feed>\n";

    static final String ATOM_FEED_DATA_2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" +
            "  <title>Patient AOP</title>\n" +
            "  <link rel=\"self\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/recent\" />\n" +
            "  <link rel=\"via\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/36\" />\n" +
            "  <link rel=\"prev-archive\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/35\" />\n" +
            "  <author>\n" +
            "    <name>OpenMRS</name>\n" +
            "  </author>\n" +
            "  <id>bec795b1-3d17-451d-b43e-a094019f6984+35</id>\n" +
            "  <generator uri=\"https://github.com/ICT4H/atomfeed\">OpenMRS Feed Publisher</generator>\n" +
            "  <updated>2016-02-23T03:31:13Z</updated>\n" +
            "  <entry>\n" +
            "    <title>Patient</title>\n" +
            "    <category term=\"patient\" />\n" +
            "    <id>tag:atomfeed.ict4h.org:9cc95d86-1b8e-4582-a774-83cf4b73c23e</id>\n" +
            "    <updated>2016-02-23T03:31:13Z</updated>\n" +
            "    <published>2016-02-23T03:31:13Z</published>\n" +
            "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/aca97062-35c5-4a23-baf8-56e6eec76320?v=full]]></content>\n" +
            "  </entry>\n" +
            "</feed>\n";


    @Before
    public void setup() {
        initMocks(this);
        atomClientService = new AtomClientServiceImpl(feedRecordDataService, eventRelay, atomClientConfigService, motechSchedulerService);

        feedURL = SimpleHttpServer.getInstance().start("foo", HttpStatus.SC_OK, ATOM_FEED_DATA);
        when(atomClientConfigService.getFeedConfigs()).thenReturn(new FeedConfigs(
                new HashSet<>(Arrays.asList(
                        new FeedConfig(feedURL, null)
                ))
        ));
        when(atomClientConfigService.getRegexForFeedUrl(feedURL)).thenReturn(".*/([0-9a-f-]*)\\?.*");

    }


    @Test
    public void verifyEventSentForNewData() {
        ArgumentCaptor<MotechEvent> event = ArgumentCaptor.forClass(MotechEvent.class);

        atomClientService.fetch();

        verify(eventRelay).sendEventMessage(event.capture());

        assertNotNull(event.getValue().getParameters());
        assertEquals(Constants.FEED_CHANGE_MESSAGE, event.getValue().getSubject());
        assertTrue(event.getValue().getParameters().containsKey("extracted_content"));
        MotechEvent capturedEvent = event.getValue();
        assertEquals("aca97062-35c5-4a23-baf8-56e6eec76320", capturedEvent.getParameters().get("extracted_content"));
    }


    @Test
    public void verifyEventNotSentForExistingData() {
        when(feedRecordDataService.findByURL(feedURL)).thenReturn(new FeedRecord(feedURL, ATOM_FEED_DATA));

        ArgumentCaptor<MotechEvent> event = ArgumentCaptor.forClass(MotechEvent.class);

        atomClientService.fetch();

        verify(eventRelay, times(0)).sendEventMessage(event.capture());
    }

   @Test
    public void verifyRead() {
       String feedURL2 = SimpleHttpServer.getInstance().start("foo2", HttpStatus.SC_OK, ATOM_FEED_DATA_2);

       when(feedRecordDataService.findByURL(feedURL)).thenReturn(new FeedRecord(feedURL, ATOM_FEED_DATA));
       when(feedRecordDataService.findByURL(feedURL2)).thenReturn(new FeedRecord(feedURL2, ATOM_FEED_DATA_2));

       atomClientService.read(feedURL, feedURL2);

       verify(atomClientConfigService).readNewFeeds(35, 36, feedURL);
   }

}
