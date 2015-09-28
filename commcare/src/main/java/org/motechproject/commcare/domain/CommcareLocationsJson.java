package org.motechproject.commcare.domain;

import java.util.List;

/**
 * Representation of a JSON response, retrieved from CommCareHQ for Locations API. Contains {@link CommcareMetadataJson}
 * and a list of {@link CommcareLocation}.
 */
public class CommcareLocationsJson {

    private CommcareMetadataJson meta;

    private List<CommcareLocation> objects;

    public List<CommcareLocation> getObjects() {
        return this.objects;
    }

    public CommcareMetadataJson getMeta() {
        return this.meta;
    }
}
