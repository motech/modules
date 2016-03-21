package org.motechproject.atomclient.service.impl;

import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.SyndFeedInfo;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.atomclient.domain.FeedRecord;
import org.motechproject.atomclient.repository.FeedRecordDataService;
import org.motechproject.atomclient.service.AtomClientConfigService;
import org.motechproject.atomclient.service.Constants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This is the 'brains' of this module. We use the rometools {@link com.rometools.fetcher.impl.FeedFetcherCache} mechanism to determine if a feed entry content has changed and send a {@link MotechEvent} if it has.
 */
public class FeedCache implements FeedFetcherCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedCache.class);

    private FeedRecordDataService feedRecordDataService;
    private AtomClientConfigService atomClientConfigService;
    private EventRelay eventRelay;


    public FeedCache(FeedRecordDataService feedRecordDataService, EventRelay eventRelay,
                     AtomClientConfigService atomClientConfigService) {
        this.feedRecordDataService = feedRecordDataService;
        this.eventRelay = eventRelay;
        this.atomClientConfigService = atomClientConfigService;
    }


    /**
     * Get a SyndFeedInfo object from the cache.
     *
     * @param feedUrl The url of the feed
     * @return A SyndFeedInfo or null if it is not in the cache
     */
    @Override
    public SyndFeedInfo getFeedInfo(URL feedUrl) {
        try {
            String url = FeedCacheUtils.urlToString(feedUrl);
            FeedRecord record = feedRecordDataService.findByURL(url);
            if (record != null) {
                return FeedCacheUtils.feedRecordToFeedInfo(record);
            }
        } catch (IOException | ClassNotFoundException | FeedException ex) {
            LOGGER.error("Error reading FeedRecord from the database {}", ex.getMessage());
        }
        return null;
    }


    /**
     * Add a SyndFeedInfo object to the cache
     *
     * @param feedUrl The url of the feed
     * @param feedInfo A SyndFeedInfo for the feed
     */
    @Override
    public synchronized void setFeedInfo(URL feedUrl, SyndFeedInfo feedInfo) {
        try {
            String url = FeedCacheUtils.urlToString(feedUrl);
            String regex = atomClientConfigService.getRegexForFeedUrl(url);
            FeedRecord record = feedRecordDataService.findByURL(url);
            if (record != null) {
                SyndFeedInfo cachedFeedInfo = FeedCacheUtils.feedRecordToFeedInfo(record);
                if (sendMessagesForChangedEntries(url, cachedFeedInfo.getSyndFeed(), feedInfo.getSyndFeed(), regex)) {
                    feedRecordDataService.delete(record);
                    feedRecordDataService.create(FeedCacheUtils.recordFromFeed(url, feedInfo));
                }
            } else {
                sendMessagesForNewFeedData(url, feedInfo.getSyndFeed(), regex);
                feedRecordDataService.create(FeedCacheUtils.recordFromFeed(url, feedInfo));
            }
        } catch (IOException | FeedException | ClassNotFoundException ex) {
            LOGGER.error("Error writing FeedRecord to the database {}", ex.getMessage());
        }
    }


    /**
     * Removes all items from the cache.
     */
    @Override
    public void clear() {
        feedRecordDataService.deleteAll();
    }


    /**
     * Removes the SyndFeedInfo identified by the url from the cache.
     *
     * @return The removed SyndFeedInfo
     */
    @Override
    public SyndFeedInfo remove(URL feedUrl) {
        try {
            String url = FeedCacheUtils.urlToString(feedUrl);
            FeedRecord record = feedRecordDataService.findByURL(url);
            if (record == null) {
                LOGGER.error("Trying to remove feedRecord for inexistent URL {}", feedUrl);
                return null;
            }
            LOGGER.debug("*** removing from cache *** {}", url);
            feedRecordDataService.delete(record);
            return FeedCacheUtils.feedRecordToFeedInfo(record);
        } catch (IOException | ClassNotFoundException | FeedException ex) {
            LOGGER.error("Error removing FeedRecord from the databaase {}", ex.getMessage());
            return null;
        }
    }


    /**
     * Given a regex, will extract the first regex capture group from the given content string
     *
     * @param content the content string from which to extract
     * @param regex   the regular expression that defines what to extract, a blank regex will return null
     * @return the extracted content part, or null
     */
    private String extractContent(String content, String regex) {
        if (StringUtils.isBlank(regex)) {
            return null;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            if (matcher.groupCount() > 0) {
                if (matcher.groupCount() > 1) {
                    LOGGER.warn("Ignoring {} extra regex extract{} for regex {}", matcher.groupCount() - 1,
                            matcher.groupCount() > 2 ? "s" : "", regex);
                }
                return matcher.group(1);
            }
        }

        return null;
    }


    /**
     * Sends a MOTECH event for a feed entry
     *
     * @param entry
     */
    private void sendMessageForFeedEntry(String url, SyndEntry entry, String regex) {
        Map<String, Object> parameters = new HashMap<>();
        String rawContent = "";
        String extractedContent = "";
        parameters.put("url", url);
        parameters.put("published_date", entry.getPublishedDate());
        parameters.put("updated_date", entry.getUpdatedDate());
        if (entry.getContents() != null && entry.getContents().size() > 0) {
            if (entry.getContents().size() > 1) {
                LOGGER.warn("More than one (actually {}) content element for this entry! Discarding all but the first.", entry.getContents().size());
            }
            SyndContent content = entry.getContents().get(0);
            rawContent = content.getValue();
            String extractedContentString = extractContent(content.getValue(), regex);
            if (StringUtils.isNotBlank(extractedContentString)) {
                extractedContent = extractedContentString;
            }
        } else {
            LOGGER.warn("NULL content for entry {}", entry.getUri());
        }
        parameters.put("raw_content", rawContent);
        parameters.put("extracted_content", extractedContent);
        MotechEvent event = new MotechEvent(Constants.FEED_CHANGE_MESSAGE, parameters);
        LOGGER.debug("sending message {}", event);
        eventRelay.sendEventMessage(event);
    }


    /**
     * Sends a MOTECH event for each new feed entry
     *
     * @param feed
     */
    private void sendMessagesForNewFeedData(String url, SyndFeed feed, String regex) {
        LOGGER.debug("Sending {} message{} for new feed {}", feed.getEntries().size(),
                feed.getEntries().size() == 1 ? "" : "s", url);
        for (SyndEntry entry : feed.getEntries()) {
            sendMessageForFeedEntry(url, entry, regex);
        }
    }


    /**
     * Sends a MOTECH event for each changed feed entry and returns true if changes were detected
     *
     * @param cachedFeed
     * @param fetchedFeed
     * @return true if any changes were detected between the cached entry and the fetched entry
     */
    private boolean sendMessagesForChangedEntries(String url, SyndFeed cachedFeed, SyndFeed fetchedFeed, String regex) {
        boolean anyChanges = false;
        for (SyndEntry fetchedEntry : fetchedFeed.getEntries()) {
            boolean foundInCache = false;
            for (SyndEntry cachedEntry : cachedFeed.getEntries()) {
                if (fetchedEntry.getUri().equals(cachedEntry.getUri())) {
                    foundInCache = true;
                    if (areEntriesDifferent(fetchedEntry, cachedEntry)) {
                        LOGGER.debug("Sending message for changed entry {} in feed {}", fetchedEntry.getUri(), url);
                        sendMessageForFeedEntry(url, fetchedEntry, regex);
                        anyChanges = true;
                    }
                }
            }
            if (!foundInCache) {
                LOGGER.debug("Sending message for new entry {} in feed {}", fetchedEntry.getUri(), url);
                sendMessageForFeedEntry(url, fetchedEntry, regex);
                anyChanges = true;
            }
        }

        if (anyChanges == false) {
            LOGGER.debug("No changes detected in feed {}", url);
        }

        return anyChanges;
    }


    private static boolean areEntriesDifferent(SyndEntry e1, SyndEntry e2) {
        if (!ObjectUtils.equals(e1.getUpdatedDate(), e2.getUpdatedDate())) {
            LOGGER.trace("different updated date");
            return true;
        }
        if (!ObjectUtils.equals(e1.getPublishedDate(), e2.getPublishedDate())) {
            LOGGER.trace("different published date");
            return true;
        }
        if (!ObjectUtils.equals(e1.getUri(), e2.getUri())) {
            LOGGER.trace("different uri");
            return true;
        }
        if (!ObjectUtils.equals(e1.getContents(), e2.getContents())) {
            LOGGER.trace("different contents");
            return true;
        }

        return false;
    }
}
