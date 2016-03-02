package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's ISA resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ISADto {
    
    private Integer adjustmentValue; //(integer, optional),
    private Integer id; //(integer, optional),
    private Integer maximumValue; //(integer, optional),
    private Integer populationSource; //(integer, optional),
    private Integer minimumValue; //(integer, optional),
    private Integer wastageFactor; //(number, optional),
    private Integer dosesPerYear; //(integer, optional),
    private Double bufferPercentage; //(number, optional),
    private Double whoRatio; //(number, optional)
    
    public Integer getAdjustmentValue() {
        return adjustmentValue;
    }

    public void setAdjustmentValue(Integer adjustmentValue) {
        this.adjustmentValue = adjustmentValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(Integer maximumValue) {
        this.maximumValue = maximumValue;
    }

    public Integer getPopulationSource() {
        return populationSource;
    }

    public void setPopulationSource(Integer populationSource) {
        this.populationSource = populationSource;
    }

    public Integer getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(Integer minimumValue) {
        this.minimumValue = minimumValue;
    }

    public Integer getWastageFactor() {
        return wastageFactor;
    }

    public void setWastageFactor(Integer wastageFactor) {
        this.wastageFactor = wastageFactor;
    }

    public Integer getDosesPerYear() {
        return dosesPerYear;
    }

    public void setDosesPerYear(Integer dosesPerYear) {
        this.dosesPerYear = dosesPerYear;
    }

    public Double getBufferPercentage() {
        return bufferPercentage;
    }

    public void setBufferPercentage(Double bufferPercentage) {
        this.bufferPercentage = bufferPercentage;
    }

    public Double getWhoRatio() {
        return whoRatio;
    }

    public void setWhoRatio(Double whoRatio) {
        this.whoRatio = whoRatio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(adjustmentValue, id, maximumValue, 
                populationSource, minimumValue, wastageFactor, 
                dosesPerYear, bufferPercentage, whoRatio);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ISADto other = (ISADto) obj;
        return equalsRange(other) && Objects.equals(this.id, other.id)
                && Objects.equals(this.populationSource, other.populationSource)
                && Objects.equals(this.dosesPerYear, other.dosesPerYear)
                && Objects.equals(this.bufferPercentage, other.bufferPercentage)
                && Objects.equals(this.whoRatio, other.whoRatio);
    }
    
    private boolean equalsRange(ISADto other) {
        return Objects.equals(this.adjustmentValue, other.adjustmentValue)
              && Objects.equals(this.minimumValue, other.minimumValue)
              && Objects.equals(this.maximumValue, other.maximumValue)
              && Objects.equals(this.wastageFactor, other.wastageFactor);
     }
}