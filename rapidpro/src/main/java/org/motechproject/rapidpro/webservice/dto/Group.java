package org.motechproject.rapidpro.webservice.dto;

import java.io.Serializable;

/**
 * Representation of a Rapidpro Group
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    private String group;
    private String name;
    private String uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
