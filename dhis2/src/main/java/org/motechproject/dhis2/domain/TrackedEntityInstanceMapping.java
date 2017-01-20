package org.motechproject.dhis2.domain;

import org.apache.commons.lang.ObjectUtils;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;
import java.util.Objects;

/**
 * Maps an External ID to a DHIS2 ID
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureDhis"})
public class TrackedEntityInstanceMapping {

    @Field
    private String externalName;

    @Field
    private String dhis2Name;

    @Field
    @Unique
    private String dhis2Uuid;

    public TrackedEntityInstanceMapping() { }

    public TrackedEntityInstanceMapping(String externalName, String dhis2Uuid) {
        this.dhis2Uuid = dhis2Uuid;
        this.externalName = externalName;
    }

    public String getDhis2Name() {
        return dhis2Name;
    }

    public void setDhis2Name(String dhis2Name) {
        this.dhis2Name = dhis2Name;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getDhis2Uuid() {
        return dhis2Uuid;
    }

    public void setDhis2Uuid(String dhis2Uuid) {
        this.dhis2Uuid = dhis2Uuid;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof TrackedEntityInstanceMapping)) {
            return false;
        }

        TrackedEntityInstanceMapping other = (TrackedEntityInstanceMapping) o;

        return ObjectUtils.equals(externalName, other.externalName) &&
                ObjectUtils.equals(dhis2Name, other.dhis2Name) &&
                ObjectUtils.equals(dhis2Uuid, other.dhis2Uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalName, dhis2Name, dhis2Uuid);
    }
}
