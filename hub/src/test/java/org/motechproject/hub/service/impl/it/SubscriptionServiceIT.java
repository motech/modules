package org.motechproject.hub.service.impl.it;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.hub.exception.HubException;
import org.motechproject.hub.mds.HubSubscription;
import org.motechproject.hub.mds.HubTopic;
import org.motechproject.hub.mds.service.HubSubscriptionMDSService;
import org.motechproject.hub.mds.service.HubTopicMDSService;
import org.motechproject.hub.mds.service.it.BaseHubIT;
import org.motechproject.hub.model.Modes;
import org.motechproject.hub.service.SubscriptionService;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.List;

/**
 * This class tests the service <code>SubscriptionService</code>
 *
 * @author Anuranjan
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class SubscriptionServiceIT extends BaseHubIT {

    @Inject
    private SubscriptionService subscriptionService;
    @Inject
    private HubTopicMDSService hubTopicMDSService;
    @Inject
    private HubSubscriptionMDSService hubSubscriptionMDSService;

    private String callbackUrl = "http://callback/url";
    private String topicUrl = "http://topic/url";

    @Test
    public void testSubscriptionAndUnsubscription() throws HubException {
        List<HubTopic> hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        List<HubSubscription> hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrl(callbackUrl);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(0, hubSubscriptions.size());

        // add a subscription
        subscriptionService.subscribe(callbackUrl, Modes.SUBSCRIBE, topicUrl,
                null, null);

        hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(1, hubTopics.size());

        HubTopic hubTopic = hubTopics.get(0);
        Assert.assertEquals(topicUrl, hubTopic.getTopicUrl());

        int topicId = (int) (long) hubTopicMDSService.getDetachedField(
                hubTopic, "id");

        hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(callbackUrl, topicId);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(1, hubSubscriptions.size());
        HubSubscription hubSubscription = hubSubscriptions.get(0);
        Assert.assertEquals(topicId, (int) hubSubscription.getHubTopicId());
        Assert.assertEquals(callbackUrl, hubSubscription.getCallbackUrl());

        // delete the subscription
        hubSubscriptionMDSService.delete(hubSubscription);
        hubTopicMDSService.delete(hubTopic);
        hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(callbackUrl, topicId);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(0, hubSubscriptions.size());
    }

}
