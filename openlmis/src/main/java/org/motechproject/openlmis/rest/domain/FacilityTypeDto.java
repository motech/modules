package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's FacilityType resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacilityTypeDto {
    
    private Integer id; //(integer, optional),
    private Double nominalEop; //(number, optional),
    private Integer levelId; //(integer, optional),
    private String description; //(string, optional),
    private String name; //(string, optional),
    private Boolean active; //(boolean, optional),
    private Integer displayOrder; //(integer, optional),
    private String code; //(string, optional),
    private Integer nominalMaxMonth; //(integer, optional)
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getNominalEop() {
        return nominalEop;
    }

    public void setNominalEop(Double nominalEop) {
        this.nominalEop = nominalEop;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNominalMaxMonth() {
        return nominalMaxMonth;
    }

    public void setNominalMaxMonth(Integer nominalMaxMonth) {
        this.nominalMaxMonth = nominalMaxMonth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nominalEop, levelId, description,
                name, active, displayOrder, code, nominalMaxMonth);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FacilityTypeDto other = (FacilityTypeDto) obj;
        return equalsDetails(other)
                && Objects.equals(this.nominalEop, other.nominalEop)
                && Objects.equals(this.levelId, other.levelId)
                && Objects.equals(this.active, other.active)
                && Objects.equals(this.displayOrder, other.displayOrder)
                && Objects.equals(this.nominalMaxMonth, other.nominalMaxMonth);
    }
    
    private boolean equalsDetails(FacilityTypeDto other) {
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.code, other.code);
    }
}
