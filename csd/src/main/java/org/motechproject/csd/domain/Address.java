package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Entity
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class Address {

    @Field(required = true)
    private List<AddressLine> addressLines;

    @Field
    private String type;

    public Address() {
    }

    public Address(String type) {
        this.type = type;
    }

    public Address(List<AddressLine> addressLines, String type) {
        this.addressLines = addressLines;
        this.type = type;
    }

    public List<AddressLine> getAddressLines() {
        return addressLines;
    }

    @XmlElement(name = "addressLine", required = true)
    public void setAddressLines(List<AddressLine> addressLines) {
        this.addressLines = addressLines;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }

        Address address = (Address) o;

        if (!addressLines.equals(address.addressLines)) {
            return false;
        }
        if (type != null ? !type.equals(address.type) : address.type != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = addressLines.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressLines=" + addressLines +
                ", type='" + type + '\'' +
                '}';
    }
}
