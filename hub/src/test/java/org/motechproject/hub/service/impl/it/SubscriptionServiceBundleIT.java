package org.motechproject.hub.service.impl.it;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.hub.exception.HubException;
import org.motechproject.hub.mds.HubSubscription;
import org.motechproject.hub.mds.HubTopic;
import org.motechproject.hub.mds.service.HubSubscriptionMDSService;
import org.motechproject.hub.mds.service.HubTopicMDSService;
import org.motechproject.hub.model.Modes;
import org.motechproject.hub.service.SubscriptionService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

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
public class SubscriptionServiceBundleIT extends BasePaxIT {

    private static final String CALLBACK_URL = "http://callback/url";
    private static final String TOPIC_URL = "http://topic/url";

    @Inject
    private SubscriptionService subscriptionService;
    @Inject
    private HubTopicMDSService hubTopicMDSService;
    @Inject
    private HubSubscriptionMDSService hubSubscriptionMDSService;

    @Test
    public void testSubscriptionAndUnsubscription() throws HubException {
        List<HubTopic> hubTopics = hubTopicMDSService.findByTopicUrl(TOPIC_URL);
        List<HubSubscription> hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrl(CALLBACK_URL);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(0, hubSubscriptions.size());

        // add a subscription
        subscriptionService.subscribe(CALLBACK_URL, Modes.SUBSCRIBE, TOPIC_URL,
                null, null);

        hubTopics = hubTopicMDSService.findByTopicUrl(TOPIC_URL);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(1, hubTopics.size());

        HubTopic hubTopic = hubTopics.get(0);
        Assert.assertEquals(TOPIC_URL, hubTopic.getTopicUrl());

        int topicId = hubTopicMDSService.doInTransaction(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus transactionStatus) {
                return (int) (long) hubTopicMDSService.getDetachedField(
                        hubTopicMDSService.retrieveAll().get(0), "id");
            }
        });

        hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(CALLBACK_URL, topicId);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(1, hubSubscriptions.size());
        HubSubscription hubSubscription = hubSubscriptions.get(0);
        Assert.assertEquals(topicId, (int) hubSubscription.getHubTopicId());
        Assert.assertEquals(CALLBACK_URL, hubSubscription.getCallbackUrl());

        // delete the subscription
        hubSubscriptionMDSService.delete(hubSubscription);
        hubTopicMDSService.delete(hubTopic);
        hubTopics = hubTopicMDSService.findByTopicUrl(TOPIC_URL);
        hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(CALLBACK_URL, topicId);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(0, hubSubscriptions.size());
    }

}
