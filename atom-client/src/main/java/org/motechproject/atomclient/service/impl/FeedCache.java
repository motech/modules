package org.motechproject.atomclient.service.impl;

import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.SyndFeedInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.atomclient.domain.FeedRecord;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;

public class FeedCache implements FeedFetcherCache {

    private FeedRecordDataService feedRecordDataService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedCache.class);


    public FeedCache(FeedRecordDataService feedRecordDataService) {
        this.feedRecordDataService = feedRecordDataService;
    }


    public static byte[] infoToBytes(SyndFeedInfo info) throws IOException, CloneNotSupportedException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        return json.getBytes("UTF-8");
    }


    public static SyndFeedInfo infoFromBytes(byte[] bytes) throws IOException , ClassNotFoundException {
        String json = new String(bytes, "UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, SyndFeedInfo.class);
    }


    public static String urlToString(URL url) throws IOException {
        return url.toExternalForm();
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
                byte[] bytes = (byte[]) feedRecordDataService.getDetachedField(record, "info");
                return infoFromBytes(bytes);
            }
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }


    /**
     * Add a SyndFeedInfo object to the cache
     *
     * @param feedUrl The url of the feed
     * @param syndFeedInfo A SyndFeedInfo for the feed
     */
    @Override
    @Transactional
    public void setFeedInfo(URL feedUrl, SyndFeedInfo syndFeedInfo) {
        try {
            feedRecordDataService.create(new FeedRecord(urlToString(feedUrl), infoToBytes(syndFeedInfo)));
        } catch (IOException | CloneNotSupportedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
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
            return infoFromBytes(bytes);
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }
}
