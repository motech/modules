package org.motechproject.atomclient.service.impl;

import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.SyndFeedInfo;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import org.joda.time.DateTime;
import org.motechproject.atomclient.domain.FeedRecord;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

public class FeedCache implements FeedFetcherCache {

    private FeedRecordDataService feedRecordDataService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedCache.class);


    public FeedCache(FeedRecordDataService feedRecordDataService) {
        this.feedRecordDataService = feedRecordDataService;
    }


    public static byte[] feedToBytes(SyndFeed feed) throws FeedException, UnsupportedEncodingException {
        SyndFeedOutput syndFeedOutput = new SyndFeedOutput();
        String s = syndFeedOutput.outputString(feed);
        return s.getBytes("UTF-8");
    }


    public static SyndFeed feedFromBytes(byte[] bytes) throws IOException, ClassNotFoundException, FeedException {
        String s = new String(bytes, "UTF-8");
        SyndFeedInput syndFeedInput = new SyndFeedInput();
        return syndFeedInput.build(new StringReader(s));
    }


    public static String urlToString(URL url) throws IOException {
        return url.toExternalForm();
    }


    public static URL urlFromString(String s) throws IOException {
        return new URL(s);
    }


    public SyndFeedInfo recordToFeedInfo(FeedRecord record) throws IOException, ClassNotFoundException, FeedException {
        SyndFeedInfo info = new SyndFeedInfo();
        info.setUrl(urlFromString(record.getUrl()));
        info.setETag(record.getFeedETag());
        info.setId(record.getFeedId());
        info.setLastModified(record.getFeedLastMofified());
        byte[] bytes = (byte[]) feedRecordDataService.getDetachedField(record, "feedData");
        info.setSyndFeed(feedFromBytes(bytes));
        return info;
    }


    /**
     * Get a SyndFeedInfo object from the cache.
     *
     * @param feedUrl The url of the feed
     * @return A SyndFeedInfo or null if it is not in the cache
     */
    @Override
    @Transactional
    public SyndFeedInfo getFeedInfo(URL feedUrl) {
        try {
            FeedRecord record = feedRecordDataService.findByURL(urlToString(feedUrl));
            if (record != null) {
                return recordToFeedInfo(record);
            }
        } catch (IOException | ClassNotFoundException | FeedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }


    /**
     * Sends a MOTECH event for each new feed entry
     *
     * @param feed
     */
    private void sendMessagesForNewFeedData(SyndFeed feed) {
        for (SyndEntry entry : feed.getEntries()) {
            LOGGER.debug("***NEW ENTRY***: {}", entry.getTitle());
            //todo the work!
        }
    }


    /**
     * Add a SyndFeedInfo object to the cache
     *
     * @param feedUrl The url of the feed
     * @param feedInfo A SyndFeedInfo for the feed
     */
    @Override
    @Transactional
    public void setFeedInfo(URL feedUrl, SyndFeedInfo feedInfo) {
        try {
            feedRecordDataService.create(new FeedRecord(urlToString(feedUrl), feedInfo.getId(),
                    urlToString(feedInfo.getUrl()), new DateTime(feedInfo.getLastModified()), feedInfo.getETag(),
                    feedToBytes(feedInfo.getSyndFeed())));
        } catch (IOException | FeedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        //Since we're adding an element to the cache, it must be new, let's broadcast this as a new feed
        sendMessagesForNewFeedData(feedInfo.getSyndFeed());
    }


    /**
     * Removes all items from the cache.
     */
    @Override
    @Transactional
    public void clear() {
        feedRecordDataService.deleteAll();
    }


    /**
     * Removes the SyndFeedInfo identified by the url from the cache.
     *
     * @return The removed SyndFeedInfo
     */
    @Override
    @Transactional
    public SyndFeedInfo remove(URL feedUrl) {
        try {
            FeedRecord record = feedRecordDataService.findByURL(urlToString(feedUrl));
            if (record != null) {
                LOGGER.error("Trying to remove feedRecord for inexistent URL {}", feedUrl);
                return null;
            }
            byte[] bytes = (byte[]) feedRecordDataService.getDetachedField(record, "info");
            feedRecordDataService.delete(record);
            return recordToFeedInfo(record);
        } catch (IOException | ClassNotFoundException | FeedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }
}
