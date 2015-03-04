package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Entity
@XmlType
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

    @XmlAttribute(required = true)
    public void setType(String type) {
        this.type = type;
    }

    public String getUrn() {
        return urn;
    }

    @XmlAttribute(required = true)
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
