package org.motechproject.atomclient.service.impl;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.io.FeedException;
import org.motechproject.atomclient.domain.FeedRecord;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.motechproject.atomclient.service.AtomClientConfigService;
import org.motechproject.atomclient.service.AtomClientService;
import org.motechproject.atomclient.service.Constants;
import org.motechproject.atomclient.service.FeedConfig;
import org.motechproject.atomclient.service.FeedConfigs;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Service("atomClientService")
public class AtomClientServiceImpl implements AtomClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomClientServiceImpl.class);

    private AtomClientConfigService configService;
    private MotechSchedulerService motechSchedulerService;
    private FeedFetcher feedFetcher;
    private FeedRecordDataService feedRecordDataService;


    @Autowired
    public AtomClientServiceImpl(FeedRecordDataService feedRecordDataService, EventRelay eventRelay,
                                 AtomClientConfigService configService, MotechSchedulerService motechSchedulerService) {
        feedFetcher = new HttpURLFeedFetcher(new FeedCache(feedRecordDataService, eventRelay, configService));
        this.configService = configService;
        this.motechSchedulerService = motechSchedulerService;
        this.feedRecordDataService = feedRecordDataService;
    }


    @PostConstruct
    public void initFetchJob() {
        scheduleFetchJob(configService.getFetchCron());
    }


    @Override
    public void scheduleFetchJob(String cronExpression) {
        if (cronExpression.isEmpty()) {
            motechSchedulerService.unscheduleJob(new CronJobId(new MotechEvent(Constants.FETCH_MESSAGE)));
            LOGGER.info("No fetch job cron.");
            return;
        }

        CronSchedulableJob job = new CronSchedulableJob(new MotechEvent(Constants.FETCH_MESSAGE), cronExpression);
        motechSchedulerService.safeScheduleJob(job);
        LOGGER.info("The fetch job cron is {}", cronExpression);
    }


    @Override
    @Transactional
    public void read(String currentUrl, String lastUrl) {
        List<FeedConfig> feeds = new ArrayList<>();
        String regex = "/([0-9a-f-]*)\\?";

        feeds.add(new FeedConfig(currentUrl, regex));
        feeds.add(new FeedConfig(lastUrl, regex));
        configService.setFeedConfigs(new FeedConfigs(new HashSet<>(feeds)));
        fetch();

        FeedRecord feedRecord = feedRecordDataService.findByURL(currentUrl);
        String currenData = feedRecord.getData();
        int currentPage = feedRecord.getPage(currenData);

        feedRecord = feedRecordDataService.findByURL(lastUrl);
        String lastData = feedRecord.getData();
        int lastPage = feedRecord.getPage(lastData);

        configService.readNewFeeds(currentPage, lastPage, currentUrl);
        fetch();
    }

    @Override
    @Transactional
    public void fetch() {
        if (configService.getFeedConfigs().getFeeds().isEmpty()) {
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
