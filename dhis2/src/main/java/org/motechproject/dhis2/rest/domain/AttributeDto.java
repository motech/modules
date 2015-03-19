package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Objects;

/**
 * A class to model the DHIS2 API's attributes resource.
 */
@JsonInclude(Include.NON_NULL)
public class AttributeDto {
    private String displayName;
    private String attribute;
    private String type;
    private String code;
    private String value;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, attribute, type, code, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AttributeDto other = (AttributeDto) obj;
        return Objects.equals(this.displayName, other.displayName)
                && Objects.equals(this.attribute, other.attribute)
                && Objects.equals(this.type, other.type)
                && Objects.equals(this.code, other.code)
                && Objects.equals(this.value, other.value);
    }
}
