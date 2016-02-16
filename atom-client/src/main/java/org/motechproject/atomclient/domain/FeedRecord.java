package org.motechproject.atomclient.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Index;

@Entity
public class FeedRecord {
    @Field(required = true)
    @Column(length = 2083)
    @Index
    private String url;

    @Field(required = true)
    private String feedId;

    @Field(required = true)
    @Column(length = 2083)
    private String feedUrl;

    @Field(required = true)
    private Long feedLastModified;

    @Field
    private String feedETag;

    @Field(required = true)
    private byte[] feedData;

    public FeedRecord(String url, String feedId, String feelUrl, Long feedLastModified, String feedETag,
                      byte[] feedData) { //NOPMD ArrayIsStoredDirectly
        this.url = url;
        this.feedId = feedId;
        this.feedUrl = feelUrl;
        this.feedLastModified = feedLastModified;
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

    public Long getFeedLastModified() {
        return feedLastModified;
    }

    public String getFeedETag() {
        return feedETag;
    }

    public byte[] getFeedData() {
        return feedData; //NOPMD ArrayIsStoredDirectly
    }
}
