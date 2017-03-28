package org.motechproject.openmrs.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Index;

/**
 * Represents the data coming from an OpenMRS Atom Feed.
 */
@Entity(tableName = "atom_feed_record")
public class FeedRecord {

    /**
     * The URL that identifies (and from which to fetch) the OpenMRS Atom Feed
     */
    @Field(required = true)
    @Index
    private String url;

    /**
     * The atom feed data returned by the atom feed server at the given URL
     * Currently a serialized {@link com.rometools.rome.feed.synd.SyndFeed}
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
}