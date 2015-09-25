package org.motechproject.hub.mds.service.it;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.hub.mds.HubSubscription;
import org.motechproject.hub.mds.HubTopic;
import org.motechproject.hub.mds.service.HubSubscriptionMDSService;
import org.motechproject.hub.mds.service.HubTopicMDSService;
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

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HubSubscriptionMDSServiceBundleIT extends BasePaxIT {

    private static final String CALLBACK_URL = "http://callback/url";
    private static final String TOPIC_URL = "http://topic/url";

    @Inject
    private HubSubscriptionMDSService hubSubscriptionMDSService;

    @Inject
    private HubTopicMDSService hubTopicMDSService;

    @Test
    public void testHubSubscriptionByCallbackUrl() {
        List<HubSubscription> hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrl(CALLBACK_URL);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(0, hubSubscriptions.size());

        HubSubscription hubSubscription = new HubSubscription();
        hubSubscription.setCallbackUrl(CALLBACK_URL);
        hubSubscription.setHubSubscriptionStatusId(3);
        hubSubscription.setHubTopicId(1);
        hubSubscriptionMDSService.create(hubSubscription);

        hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrl(CALLBACK_URL);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(1, hubSubscriptions.size());
        Assert.assertEquals(CALLBACK_URL, hubSubscriptions.get(0)
                .getCallbackUrl());
        Assert.assertEquals(1, (int) hubSubscriptions.get(0).getHubTopicId());

        hubSubscriptionMDSService.delete(hubSubscriptions.get(0));
        hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrl(CALLBACK_URL);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(0, hubSubscriptions.size());
    }

    @Test
    public void testHubSubscriptionByTopicAndCallbackUrl() {
        HubTopic hubTopic = new HubTopic();
        hubTopic.setTopicUrl(TOPIC_URL);
        hubTopicMDSService.create(hubTopic);

        List<HubTopic> hubTopics = hubTopicMDSService.findByTopicUrl(TOPIC_URL);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(1, hubTopics.size());

        int topicId = hubTopicMDSService.doInTransaction(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus transactionStatus) {
                return (int) (long) hubTopicMDSService.getDetachedField(
                        hubTopicMDSService.retrieveAll().get(0), "id");
            }
        });

        List<HubSubscription> hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(CALLBACK_URL, topicId);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(0, hubSubscriptions.size());

        HubSubscription hubSubscription = new HubSubscription();
        hubSubscription.setCallbackUrl(CALLBACK_URL);
        hubSubscription.setHubSubscriptionStatusId(3);
        hubSubscription.setHubTopicId(topicId);
        hubSubscriptionMDSService.create(hubSubscription);

        hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(CALLBACK_URL, topicId);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(1, hubSubscriptions.size());
        Assert.assertEquals(CALLBACK_URL, hubSubscriptions.get(0)
                .getCallbackUrl());
        Assert.assertEquals(3, (int) hubSubscriptions.get(0)
                .getHubSubscriptionStatusId());
        Assert.assertEquals(topicId, (int) hubSubscriptions.get(0)
                .getHubTopicId());

        hubSubscriptionMDSService.delete(hubSubscriptions.get(0));
        hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(CALLBACK_URL, topicId);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(0, hubSubscriptions.size());

        hubTopicMDSService.delete(hubTopic);
        hubTopics = hubTopicMDSService.findByTopicUrl(TOPIC_URL);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());
    }
}
