package org.motechproject.openlmis.domain;

import javax.jdo.annotations.Unique;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

/**
 * Represents a OpenLMIS GeographicZone
 */

@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureOpenlmis" })
public class GeographicZone {
    
    @Field(required = true)
    @Unique
    private Integer openlmisid;

    @Field
    private String name;
    
    @Field(required = true)
    @Unique
    private String code;
    
    @Field
    private GeographicZone parent;
    
    @Field
    private Integer catchmentPopulation;
    
    @Field
    private Double latitude;
    
    @Field
    private Double longitude;

    @Field
    private GeographicLevel level;
    
    public Integer getOpenlmisid() {
        return openlmisid;
    }

    public void setOpenlmisid(Integer openlmisid) {
        this.openlmisid = openlmisid;
    }

    public GeographicLevel getLevel() {
        return level;
    }

    public void setLevel(GeographicLevel level) {
        this.level = level;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public GeographicZone getParent() {
        return parent;
    }

    public void setParent(GeographicZone parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCatchmentPopulation() {
        return catchmentPopulation;
    }

    public void setCatchmentPopulation(Integer catchmentPopulation) {
        this.catchmentPopulation = catchmentPopulation;
    }

}
