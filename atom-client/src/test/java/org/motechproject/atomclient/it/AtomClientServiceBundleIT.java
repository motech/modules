package org.motechproject.atomclient.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.motechproject.atomclient.service.AtomClientService;
import org.motechproject.atomclient.service.FeedConfigs;
import org.motechproject.atomclient.service.AtomClientConfigService;
import org.motechproject.atomclient.service.FeedConfig;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
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
        feedRecordDataService.deleteAll();
    }


    @Before
    public void setupProperties() {
        configService.setFetchCron("0/10 * * * * ?");
        configService.setFeedConfigs(new FeedConfigs(
                new HashSet<>(Arrays.asList(
                        new FeedConfig("http://localhost:8080/openmrs/ws/atomfeed/foo/recent", ""),
                        new FeedConfig("http://localhost:8080/openmrs/ws/atomfeed/bar/recent", ""),
                        new FeedConfig("http://localhost:8080/openmrs/ws/atomfeed/bat/recent", "")
                ))));
        // http://intertwingly.net/blog/index.atom
    }


    @Test
    public void verifyService() {
        assertNotNull(atomClientService);
        atomClientService.fetch();
        atomClientService.fetch();
        atomClientService.fetch();
    }
}
