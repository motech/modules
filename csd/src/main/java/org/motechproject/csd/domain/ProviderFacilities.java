package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class ProviderFacilities {

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
