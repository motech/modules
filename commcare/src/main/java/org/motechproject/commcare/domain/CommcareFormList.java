package org.motechproject.commcare.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of forms with metadata, as returned by Commcare.
 */
public class CommcareFormList {

    /**
     * The metadata object.
     */
    private CommcareMetadataJson meta;

    /**
     * The actual forms.
     */
    private List<CommcareForm> objects = new ArrayList<>();

    /**
     * @return the metadata associated with this list
     */
    public CommcareMetadataJson getMeta() {
        return meta;
    }

    /**
     * @param meta the metadata associated with this list
     */
    public void setMeta(CommcareMetadataJson meta) {
        this.meta = meta;
    }

    /**
     * @return the list of forms
     */
    public List<CommcareForm> getObjects() {
        return objects;
    }

    /**
     * @param objects the list of forms
     */
    public void setObjects(List<CommcareForm> objects) {
        this.objects = objects;
    }
}
