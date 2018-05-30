package org.motechproject.openlmis.domain;

import javax.jdo.annotations.Unique;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

/**
 * Represents a OpenLMIS FacilityType
 */

@Entity
@Access(value = SecurityMode.PERMISSIONS, members = { "configureOpenlmis" })
public class FacilityType {

    @Field(required = true)
    @Unique
    private Integer openlmisid;

    @Field
    private String name;
    
    @Field
    private String code;
    
    @Field
    private String description;
    
    @Field
    private Integer nominalMaxMonth;

    @Field
    private Double nominalEop;

    @Field
    private GeographicLevel level;

    @Field
    private Boolean active;

    @Field
    private Integer displayOrder;

    public Integer getOpenlmisid() {
        return openlmisid;
    }

    public void setOpenlmisid(Integer openlmisid) {
        this.openlmisid = openlmisid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNominalMaxMonth() {
        return nominalMaxMonth;
    }

    public void setNominalMaxMonth(Integer nominalMaxMonth) {
        this.nominalMaxMonth = nominalMaxMonth;
    }

    public Double getNominalEop() {
        return nominalEop;
    }

    public void setNominalEop(Double nominalEop) {
        this.nominalEop = nominalEop;
    }

    public GeographicLevel getLevel() {
        return level;
    }

    public void setLevel(GeographicLevel level) {
        this.level = level;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

}
