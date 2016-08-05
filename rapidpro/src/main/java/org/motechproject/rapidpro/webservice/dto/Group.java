package org.motechproject.rapidpro.webservice.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * Representation of a RapidPro Group
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    private String group;
    private String name;
    private UUID uuid;
    private int size;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
