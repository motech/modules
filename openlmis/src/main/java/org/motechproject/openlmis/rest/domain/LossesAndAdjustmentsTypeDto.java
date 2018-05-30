package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's LossesAndAdjustments resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LossesAndAdjustmentsTypeDto {
    
    private Integer id; //(integer, optional),
    private String description; //(string, optional),
    private String name; //(string, optional),
    private Integer displayOrder; //(integer, optional),
    private Boolean additive; //(boolean, optional)
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getAdditive() {
        return additive;
    }

    public void setAdditive(Boolean additive) {
        this.additive = additive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, displayOrder, additive);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final LossesAndAdjustmentsTypeDto other = (LossesAndAdjustmentsTypeDto) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.displayOrder, other.displayOrder)
                && Objects.equals(this.additive, other.additive);
    }
}
