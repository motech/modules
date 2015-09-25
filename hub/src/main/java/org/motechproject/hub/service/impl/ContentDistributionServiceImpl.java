package org.motechproject.hub.service.impl;

import org.joda.time.DateTime;
import org.motechproject.hub.mds.HubDistributionContent;
import org.motechproject.hub.mds.HubPublisherTransaction;
import org.motechproject.hub.mds.HubSubscriberTransaction;
import org.motechproject.hub.mds.HubSubscription;
import org.motechproject.hub.mds.HubTopic;
import org.motechproject.hub.mds.service.HubDistributionContentMDSService;
import org.motechproject.hub.mds.service.HubPublisherTransactionMDSService;
import org.motechproject.hub.mds.service.HubSubscriberTransactionMDSService;
import org.motechproject.hub.mds.service.HubSubscriptionMDSService;
import org.motechproject.hub.mds.service.HubTopicMDSService;
import org.motechproject.hub.model.DistributionStatusLookup;
import org.motechproject.hub.model.SubscriptionStatusLookup;
import org.motechproject.hub.service.ContentDistributionService;
import org.motechproject.hub.service.DistributionServiceDelegate;
import org.motechproject.hub.util.HubConstants;
import org.motechproject.hub.util.HubUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.List;

/**
 * Default implementation of {@link org.motechproject.hub.service.ContentDistributionService}
 */
