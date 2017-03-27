package org.motechproject.openmrs.service.impl;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.io.FeedException;
import org.apache.commons.collections.CollectionUtils;
import java.util.HashMap;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.repository.FeedRecordDataService;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSAtomFeedService;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.tasks.constants.EventSubjects;
import org.motechproject.scheduler.contract.CronJobId;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


@Service("atomFeedService")
public class OpenMRSAtomFeedServiceImpl implements OpenMRSAtomFeedService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSAtomFeedServiceImpl.class);

    private static final String NEXT_ARCHIVE = "next-archive";

    private OpenMRSConfigService configService;
    private MotechSchedulerService motechSchedulerService;
    private FeedFetcher feedFetcher;
    private FeedRecordDataService feedRecordDataService;

    private EventRelay eventRelay;
    private Config actualConfig;

    @Autowired
    public OpenMRSAtomFeedServiceImpl(FeedRecordDataService feedRecordDataService, EventRelay eventRelay,
                                      OpenMRSConfigService configService, MotechSchedulerService motechSchedulerService) {
        this.configService = configService;
        this.motechSchedulerService = motechSchedulerService;
        this.feedRecordDataService = feedRecordDataService;
        this.eventRelay = eventRelay;
        this.feedFetcher = new HttpURLFeedFetcher(new FeedCache(this.feedRecordDataService, this.eventRelay));

    }

    @Override
    public void shouldScheduleFetchJob(Map<String, String> feeds) {
        for (Map.Entry<String, String> feed : feeds.entrySet()) {
            if (isCronExpression(feed.getKey())) {
                schedule(feed);
            }
        }
    }

    private void schedule(Map.Entry<String, String> feed) {
        String subject = feed.getKey().equals(EventKeys.PATIENT_SCHEDULE_KEY) ? EventSubjects.FETCH_PATIENT_MESSAGE : EventSubjects.FETCH_ENCOUNTER_MESSAGE;
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("configName", actualConfig.getName());
        if (feed.getValue().isEmpty()) {
            motechSchedulerService.unscheduleJob(new CronJobId(new MotechEvent(subject, parameters)));
            LOGGER.info("No patient fetch job cron.");
        } else {
            CronSchedulableJob job = new CronSchedulableJob(new MotechEvent(subject, parameters), feed.getValue());
            motechSchedulerService.safeScheduleJob(job);
            LOGGER.info("The fetch job cron is {}", feed.getValue());
        }
    }

    @Override
    @Transactional
    public void fetch(String configName) {

        if(!configName.isEmpty()) {
            actualConfig = configService.getConfigByName(configName);
        }

        Map<String, String> feeds = actualConfig.getFeedConfig().getAtomFeeds();
        if (feeds.isEmpty()) {
            LOGGER.warn("No feeds to fetch.");
        }

        for (Map.Entry<String, String> feed : feeds.entrySet()) {
            String key = feed.getKey();
            if (isFeedSupported(key)) {
                fetchAll(feed);
            } else {
                LOGGER.error("Feed {} is not supported.", feed.getKey());
            }
        }

        shouldScheduleFetchJob(feeds);
    }

    @Override
    public void setActualConfig(Config actualConfig) {
        this.actualConfig = actualConfig;
    }

    private boolean isCronExpression(String key) {
        return key.equals(EventKeys.PATIENT_SCHEDULE_KEY) || key.equals(EventKeys.ENCOUNTER_SCHEDULE_KEY);
    }

    private void fetchAll(Map.Entry<String, String> feed) {
        URL url;
        boolean lastPage = false;
        SyndFeed pageFromFeed = null;
        String pageUrl = actualConfig.getAtomFeedUrl(feed);
        String pageId = feed.getValue();

        if (pageId.isEmpty()) {
            LOGGER.error("The OpenMRS Atom Feed Page ID cannot be empty.");
            return;
        }

        while (!lastPage) {
            try {
                url = new URL(pageUrl);
            } catch (MalformedURLException e) {
                LOGGER.error("Invalid Atom feed URL {}", pageUrl);
                return;
            }
            LOGGER.trace("Fetching {}", url);
            try {
                pageFromFeed = feedFetcher.retrieveFeed(url);
            } catch (FeedException | IOException | FetcherException e) {
                LOGGER.error("Error while fetching {}: {}", pageUrl, e.getMessage());
            }

            pageUrl = getNextPageUrl(pageFromFeed);
            lastPage = pageUrl.isEmpty() ? true : false;
        }
    }

    private String getNextPageUrl(SyndFeed page) {
        String result = "";

        if (null != page.getLinks() && CollectionUtils.isNotEmpty(page.getLinks())) {
            for (SyndLink link : page.getLinks()) {
                if (NEXT_ARCHIVE.equals(link.getRel())) {
                    result = link.getHref();
                    break;
                }
            }
        }
        return result;
    }

    private boolean isFeedSupported(String key) {
        boolean result = true;

        switch (key) {
            case EventKeys.ATOM_FEED_ENCOUNTER_PAGE_ID:
                break;
            case EventKeys.ATOM_FEED_PATIENT_PAGE_ID:
                break;
            default:
                result = false;
        }

        return result;
    }
}

