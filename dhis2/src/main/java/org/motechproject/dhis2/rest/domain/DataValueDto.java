package org.motechproject.dhis2.rest.domain;

import java.util.Objects;

/**
 * A class to model data values associated with program stage events in the DHIS2 API.
 */
public class DataValueDto {
    private String value;
    private String dataElement;

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

    @Override
    public int hashCode() {
        return Objects.hash(value, dataElement);
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
