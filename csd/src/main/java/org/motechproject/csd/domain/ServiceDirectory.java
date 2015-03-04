package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Entity
@XmlType
public class ServiceDirectory {

    @Order(column = "service_directory_services_idx")
    @Field(name = "service_directory_services")
    private List<Service> services;

    public ServiceDirectory() {
    }

    public ServiceDirectory(List<Service> services) {
        this.services = services;
    }

    public List<Service> getServices() {
        return services;
    }

    @XmlElement(name = "service")
    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceDirectory that = (ServiceDirectory) o;

        if (services != null ? !services.equals(that.services) : that.services != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return services != null ? services.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ServiceDirectory{" +
                "services=" + services +
                '}';
    }
}
