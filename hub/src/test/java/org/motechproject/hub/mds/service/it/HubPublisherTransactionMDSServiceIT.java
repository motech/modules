package org.motechproject.hub.mds.service.it;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.hub.mds.HubPublisherTransaction;
import org.motechproject.hub.mds.HubTopic;
import org.motechproject.hub.mds.service.HubPublisherTransactionMDSService;
import org.motechproject.hub.mds.service.HubTopicMDSService;
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
public class HubPublisherTransactionMDSServiceIT extends BaseHubIT {

    @Inject
    private HubPublisherTransactionMDSService hubPublisherTransactionMDSService;
    @Inject
    private HubTopicMDSService hubTopicMDSService;

    private String topicUrl = "http://topic/url";

    @Test
    public void testHubPublisherTransaction() {
        HubTopic hubTopic = new HubTopic();
        hubTopic.setTopicUrl(topicUrl);
        hubTopicMDSService.create(hubTopic);

        List<HubTopic> hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(1, hubTopics.size());

        int topicId = (int) (long) hubTopicMDSService.getDetachedField(
                hubTopic, "id");

        List<HubPublisherTransaction> hubPublisherTransactions = hubPublisherTransactionMDSService
                .findPubTransactionByTopicId(topicId);
        Assert.assertNotNull(hubPublisherTransactions);
        Assert.assertEquals(0, hubPublisherTransactions.size());

        HubPublisherTransaction hubPublisherTransaction = new HubPublisherTransaction();
        hubPublisherTransaction.setHubTopicId(topicId);
        hubPublisherTransactionMDSService.create(hubPublisherTransaction);

        hubPublisherTransactions = hubPublisherTransactionMDSService
                .findPubTransactionByTopicId(topicId);
        Assert.assertNotNull(hubPublisherTransactions);
        Assert.assertEquals(1, hubPublisherTransactions.size());
        Assert.assertEquals(topicId, (int) hubPublisherTransactions.get(0)
                .getHubTopicId());

        hubPublisherTransactionMDSService.delete(hubPublisherTransaction);
        hubPublisherTransactions = hubPublisherTransactionMDSService
                .findPubTransactionByTopicId(topicId);
        Assert.assertNotNull(hubPublisherTransactions);
        Assert.assertEquals(0, hubPublisherTransactions.size());

        hubTopicMDSService.delete(hubTopic);
        hubTopics = hubTopicMDSService.findByTopicUrl(topicUrl);
        Assert.assertNotNull(hubTopics);
        Assert.assertEquals(0, hubTopics.size());

    }
}
