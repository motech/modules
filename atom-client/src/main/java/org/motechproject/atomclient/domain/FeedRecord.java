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
    private String url; //NOPMD UnusedPrivateField

    @Field(required = true)
    private byte[] info; //NOPMD UnusedPrivateField

    public FeedRecord(String url, byte[] info) { //NOPMD ArrayIsStoredDirectly
        this.url = url;
        this.info = info;
    }
}
