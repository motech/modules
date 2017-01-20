package org.motechproject.dhis2.domain;

import org.apache.commons.lang.ObjectUtils;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;
import java.util.Objects;

/**
 * Represents a DHIS2 tracked entity attribute
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureDhis"})
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

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof TrackedEntityAttribute)) {
            return false;
        }

        TrackedEntityAttribute other = (TrackedEntityAttribute) o;

        return ObjectUtils.equals(uuid, other.uuid) && ObjectUtils.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name);
    }
}
