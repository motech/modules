package org.motechproject.atomclient.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Index;

@Entity
public class FeedRecord {
    @Field(required = true)
    @Column(length = 2083)
    @Index
    private String url; //NOPMD UnusedPrivateField


    @Field
    private String feedId;

    @Field
    private String feedUrl;

    @Field
    private DateTime feedLastMofified;

    @Field
    private String feedETag;

    @Field(required = true)
    private byte[] feedData; //NOPMD UnusedPrivateField

    public FeedRecord(String url, String feedId, String feelUrl, DateTime feedLastModified, String feedETag,
                      byte[] feedData) { //NOPMD ArrayIsStoredDirectly
        this.url = url;
        this.feedId = feedId;
        this.feedUrl = feelUrl;
        this.feedLastMofified = feedLastModified;
        this.feedETag = feedETag;
        this.feedData = feedData;
    }

    public String getUrl() {
        return url;
    }

    public String getFeedId() {
        return feedId;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public DateTime getFeedLastMofified() {
        return feedLastMofified;
    }

    public String getFeedETag() {
        return feedETag;
    }

    public byte[] getFeedData() {
        return feedData; //NOPMD ArrayIsStoredDirectly
    }
}
