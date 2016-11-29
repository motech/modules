package org.motechproject.dhis2.domain;

import org.motechproject.dhis2.dto.OrgUnitDto;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;

/**
 * Represents a DHIS2 Organisation Unit
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureDhis"})
public class OrgUnit {
    @Field(required = true)
    @Unique
    private String uuid;

    @Field
    private String name;

    public OrgUnit() { }

    public OrgUnit(String name, String uuid) {
        this.uuid = uuid;
        this.name = name;
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

    public OrgUnitDto toDto () {
        return new OrgUnitDto(name, uuid);
    }

}
