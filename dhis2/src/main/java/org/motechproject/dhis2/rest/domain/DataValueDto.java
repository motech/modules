package org.motechproject.dhis2.rest.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Objects;

/**
 * A class to model data values associated with program stage events in the DHIS2 API.
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DataValueDto {
    private String value;
    private String dataElement;
    private String period;
    private String orgUnit;
    private String categoryOptionCombo;
    private String comment;

    public String getCategoryOptionCombo() {
        return categoryOptionCombo;
    }

    public void setCategoryOptionCombo(String categoryOptionCombo) {
        this.categoryOptionCombo = categoryOptionCombo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDataElement() {
        return dataElement;
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, dataElement, period, orgUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DataValueDto other = (DataValueDto) obj;
        return Objects.equals(this.value, other.value)
                && Objects.equals(this.dataElement, other.dataElement);
    }
}
