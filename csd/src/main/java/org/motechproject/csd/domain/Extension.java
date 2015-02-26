package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class Extension {

    @Field(required = true)
    private String type;

    @Field(required = true)
    private String urn;

    public Extension() {
    }

    public Extension(String type, String urn) {
        this.type = type;
        this.urn = urn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Extension extension = (Extension) o;

        if (!type.equals(extension.type)) {
            return false;
        }
        if (!urn.equals(extension.urn)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + urn.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Extension{" +
                "type='" + type + '\'' +
                ", urn='" + urn + '\'' +
                '}';
    }
}
