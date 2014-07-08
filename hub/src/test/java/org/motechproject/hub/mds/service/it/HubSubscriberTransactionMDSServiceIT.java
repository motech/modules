package org.motechproject.hub.mds.service.it;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.hub.mds.HubSubscriberTransaction;
import org.motechproject.hub.mds.HubSubscription;
import org.motechproject.hub.mds.HubTopic;
import org.motechproject.hub.mds.service.HubSubscriberTransactionMDSService;
import org.motechproject.hub.mds.service.HubSubscriptionMDSService;
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
public class HubSubscriberTransactionMDSServiceIT extends BasePaxIT {

    @Inject
    private HubSubscriberTransactionMDSService hubSubscriberTransactionMDSService;
    @Inject
    private HubSubscriptionMDSService hubSubscriptionMDSService;
    @Inject
    private HubTopicMDSService hubTopicMDSService;

    private String callbackUrl = "http://callback/url";
    private String topicUrl = "http://topic/url";

    @Test
    public void testHubSubscriberTransaction() {
        HubTopic hubTopic = new HubTopic();
        hubTopic.setTopicUrl(topicUrl);
        hubTopicMDSService.create(hubTopic);

        List<HubTopic> hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(1, hubTopics.size());

        int topicId = (int) (long) hubTopicMDSService.getDetachedField(
                hubTopic, "id");

        HubSubscription hubSubscription = new HubSubscription();
        hubSubscription.setCallbackUrl(callbackUrl);
        hubSubscription.setHubSubscriptionStatusId(3);
        hubSubscription.setHubTopicId(topicId);
        hubSubscriptionMDSService.create(hubSubscription);

        int hubSubscriptionId = (int) (long) hubSubscriptionMDSService
                .getDetachedField(hubSubscription, "id");

        List<HubSubscription> hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(callbackUrl, topicId);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(1, hubSubscriptions.size());
        Assert.assertEquals(callbackUrl, hubSubscriptions.get(0)
                .getCallbackUrl());
        Assert.assertEquals(3, (int) hubSubscriptions.get(0)
                .getHubSubscriptionStatusId());
        Assert.assertEquals(topicId, (int) hubSubscriptions.get(0)
                .getHubTopicId());

        List<HubSubscriberTransaction> hubSubscriberTransactions = hubSubscriberTransactionMDSService
                .findSubTransBySubId(hubSubscriptionId);
        Assert.assertNotNull(hubSubscriberTransactions);
        Assert.assertEquals(0, hubSubscriberTransactions.size());

        HubSubscriberTransaction hubSubscriberTransaction = new HubSubscriberTransaction();
        hubSubscriberTransaction.setContentId(1);
        hubSubscriberTransaction.setHubDistributionStatusId(2);
        hubSubscriberTransaction.setHubSubscriptionId(hubSubscriptionId);
        hubSubscriberTransactionMDSService.create(hubSubscriberTransaction);

        hubSubscriberTransactions = hubSubscriberTransactionMDSService
                .findSubTransBySubId(hubSubscriptionId);
        Assert.assertNotNull(hubSubscriberTransactions);
        Assert.assertEquals(1, hubSubscriberTransactions.size());
        Assert.assertEquals(1, (int) hubSubscriberTransactions.get(0)
                .getContentId());
        Assert.assertEquals(2, (int) hubSubscriberTransactions.get(0)
                .getHubDistributionStatusId());
        Assert.assertEquals(hubSubscriptionId, (int) hubSubscriberTransactions
                .get(0).getHubSubscriptionId());

        hubSubscriberTransactionMDSService.delete(hubSubscriberTransaction);
        hubSubscriberTransactions = hubSubscriberTransactionMDSService
                .findSubTransBySubId(hubSubscriptionId);
        Assert.assertNotNull(hubSubscriberTransactions);
        Assert.assertEquals(0, hubSubscriberTransactions.size());

        hubSubscriptionMDSService.delete(hubSubscription);
        hubSubscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(callbackUrl, topicId);
        Assert.assertNotNull(hubSubscriptions);
        Assert.assertEquals(0, hubSubscriptions.size());

        hubTopicMDSService.delete(hubTopic);
        hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());

    }
}
