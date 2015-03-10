package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Entity
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class ProviderFacilities {

    @Order(column = "provider_facilities_facility_idx")
    @Field(required = true, name = "provider_facilities_facility")
    private List<ProviderFacility> providerFacilities;

    public ProviderFacilities() {
    }

    public ProviderFacilities(List<ProviderFacility> providerFacilities) {
        this.providerFacilities = providerFacilities;
    }

    public List<ProviderFacility> getProviderFacilities() {
        return providerFacilities;
    }

    @XmlElement(name = "facility", required = true)
    public void setProviderFacilities(List<ProviderFacility> providerFacilities) {
        this.providerFacilities = providerFacilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProviderFacilities that = (ProviderFacilities) o;

        if (!providerFacilities.equals(that.providerFacilities)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return providerFacilities.hashCode();
    }

    @Override
    public String toString() {
        return "ProviderFacilities{" +
                "providerFacilities=" + providerFacilities +
                '}';
    }
}
