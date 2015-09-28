package org.motechproject.commcare.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for storing list of instances of the {@link CommcareDataForwardingEndpoint} class and their metadata.
 * It's part of the CommCareHQ model.
 */
public class CommcareDataForwardingEndpointsJson {

    private CommcareMetadataJson meta;

    private List<CommcareDataForwardingEndpoint> objects = new ArrayList<>();

    public List<CommcareDataForwardingEndpoint> getObjects() {
        return this.objects;
    }

    public CommcareMetadataJson getMeta() {
        return this.meta;
    }
}
