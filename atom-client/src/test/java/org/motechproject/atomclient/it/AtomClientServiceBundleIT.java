package org.motechproject.atomclient.it;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.atomclient.domain.FeedRecord;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.motechproject.atomclient.service.AtomClientConfigService;
import org.motechproject.atomclient.service.AtomClientService;
import org.motechproject.atomclient.service.FeedConfig;
import org.motechproject.atomclient.service.FeedConfigs;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.http.SimpleHttpServer;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class AtomClientServiceBundleIT extends BasePaxIT {

    @Inject
    private AtomClientService atomClientService;
    @Inject
    private AtomClientConfigService configService;
    @Inject
    private FeedRecordDataService feedRecordDataService;


    @Before
    public void setup() {
        feedRecordDataService.deleteAll();
        atomClientService.scheduleFetchJob("");
    }


    @Test
    public void verifyService() {
        assertNotNull(atomClientService);
    }


    @Test
    public void verifySuccessiveFetches() {

        final String atomResponse1 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" +
                        "  <title>Patient AOP</title>\n" +
                        "  <link rel=\"self\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/recent\" />\n" +
                        "  <link rel=\"via\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/36\" />\n" +
                        "  <link rel=\"prev-archive\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/35\" />\n" +
                        "  <author>\n" +
                        "    <name>OpenMRS</name>\n" +
                        "  </author>\n" +
                        "  <id>bec795b1-3d17-451d-b43e-a094019f6984+36</id>\n" +
                        "  <generator uri=\"https://github.com/ICT4H/atomfeed\">OpenMRS Feed Publisher</generator>\n" +
                        "  <updated>2016-03-03T23:46:14Z</updated>\n" +
                        "  <entry>\n" +
                        "    <title>Patient</title>\n" +
                        "    <category term=\"patient\" />\n" +
                        "    <id>tag:atomfeed.ict4h.org:fbd3d76b-a23e-44d7-8df6-c0da95ab34ad</id>\n" +
                        "    <updated>2016-03-03T23:39:59Z</updated>\n" +
                        "    <published>2016-03-03T23:39:59Z</published>\n" +
                        "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/1f45f9b9-301b-42fd-8a98-9bd3115cd558?v=full]]></content>\n" +
                        "  </entry>\n" +
                        "  <entry>\n" +
                        "    <title>Patient</title>\n" +
                        "    <category term=\"patient\" />\n" +
                        "    <id>tag:atomfeed.ict4h.org:e321ea5d-14d6-41ee-8727-9e5291bbcbb6</id>\n" +
                        "    <updated>2016-03-03T23:41:20Z</updated>\n" +
                        "    <published>2016-03-03T23:41:20Z</published>\n" +
                        "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/51da1b49-c833-46e5-8f08-f88df0d16270?v=full]]></content>\n" +
                        "  </entry>\n" +
                        "  <entry>\n" +
                        "    <title>Patient</title>\n" +
                        "    <category term=\"patient\" />\n" +
                        "    <id>tag:atomfeed.ict4h.org:91b6e147-39eb-4dd1-a983-ec95fc99a9fb</id>\n" +
                        "    <updated>2016-03-03T23:42:34Z</updated>\n" +
                        "    <published>2016-03-03T23:42:34Z</published>\n" +
                        "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/8a9633e5-d23f-492e-9178-bb76f170f118?v=full]]></content>\n" +
                        "  </entry>\n" +
                        "  <entry>\n" +
                        "    <title>Patient</title>\n" +
                        "    <category term=\"patient\" />\n" +
                        "    <id>tag:atomfeed.ict4h.org:76843fee-721d-472c-a849-25c292e3d3b5</id>\n" +
                        "    <updated>2016-03-03T23:43:57Z</updated>\n" +
                        "    <published>2016-03-03T23:43:57Z</published>\n" +
                        "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/8a9633e5-d23f-492e-9178-bb76f170f118?v=full]]></content>\n" +
                        "  </entry>\n" +
                        "  <entry>\n" +
                        "    <title>Patient</title>\n" +
                        "    <category term=\"patient\" />\n" +
                        "    <id>tag:atomfeed.ict4h.org:e12ca14d-7fdb-44cf-9efc-f8298df6ce0b</id>\n" +
                        "    <updated>2016-03-03T23:46:14Z</updated>\n" +
                        "    <published>2016-03-03T23:46:14Z</published>\n" +
                        "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/8a9633e5-d23f-492e-9178-bb76f170f118?v=full]]></content>\n" +
                        "  </entry>\n" +
                        "</feed>\n";

        final String atomResponse2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" +
                        "  <title>Patient AOP</title>\n" +
                        "  <link rel=\"self\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/recent\" />\n" +
                        "  <link rel=\"via\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/37\" />\n" +
                        "  <link rel=\"prev-archive\" type=\"application/atom+xml\" href=\"http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/36\" />\n" +
                        "  <author>\n" +
                        "    <name>OpenMRS</name>\n" +
                        "  </author>\n" +
                        "  <id>bec795b1-3d17-451d-b43e-a094019f6984+37</id>\n" +
                        "  <generator uri=\"https://github.com/ICT4H/atomfeed\">OpenMRS Feed Publisher</generator>\n" +
                        "  <updated>2016-03-03T23:51:45Z</updated>\n" +
                        "  <entry>\n" +
                        "    <title>Patient</title>\n" +
                        "    <category term=\"patient\" />\n" +
                        "    <id>tag:atomfeed.ict4h.org:72deaf8e-039d-49ec-b041-4da9a35c95a2</id>\n" +
                        "    <updated>2016-03-03T23:51:45Z</updated>\n" +
                        "    <published>2016-03-03T23:51:45Z</published>\n" +
                        "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/8a9633e5-d23f-492e-9178-bb76f170f118?v=full]]></content>\n" +
                        "  </entry>\n" +
                        "</feed>\n";

        assertEquals(feedRecordDataService.count(), 0);

        String feedUrl = SimpleHttpServer.getInstance().start("foo", HttpStatus.SC_OK, atomResponse1);
        configService.setFeedConfigs(new FeedConfigs(new HashSet<>(Arrays.asList(new FeedConfig(feedUrl, "/([0-9a-f-]*)\\?")))));
        atomClientService.fetch();

        FeedRecord feedRecord = feedRecordDataService.findByURL(feedUrl);
        assertNotNull(feedRecord);
        String data1 = feedRecord.getData();


        // A second fetch of the same feed should yield the same cache content
        atomClientService.fetch();
        feedRecord = feedRecordDataService.findByURL(feedUrl);
        assertNotNull(feedRecord);
        String data2 = feedRecord.getData();
        assertThat(data2, is(data1));

        // A third (yep, just for luck) fetch of the same feed should yield the same cache content
        atomClientService.fetch();
        feedRecord = feedRecordDataService.findByURL(feedUrl);
        assertNotNull(feedRecord);
        data2 = feedRecord.getData();
        assertThat(data2, is(data1));


        // Let's have the atom sever respond with one more content item for the feed
        feedUrl = SimpleHttpServer.getInstance().start("foo", HttpStatus.SC_OK, atomResponse2);
        configService.setFeedConfigs(new FeedConfigs(new HashSet<>(Arrays.asList(new FeedConfig(feedUrl, "/([0-9a-f-]*)\\?")))));

        // This time, the cache content should be different than it was the first two tries
        atomClientService.fetch();
        feedRecord = feedRecordDataService.findByURL(feedUrl);
        assertNotNull(feedRecord);
        String data3 = feedRecord.getData();
        assertThat(data3, is(not(data2)));
    }
}
