package org.motechproject.atomclient.service.impl;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.apache.commons.lang.StringUtils;
import org.motechproject.atomclient.exception.AtomClientConfigurationException;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.motechproject.atomclient.service.AtomClientService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.server.config.SettingsFacade;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;


@Service("atomClientService")
public class AtomClientServiceImpl implements AtomClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomClientServiceImpl.class);

    public static final String ATOMCLIENT_CRON = "atomclient.fetch.cron";
    public static final String ATOMCLIENT_FEED_URL = "atomclient.feed.url";

    private SettingsFacade settingsFacade;
    private MotechSchedulerService motechSchedulerService;
    private FeedFetcher feedFetcher;
    private URL feedUrl;


    @Autowired
    public AtomClientServiceImpl(FeedRecordDataService feedRecordDataService, EventRelay eventRelay,
                                 @Qualifier("atomClientSettings") SettingsFacade settingsFacade,
                                 MotechSchedulerService motechSchedulerService) {
        feedFetcher = new HttpURLFeedFetcher(new FeedCache(feedRecordDataService, eventRelay));
        this.settingsFacade = settingsFacade;
        this.motechSchedulerService = motechSchedulerService;
    }


    @PostConstruct
    public void initFetchJob() {
        String cronExpression = settingsFacade.getProperty(ATOMCLIENT_CRON);
        if (StringUtils.isBlank(cronExpression)) {
            LOGGER.warn("No cron expression configured, no ATOM feed will be fetched!");
            return;
        }

        String urlString = settingsFacade.getProperty(ATOMCLIENT_FEED_URL);
        if (StringUtils.isBlank(urlString)) {
            LOGGER.warn("No feed URL configured, no ATOM feed will be fetched!");
            return;
        }

        setupFetchJob(urlString, cronExpression);
    }


    @MotechListener(subjects = { Constants.ATOMCLIENT_FEED_FETCH_MESSAGE })
    public void handleFeedFetch(MotechEvent event) {
        LOGGER.info("handleFeedFetch {} ", event.toString());
        //todo
    }


    @Override
    public void setupFetchJob(String urlString, String cronExpression) {
        try {
            CronExpression.validateExpression(cronExpression);
        } catch (ParseException ex) {
            throw new AtomClientConfigurationException(String.format("Cron expression %s is invalid: %s",
                    cronExpression, ex.getMessage()), ex);
        }

        try {
            feedUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new AtomClientConfigurationException("Atom feed URL is invalid: " + urlString, e);
        }

        CronSchedulableJob mctsImportJob = new CronSchedulableJob(
                new MotechEvent(Constants.ATOMCLIENT_FEED_FETCH_MESSAGE), cronExpression);
        motechSchedulerService.safeScheduleJob(mctsImportJob);

        LOGGER.info("Atom feed URL {}", feedUrl.toExternalForm());
        LOGGER.info("Atom feed fetch cron {}", cronExpression);
    }


    @Override
    public void fetch() {
        LOGGER.debug("fetch");

        try {
            SyndFeed feed = feedFetcher.retrieveFeed(feedUrl);
            LOGGER.debug("feed title: {}, # of entries: {}", feed.getTitle(), feed.getEntries().size());
        } catch (FeedException | IOException | FetcherException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
