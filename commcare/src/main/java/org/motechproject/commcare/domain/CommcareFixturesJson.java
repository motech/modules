package org.motechproject.commcare.domain;

import java.util.List;
import java.util.Map;

/**
 * Wrapper class for storing a list of instances of the {@link CommcareFixture} class and their metadata. It's part of
 * the CommCareHQ model.
 */
public class CommcareFixturesJson {

    private Map<String, String> meta;
    private List<CommcareFixture> objects;

    public List<CommcareFixture> getObjects() {
        return this.objects;
    }

    public Map<String, String> getMeta() {
        return this.meta;
    }
}
