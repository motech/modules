package org.motechproject.commcare.domain;

import java.util.List;
import java.util.Map;

/**
 * A wrapper class for storing list of instances of the {@link CommcareUser} class and their metadata. It's part of the
 * MOTECH model.
 */
public class CommcareUsersJson {

    private Map<String, String> meta;
    private List<CommcareUser> objects;

    public List<CommcareUser> getObjects() {
        return this.objects;
    }

    public Map<String, String> getMeta() {
        return this.meta;
    }
}
