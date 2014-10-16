package org.motechproject.hub.mds.service.it;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.hub.mds.HubTopic;
import org.motechproject.hub.mds.service.HubTopicMDSService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.List;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HubTopicMDSServiceBundleIT extends BasePaxIT {

    private static final String TOPIC_URL = "http://topic/url";

    @Inject
    private HubTopicMDSService hubTopicMDSService;

    @Test
    public void testHubTopic() {
        List<HubTopic> hubTopics = hubTopicMDSService.findByTopicUrl(TOPIC_URL);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());

        HubTopic hubTopic = new HubTopic();
        hubTopic.setTopicUrl(TOPIC_URL);
        hubTopicMDSService.create(hubTopic);

        hubTopics = hubTopicMDSService.findByTopicUrl(TOPIC_URL);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(1, hubTopics.size());
        Assert.assertEquals(TOPIC_URL, hubTopics.get(0).getTopicUrl());

        hubTopicMDSService.delete(hubTopics.get(0));
        hubTopics = hubTopicMDSService.findByTopicUrl(TOPIC_URL);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());

    }
}
