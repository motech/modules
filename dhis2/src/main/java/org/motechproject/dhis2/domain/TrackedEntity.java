package org.motechproject.dhis2.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;

/**
 * Represents a DHIS2 tracked entity type
 */
@Entity
public class TrackedEntity {
    @Field(required = true)
    @Unique
    private String uuid;

    @Field
    private String name;

    public TrackedEntity() { }

    public TrackedEntity(String name, String uuid) {
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
