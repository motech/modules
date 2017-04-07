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
    private static final String CONFIG_NAME_KEY = "configName";

    private OpenMRSConfigService configService;
    private MotechSchedulerService motechSchedulerService;
    private FeedFetcher feedFetcher;
    private FeedRecordDataService feedRecordDataService;

    private EventRelay eventRelay;

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
    public void shouldScheduleFetchJob(Config config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CONFIG_NAME_KEY, config.getName());

        for (Map.Entry<String, String> feed : config.getFeedConfig().getAtomFeeds().entrySet()) {
            if (isCronExpression(feed.getKey())) {
                schedule(config.getName(), feed);
            }
        }
        if (!config.getFeedConfig().isPatientAtomFeed()) {
            unschedule(EventSubjects.FETCH_PATIENT_MESSAGE, parameters);
        }
        if (!config.getFeedConfig().isEncounterAtomFeed()) {
            unschedule(EventSubjects.FETCH_ENCOUNTER_MESSAGE, parameters);
        }
    }

    private void schedule(String configName, Map.Entry<String, String> feed) {
        String subject = feed.getKey().equals(EventKeys.PATIENT_SCHEDULE_KEY) ? EventSubjects.FETCH_PATIENT_MESSAGE : EventSubjects.FETCH_ENCOUNTER_MESSAGE;
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(CONFIG_NAME_KEY, configName);
        if (feed.getValue() == null) {
            unschedule(subject, parameters);
        } else {
            CronSchedulableJob job = new CronSchedulableJob(new MotechEvent(subject, parameters), feed.getValue());
            motechSchedulerService.safeScheduleJob(job);
            LOGGER.info("The fetch job cron is {}", feed.getValue());
        }
    }

    @Override
    @Transactional
    public void fetch(String configName) {
        Config config = configService.getConfigByName(configName);

        Map<String, String> feeds = config.getFeedConfig().getAtomFeeds();
        if (feeds.isEmpty()) {
            LOGGER.warn("No feeds to fetch.");
        }

        for (Map.Entry<String, String> feed : feeds.entrySet()) {
            String key = feed.getKey();
            if (isFeedSupported(config, key)) {
                fetchAll(config, feed);
            }
        }
    }

    private boolean isCronExpression(String key) {
        return key.equals(EventKeys.PATIENT_SCHEDULE_KEY) || key.equals(EventKeys.ENCOUNTER_SCHEDULE_KEY);
    }

    private void fetchAll(Config config, Map.Entry<String, String> feed) {
        URL url;
        boolean lastPage = false;
        SyndFeed pageFromFeed = null;
        String pageUrl = config.getAtomFeedUrl(feed);
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

            pageUrl = getNextPageUrl(config, pageFromFeed, feed.getKey());
            lastPage = pageUrl.isEmpty() ? true : false;
        }
    }

    private String getNextPageUrl(Config config, SyndFeed page, String feedKey) {
        String result = "";

        if (null != page.getLinks() && CollectionUtils.isNotEmpty(page.getLinks())) {
            for (SyndLink link : page.getLinks()) {
                if (NEXT_ARCHIVE.equals(link.getRel())) {
                    result = link.getHref();
                    updateCurrentPageId(config, result, feedKey);
                    break;
                }
            }
        }
        return result;
    }

    private void updateCurrentPageId(Config config, String href, String feedKey) {
        String actualPageId = href != null ? href.substring(href.lastIndexOf('/') + 1) : "";

        if (!actualPageId.isEmpty()) {
            config.getFeedConfig().getAtomFeeds().put(feedKey, actualPageId);
            configService.updateConfig(config);
        }
    }

    private boolean isFeedSupported(Config config, String key) {
        boolean result;

        switch (key) {
            case EventKeys.ATOM_FEED_ENCOUNTER_PAGE_ID:
                result = config.getFeedConfig().isEncounterAtomFeed() ? true : false;
                break;
            case EventKeys.ATOM_FEED_PATIENT_PAGE_ID:
                result = config.getFeedConfig().isPatientAtomFeed() ? true : false;
                break;
            default:
                result = false;
        }
        return result;
    }

    private void unschedule(String subject, Map<String, Object> params) {
        motechSchedulerService.unscheduleJob(new CronJobId(new MotechEvent(subject, params)));
        LOGGER.info("No fetch job cron.");
    }
}

