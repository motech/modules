package org.motechproject.dhis2.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;

/**
 * Represents a DHIS2 tracked entity attribute
 */
@Entity
public class TrackedEntityAttribute {
    @Field(required = true)
    @Unique
    private String uuid;

    @Field
    private String name;

    public TrackedEntityAttribute() { }

    public TrackedEntityAttribute(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
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
}
