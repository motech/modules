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
 *     &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *       &lt;sequence>
 *         &lt;element name="name" type="{urn:ihe:iti:csd:2013}name" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="freeBusyURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "names", "languages", "operatingHours", "freeBusyURI", "extensions" })
public class FacilityOrganizationService extends AbstractUniqueID {

    @Order(column = "facility_organization_service_names_idx")
    @Field(name = "facility_organization_service_names")
    @Cascade(delete = true)
    private List<Name> names = new ArrayList<>();

    @Order(column = "facility_organization_service_languages_idx")
    @Field(name = "facility_organization_service_languages")
    @Cascade(delete = true)
    private List<CodedType> languages = new ArrayList<>();

    @Order(column = "facility_organization_service_operatingHours_idx")
    @Field(name = "facility_organization_service_operating_hours")
    @Cascade(delete = true)
    private List<OperatingHours> operatingHours = new ArrayList<>();

    @Field
    private String freeBusyURI;

    @Order(column = "facility_organization_service_extensions_idx")
    @Field(name = "facility_organization_service_extensions")
    @Cascade(delete = true)
    private List<Extension> extensions = new ArrayList<>();

    public FacilityOrganizationService() {
    }

    public FacilityOrganizationService(String entityID) {
        setEntityID(entityID);
    }

    public FacilityOrganizationService(String entityID, List<Name> names, List<CodedType> languages, List<OperatingHours> operatingHours, String freeBusyURI, List<Extension> extensions) {
        setEntityID(entityID);
        this.names = names;
        this.languages = languages;
        this.operatingHours = operatingHours;
        this.freeBusyURI = freeBusyURI;
        this.extensions = extensions;
    }

    public List<Name> getNames() {
        return names;
    }

    @XmlElement(name = "name")
    public void setNames(List<Name> names) {
        this.names = names;
    }

    public List<CodedType> getLanguages() {
        return languages;
    }

    @XmlElement(name = "language")
    public void setLanguages(List<CodedType> languages) {
        this.languages = languages;
    }

    public List<OperatingHours> getOperatingHours() {
        return operatingHours;
    }

    @XmlElement
    public void setOperatingHours(List<OperatingHours> operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getFreeBusyURI() {
        return freeBusyURI;
    }

    @XmlElement
    public void setFreeBusyURI(String freeBusyURI) {
        this.freeBusyURI = freeBusyURI;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        FacilityOrganizationService that = (FacilityOrganizationService) o;

        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        if (freeBusyURI != null ? !freeBusyURI.equals(that.freeBusyURI) : that.freeBusyURI != null) {
            return false;
        }
        if (languages != null ? !languages.equals(that.languages) : that.languages != null) {
            return false;
        }
        if (names != null ? !names.equals(that.names) : that.names != null) {
            return false;
        }
        if (operatingHours != null ? !operatingHours.equals(that.operatingHours) : that.operatingHours != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (names != null ? names.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (operatingHours != null ? operatingHours.hashCode() : 0);
        result = 31 * result + (freeBusyURI != null ? freeBusyURI.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (names != null && !names.isEmpty()) {
            return names.toString();
        }
        return super.toString();
    }
}
