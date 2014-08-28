package org.motechproject.commcare.domain;

import java.util.ArrayList;
import java.util.List;

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
