package org.motechproject.atomclient.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Index;

/**
 * Represents the data coming from an atom feed.
 */
@Entity(tableName = "atomclient_feed_record")
public class FeedRecord {

    /**
     * The URL that identifies (and from which to fetch) the atom feed
     */
    @Field(required = true)
    @Index
    private String url;

    /**
     * The atom feed data returned by the atom feed server at the given URL
     * Currently a serialized {@link com.rometools.rome.feed.synd.SyndFeed}
     *
     * Note: we may eventually want to store the data (corresponding to {@link com.rometools.rome.feed.synd.SyndFeed}) into separate database entities.
     */
    @Field(type = "TEXT")
    private String data;


    public FeedRecord(String url, String data) {
        this.url = url;
        this.data = data;
    }


    public String getUrl() {
        return url;
    }


    public String getData() {
        return data;
    }

    public Integer getPage(String data) {
        int beginIndex = data.indexOf("via");
        beginIndex = data.indexOf("patient", beginIndex) + "patient/".length();
        int endIndex = data.indexOf('/', beginIndex) - "\" ".length();
        return Integer.parseInt(data.substring(beginIndex, endIndex));
    }
}
