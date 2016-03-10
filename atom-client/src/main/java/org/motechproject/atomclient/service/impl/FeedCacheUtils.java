package org.motechproject.atomclient.service.impl;

import com.rometools.fetcher.impl.SyndFeedInfo;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import org.motechproject.atomclient.domain.FeedRecord;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;


/**
 * FeedCache static helper methods
 */
public final class FeedCacheUtils {

    private FeedCacheUtils() { }


    public static String feedToString(SyndFeed feed) throws FeedException, UnsupportedEncodingException {
        SyndFeedOutput syndFeedOutput = new SyndFeedOutput();
        return syndFeedOutput.outputString(feed);
    }


    public static SyndFeed feedFromString(String xml) throws IOException, ClassNotFoundException, FeedException {
        SyndFeedInput syndFeedInput = new SyndFeedInput();
        return syndFeedInput.build(new StringReader(xml));
    }


    public static FeedRecord recordFromFeed(String url, SyndFeedInfo info) throws IOException, FeedException {
        return new FeedRecord(url, feedToString(info.getSyndFeed()));
    }


    public static String urlToString(URL url) throws IOException {
        return url.toExternalForm();
    }


    public static SyndFeedInfo feedRecordToFeedInfo(FeedRecord record) throws IOException, ClassNotFoundException, FeedException {
        SyndFeedInfo info = new SyndFeedInfo();
        info.setUrl(new URL(record.getUrl()));
        info.setSyndFeed(feedFromString(record.getData()));
        return info;
    }
}
