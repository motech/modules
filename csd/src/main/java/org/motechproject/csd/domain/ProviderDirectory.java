package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="provider" type="{urn:ihe:iti:csd:2013}provider" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class ProviderDirectory {

    @Order(column = "provider_directory_providers_idx")
    @Field(name = "provider_directory_providers")
    @Cascade(delete = true)
    private List<Provider> providers = new ArrayList<>();

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
