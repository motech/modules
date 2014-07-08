package org.motechproject.hub.mds.service.it;

import java.util.List;

import javax.inject.Inject;

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

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HubTopicMDSServiceIT extends BasePaxIT {

    @Inject
    private HubTopicMDSService hubTopicMDSService;

    private String topicUrl = "http://topic/url";

    @Test
    public void testHubTopic() {
        List<HubTopic> hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());

        HubTopic hubTopic = new HubTopic();
        hubTopic.setTopicUrl(topicUrl);
        hubTopicMDSService.create(hubTopic);

        hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(1, hubTopics.size());
        Assert.assertEquals(topicUrl, hubTopics.get(0).getTopicUrl());

        hubTopicMDSService.delete(hubTopics.get(0));
        hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());

    }
}