@Service(value = "contentDistributionService")
public class ContentDistributionServiceImpl implements
        ContentDistributionService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ContentDistributionService.class);

    private HubTopicMDSService hubTopicMDSService;

    private HubPublisherTransactionMDSService hubPublisherTransactionMDSService;

    private HubSubscriptionMDSService hubSubscriptionMDSService;

    private HubSubscriberTransactionMDSService hubSubscriberTransactionMDSService;

    private HubDistributionContentMDSService hubDistributionContentMDSService;

    /**
     * Creates a new instance of <code>ContentDistributionServiceImpl</code>, with
     * all fields set to the autowired parameters values.
     *
     * @param hubTopicService autowired {@link org.motechproject.hub.mds.service.HubTopicMDSService}
     * @param hubSubscriptionMDSService autowired {@link org.motechproject.hub.mds.service.HubSubscriberTransactionMDSService}
     * @param hubPublisherTransactionMDSService autowired {@link org.motechproject.hub.mds.service.HubPublisherTransactionMDSService}
     * @param hubSubscriberTransactionMDSService autowired {@link org.motechproject.hub.mds.service.HubSubscriberTransactionMDSService}
     * @param hubDistributionContentMDSService autowired {@link org.motechproject.hub.mds.service.HubDistributionContentMDSService}
     */
    @Autowired
    public ContentDistributionServiceImpl(
            HubTopicMDSService hubTopicService,
            HubSubscriptionMDSService hubSubscriptionMDSService,
            HubPublisherTransactionMDSService hubPublisherTransactionMDSService,
            HubSubscriberTransactionMDSService hubSubscriberTransactionMDSService,
            HubDistributionContentMDSService hubDistributionContentMDSService) {
        this.hubTopicMDSService = hubTopicService;
        this.hubSubscriptionMDSService = hubSubscriptionMDSService;
        this.hubPublisherTransactionMDSService = hubPublisherTransactionMDSService;
        this.hubSubscriberTransactionMDSService = hubSubscriberTransactionMDSService;
        this.hubDistributionContentMDSService = hubDistributionContentMDSService;
    }

    @Autowired
    private DistributionServiceDelegate distributionServiceDelegate;

    /**
     * Gets <code>DistributionServiceDelegate</code> used for this class to fetching
     * and distributing fetched content to all interested subscribers.
     *
     * @return the <code>DistributionServiceDelegate</code> object
     */
    public DistributionServiceDelegate getDistributionServiceDelegate() {
        return distributionServiceDelegate;
    }

    /**
     * Sets <code>DistributionServiceDelegate</code> to the value passed in a parameter. It's
     * used for fetching and distributing fetched content to all interested subscribers.
     *
     * @param distributionServiceDelegate <code>DistributionServiceDelegate</code> to be set
     */
    public void setDistributionServiceDelegate(
            DistributionServiceDelegate distributionServiceDelegate) {
        this.distributionServiceDelegate = distributionServiceDelegate;
    }

    /**
     * Creates a new instance of <code>ContentDistributionServiceImpl</code>, with
     * all fields set to null.
     */
    public ContentDistributionServiceImpl() {

    }

    @Override
    @Transactional
    public void distribute(String url) {
        List<HubTopic> hubTopics = hubTopicMDSService.findByTopicUrl(url);
        long topicId = -1;
        if (hubTopics == null || hubTopics.isEmpty()) {
            LOGGER.info(
                    "No Hub topics for the url '{}'. Creating the hub topic.",
                    url);
            HubTopic hubTopic = new HubTopic();
            hubTopic.setTopicUrl(url);
            hubTopic = hubTopicMDSService.create(hubTopic);
            topicId = (long) hubTopicMDSService
                    .getDetachedField(hubTopic, "id");
        } else if (hubTopics.size() > 1) {
            LOGGER.error("Multiple hub topics for the url {} ", url);
        } else {
            topicId = (long) hubTopicMDSService.getDetachedField(
                    hubTopics.get(0), "id");
        }

        // Get the content
        ResponseEntity<String> response = distributionServiceDelegate
                .getContent(url);

        String content = "";
        MediaType contentType = null;
        // Ignore any status code other than 2xx
        if (response != null
                && response.getStatusCode().value() / HubConstants.HUNDRED == 2) {
            content = response.getBody();
            contentType = response.getHeaders().getContentType();
            LOGGER.debug("Content received from Publisher: {}", content);
        }

        HubDistributionContent hubDistributionContent = new HubDistributionContent();
        hubDistributionContent.setContent(content);
        hubDistributionContent.setContentType(contentType == null ? ""
                : contentType.toString());
        hubDistributionContentMDSService.create(hubDistributionContent);

        final long contentId = (long) hubDistributionContentMDSService
                .getDetachedField(hubDistributionContent, "id");

        HubPublisherTransaction publisherTransaction = new HubPublisherTransaction();
        publisherTransaction.setHubTopicId(Integer.valueOf((int) topicId));
        publisherTransaction.setContentId(Integer.valueOf((int) contentId));
        publisherTransaction.setNotificationTime(new DateTime(HubUtils
                .getCurrentDateTime()));
        hubPublisherTransactionMDSService.create(publisherTransaction);

        List<HubSubscription> subscriptionList = hubSubscriptionMDSService
                .findSubByTopicId(Integer.valueOf((int) topicId));

        for (HubSubscription subscription : subscriptionList) {
            long subscriptionId = (long) hubSubscriptionMDSService
                    .getDetachedField(subscription, "id");

            DistributionStatusLookup statusLookup = DistributionStatusLookup.FAILURE;
            int subscriptionStatusId = Integer.valueOf(subscription
                    .getHubSubscriptionStatusId());
            if (subscriptionStatusId == SubscriptionStatusLookup.INTENT_VERIFIED
                    .getId()) {

                // distribute the content
                String callbackUrl = subscription.getCallbackUrl();
                distributionServiceDelegate.distribute(callbackUrl, content,
                        contentType, url);

            }
            HubSubscriberTransaction subscriberTransaction = new HubSubscriberTransaction();
            subscriberTransaction.setHubSubscriptionId(Integer
                    .valueOf((int) subscriptionId));

            subscriberTransaction.setHubDistributionStatusId(statusLookup
                    .getId());
            subscriberTransaction
                    .setContentId(Integer.valueOf((int) contentId));
            hubSubscriberTransactionMDSService.create(subscriberTransaction);

        }
    }
}
