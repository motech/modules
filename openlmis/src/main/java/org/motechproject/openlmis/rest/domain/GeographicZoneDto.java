package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's GeographicZone resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeographicZoneDto {
    
    private Integer id; //(integer, optional),
    private Integer levelId; //(GeographicLevel, optional),
    private String name; //(string, optional),
    private Integer parentId;
    private Double longitude; //(number, optional),
    private Double latitude; //(number, optional),
    private String code; //(string, optional),
    private Integer catchmentPopulation; //(integer, optional)
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    @Override
    public int hashCode() {
        return Objects.hash(id, levelId, name, parentId,
                longitude, latitude, code, catchmentPopulation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final GeographicZoneDto other = (GeographicZoneDto) obj;
        return equalsDetails(other) && Objects.equals(this.id, other.id)
                && Objects.equals(this.levelId, other.levelId)
                && Objects.equals(this.parentId, other.parentId)
                && Objects.equals(this.catchmentPopulation, 
                        other.catchmentPopulation);
    }
    
    private boolean equalsDetails(GeographicZoneDto other) {
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.longitude, other.longitude)
                && Objects.equals(this.latitude, other.latitude)
                && Objects.equals(this.code, other.code);
                
    }
}
