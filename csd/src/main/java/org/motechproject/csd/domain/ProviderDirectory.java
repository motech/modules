package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Entity
@XmlType
public class ProviderDirectory {

    @Order(column = "provider_directory_providers_idx")
    @Field(name = "provider_directory_providers")
    private List<Provider> providers;

    public ProviderDirectory() {
    }

    public ProviderDirectory(List<Provider> providers) {
        this.providers = providers;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    @XmlElement(name = "provider")
    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProviderDirectory that = (ProviderDirectory) o;

        if (providers != null ? !providers.equals(that.providers) : that.providers != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return providers != null ? providers.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProviderDirectory{" +
                "providers=" + providers +
                '}';
    }
}
