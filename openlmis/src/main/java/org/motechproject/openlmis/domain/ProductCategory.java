package org.motechproject.openlmis.domain;

import javax.jdo.annotations.Unique;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

/**
 * Represents a OpenLMIS Product Category
 */

@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureOpenlmis" })
public class ProductCategory {
  
    @Field(required = true)
    @Unique
    private Integer openlmisid;
    
    @Field(required = true)
    @Unique
    private String code;
    
    @Field
    private String name;

    @Field
    private Integer displayOrder;

    public Integer getOpenlmisid() {
        return openlmisid;
    }

    public void setOpenlmisid(Integer openlmisid) {
        this.openlmisid = openlmisid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }


}
