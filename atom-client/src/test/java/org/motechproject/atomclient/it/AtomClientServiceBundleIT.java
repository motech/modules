package org.motechproject.atomclient.it;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.motechproject.atomclient.service.AtomClientConfigService;
import org.motechproject.atomclient.service.AtomClientService;
import org.motechproject.atomclient.service.FeedConfig;
import org.motechproject.atomclient.service.FeedConfigs;
import org.motechproject.mds.query.SqlQueryExecution;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.http.SimpleHttpServer;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import javax.jdo.Query;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertNotNull;

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
    public void setupDatabase() {
        SqlQueryExecution sqe = new SqlQueryExecution() {

            @Override
            public String getSqlQuery() {
                return "DELETE FROM atomclient_feed_record WHERE 1=1";
            }

            @Override
            public Object execute(Query query) {
                query.execute();
                return null;
            }
        };
        try {
            feedRecordDataService.executeSQLQuery(sqe);
        } catch (Exception e) {
            String s = String.format("Exception while deleting atomclient_feed_record : %s", e.getMessage());
            getLogger().error(s);
            throw e;
        }
    }


    @Before
    public void setupAtomServer() {

        final String atomResponse =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                "    <id>tag:atomfeed.ict4h.org:8b38f530-6c19-4e52-a3be-d3fce58132ce</id>\n" +
                "    <updated>2016-02-23T03:27:59Z</updated>\n" +
                "    <published>2016-02-23T03:27:59Z</published>\n" +
                "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/aca97062-35c5-4a23-baf8-56e6eec76320?v=full]]></content>\n" +
                "  </entry>\n" +
                "  <entry>\n" +
                "    <title>Patient</title>\n" +
                "    <category term=\"patient\" />\n" +
                "    <id>tag:atomfeed.ict4h.org:9cc95d86-1b8e-4582-a774-83cf4b73c23e</id>\n" +
                "    <updated>2016-02-23T03:31:13Z</updated>\n" +
                "    <published>2016-02-23T03:31:13Z</published>\n" +
                "    <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/aca97062-35c5-4a23-baf8-56e6eec76320?v=full]]></content>\n" +
                "  </entry>\n" +
                "</feed>\n";

        String httpServerURI = SimpleHttpServer.getInstance().start("foo", HttpStatus.SC_OK, atomResponse);
        getLogger().debug("verifyServiceFunctional - We have a server listening at {}", httpServerURI);

        configService.setFeedConfigs(new FeedConfigs(
                new HashSet<>(Arrays.asList(
                        new FeedConfig(httpServerURI, "/([0-9a-f-]*)\\?")
                ))));
        atomClientService.scheduleFetchJob("");
    }


    @Test
    public void verifyService() {
        assertNotNull(atomClientService);
        atomClientService.fetch();
        atomClientService.fetch();
        atomClientService.fetch();
    }
}
