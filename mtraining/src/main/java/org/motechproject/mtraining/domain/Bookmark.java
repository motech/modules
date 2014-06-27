package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Map;

/**
 * Bookmark object to store the progress for the user
 */
@Entity
public class Bookmark {

    @Field
    private String externalId;

    @Field
    private Map<String, Object> progress;

    public Bookmark() {
        this(null, null);
    }

    public Bookmark(String externalId, Map<String, Object> progress) {
        this.externalId = externalId;
        this.progress = progress;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Map<String, Object> getProgress() {
        return progress;
    }

    public void setProgress(Map<String, Object> progress) {
        this.progress = progress;
    }
}
