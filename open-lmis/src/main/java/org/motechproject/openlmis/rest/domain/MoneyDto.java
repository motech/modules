package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's Money resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyDto {
    
    private Double value; //(number, optional),
    private Boolean negative; //(boolean, optional)
    

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean getNegative() {
        return negative;
    }

    public void setNegative(Boolean negative) {
        this.negative = negative;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, negative);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MoneyDto other = (MoneyDto) obj;
        return Objects.equals(this.value, other.value)
                && Objects.equals(this.negative, other.negative);
    }
}
