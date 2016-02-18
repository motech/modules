package org.motechproject.atomclient.service;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.io.FeedException;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduler.contract.CronJobId;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


@Service("atomClientService")
public class AtomClientServiceImpl implements AtomClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomClientServiceImpl.class);

    private AtomClientConfigService configService;
    private MotechSchedulerService motechSchedulerService;
    private FeedFetcher feedFetcher;


    @Autowired
    public AtomClientServiceImpl(FeedRecordDataService feedRecordDataService, EventRelay eventRelay,
                                 AtomClientConfigService configService, MotechSchedulerService motechSchedulerService) {
        feedFetcher = new HttpURLFeedFetcher(new FeedCache(feedRecordDataService, eventRelay));
        this.configService = configService;
        this.motechSchedulerService = motechSchedulerService;
    }


    @PostConstruct
    public void initFetchJob() {
        rescheduleFetchJob(configService.getFetchCron());
    }


    @Override
    public void rescheduleFetchJob(String cronExpression) {

        if (cronExpression.isEmpty()) {
            LOGGER.info("Unscheduling existing fetch job.");
            motechSchedulerService.unscheduleJob(new CronJobId(new MotechEvent(Constants.FETCH_MESSAGE)));
            return;
        }

        CronSchedulableJob job = new CronSchedulableJob(new MotechEvent(Constants.FETCH_MESSAGE), cronExpression);
        motechSchedulerService.safeScheduleJob(job);
        LOGGER.info("The fetch job cron is {}", cronExpression);
    }


    @Override
    @Transactional
    public void fetch() {
        if (configService.getFeedConfigs().getFeeds().size() == 0) {
            LOGGER.warn("No feeds to fetch.");
        }
        for (FeedConfig feedConfig : configService.getFeedConfigs().getFeeds()) {
            URL url;
            try {
                url = new URL(feedConfig.getUrl());
            } catch (MalformedURLException e) {
                LOGGER.error("Invalid Atom feed URL {}", feedConfig.getUrl());
                continue;
            }
            LOGGER.trace("Fetching {}", url);
            try {
                feedFetcher.retrieveFeed(url);
            } catch (FeedException | IOException | FetcherException e) {
                LOGGER.error("Error while fetching {}: {}", feedConfig.getUrl(), e.getMessage());
            }
        }
    }
}
