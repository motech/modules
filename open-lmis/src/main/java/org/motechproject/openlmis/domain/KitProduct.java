package org.motechproject.openlmis.domain;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

/**
 * Represents a OpenLMIS KitProduct
 */

@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureOpenlmis" })
public class KitProduct {
    
    @Field
    private Integer openlmisid;
    
    @Field
    private Product product;
    
    @Field
    private String kitCode;
    
    @Field
    private Integer quantity;

    public Integer getOpenlmisid() {
        return openlmisid;
    }

    public void setOpenlmisid(Integer openlmisid) {
        this.openlmisid = openlmisid;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getKitCode() {
        return kitCode;
    }

    public void setKitCode(String kitCode) {
        this.kitCode = kitCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
