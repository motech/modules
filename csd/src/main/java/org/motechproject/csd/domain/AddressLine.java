package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class AddressLine {

    @Field
    private String value;

    @Field
    private String component;


    public AddressLine() {
    }

    public AddressLine(String value) {
        this.value = value;
    }

    public AddressLine(String value, String component) {
        this.value = value;
        this.component = component;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddressLine that = (AddressLine) o;

        if (!value.equals(that.value)) {
            return false;
        }
        if (component != null ? !component.equals(that.component) : that.component != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + (component != null ? component.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AddressLine{" +
                "value='" + value + '\'' +
                ", component='" + component + '\'' +
                '}';
    }
}
