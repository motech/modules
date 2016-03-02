package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's GeographicLevel resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeographicLevelDto {
    
    private Integer id; //(integer, optional),
    private Integer levelNumber; //(integer, optional),
    private String name; //(string, optional),
    private String code; //(string, optional)
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(Integer levelNumber) {
        this.levelNumber = levelNumber;
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

    @Override
    public int hashCode() {
        return Objects.hash(id, levelNumber, name, code);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final GeographicLevelDto other = (GeographicLevelDto) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.levelNumber, other.levelNumber)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.code, other.code);
    }
}
