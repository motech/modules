package org.motechproject.commcare.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommcareDataForwardingEndpointsJson {
    private Map<String, String> meta = new HashMap<>();
    private List<CommcareDataForwardingEndpoint> objects = new ArrayList<>();

    public List<CommcareDataForwardingEndpoint> getObjects() {
        return this.objects;
    }

    public Map<String, String> getMeta() {
        return this.meta;
    }
}
