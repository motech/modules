package org.motechproject.commcare.domain;

import java.util.List;
import java.util.Map;

public class CommcareDataForwardingEndpointsJson {
    private Map<String, String> meta;
    private List<CommcareDataForwardingEndpoint> objects;

    public List<CommcareDataForwardingEndpoint> getObjects() {
        return this.objects;
    }

    public Map<String, String> getMeta() {
        return this.meta;
    }
}
