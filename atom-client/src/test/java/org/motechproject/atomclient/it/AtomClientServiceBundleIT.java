package org.motechproject.atomclient.it;

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
    public void setupProperties() {
        configService.setFeedConfigs(new FeedConfigs(
                new HashSet<>(Arrays.asList(
                        // http://intertwingly.net/blog/index.atom
                        // <![CDATA[/openmrs/ws/rest/v1/bahmnicore/bahmniencounter/cc5e4eff-b140-46fe-835a-1f8e4fc293ca?includeAll=true]]>

                        new FeedConfig("http://192.168.33.10:8080/openmrs/ws/atomfeed/patient/recent", "<!\\[CDATA\\[(.*/([0-9a-f-]*)\\?.*)\\]\\]>"),
                        new FeedConfig("http://192.168.33.10:8080/openmrs/ws/atomfeed/Encounter/recent", "<!\\[CDATA\\[(.*)\\]\\]>")
                ))));
        atomClientService.rescheduleFetchJob("");
    }


    @Test
    public void verifyService() {
        assertNotNull(atomClientService);
        atomClientService.fetch();
        atomClientService.fetch();
        atomClientService.fetch();
    }
}
