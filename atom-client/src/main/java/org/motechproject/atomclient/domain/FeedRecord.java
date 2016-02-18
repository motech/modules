package org.motechproject.atomclient.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Index;

@Entity(tableName = "atomclient_feed_record")
public class FeedRecord {
    @Field(required = true)
    @Index
    private String url;

    @Field(required = true)
    private Long lastModified;

    @Field(type = "TEXT")
    private String data;


    public FeedRecord(String url, Long lastModified, String data) {
        this.url = url;
        this.lastModified = lastModified;
        this.data = data;
    }


    public String getUrl() {
        return url;
    }


    public Long getLastModified() {
        return lastModified;
    }


    public String getData() {
        return data;
    }
}
