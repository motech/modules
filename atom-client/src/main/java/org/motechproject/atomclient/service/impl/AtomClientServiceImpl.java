package org.motechproject.atomclient.service.impl;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.motechproject.atomclient.service.AtomClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;


@Service("atomClientService")
public class AtomClientServiceImpl implements AtomClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomClientServiceImpl.class);


    public AtomClientServiceImpl() {
        LOGGER.debug("AtomClientServiceImpl");
    }



    @Override
    public void foo() {
        LOGGER.debug("foo");

        try {
            FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
            FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
            SyndFeed feed = feedFetcher.retrieveFeed(new URL("http://blogs.sun.com/roller/rss/pat"));
            LOGGER.debug("feed={}", feed.toString());
        } catch (FeedException | IOException | FetcherException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
