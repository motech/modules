package org.motechproject.atomclient.service.impl;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.motechproject.atomclient.service.AtomClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;


@Service("atomClientService")
public class AtomClientServiceImpl implements AtomClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomClientServiceImpl.class);

    private FeedFetcher feedFetcher;


    @Autowired
    public AtomClientServiceImpl(FeedRecordDataService feedRecordDataService) {
        feedFetcher = new HttpURLFeedFetcher(new FeedCache(feedRecordDataService));
    }


    @Override
    public void foo() {
        LOGGER.debug("foo");

        try {
            SyndFeed feed = feedFetcher.retrieveFeed(new URL("http://www.intertwingly.net/blog/index.atom"));
            LOGGER.debug("feed title: {}, # of links: {}", feed.getTitle(), feed.getLinks().size());
        } catch (FeedException | IOException | FetcherException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
