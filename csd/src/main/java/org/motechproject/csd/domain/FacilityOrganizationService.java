package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashSet;
import java.util.Set;

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

    @UIDisplayable(position = 0)
    @Field(name = "facility_organization_service_names")
    @Cascade(delete = true)
    private Set<Name> names = new HashSet<>();

    @UIDisplayable(position = 2)
    @Field(name = "facility_organization_service_languages")
    @Cascade(delete = true)
    private Set<CodedType> languages = new HashSet<>();

    @UIDisplayable(position = 1)
    @Field(name = "facility_organization_service_operating_hours")
    @Cascade(delete = true)
    private Set<OperatingHours> operatingHours = new HashSet<>();

    @UIDisplayable(position = 3)
    @Field
    private String freeBusyURI;

    @Field(name = "facility_organization_service_extensions")
    @Cascade(delete = true)
    private Set<Extension> extensions = new HashSet<>();

    public FacilityOrganizationService() {
    }

    public FacilityOrganizationService(String entityID) {
        setEntityID(entityID);
    }

    public FacilityOrganizationService(String entityID, Set<Name> names, Set<CodedType> languages,
                                       Set<OperatingHours> operatingHours, String freeBusyURI, Set<Extension> extensions) {
        setEntityID(entityID);
        this.names = names;
        this.languages = languages;
        this.operatingHours = operatingHours;
        this.freeBusyURI = freeBusyURI;
        this.extensions = extensions;
    }

    public Set<Name> getNames() {
        return names;
    }

    @XmlElement(name = "name")
    public void setNames(Set<Name> names) {
        this.names = names;
    }

    public Set<CodedType> getLanguages() {
        return languages;
    }

    @XmlElement(name = "language")
    public void setLanguages(Set<CodedType> languages) {
        this.languages = languages;
    }

    public Set<OperatingHours> getOperatingHours() {
        return operatingHours;
    }

    @XmlElement
    public void setOperatingHours(Set<OperatingHours> operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getFreeBusyURI() {
        return freeBusyURI;
    }

    @XmlElement
    public void setFreeBusyURI(String freeBusyURI) {
        this.freeBusyURI = freeBusyURI;
    }

    public Set<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(Set<Extension> extensions) {
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

        if (names != null ? !names.equals(that.names) : that.names != null) {
            return false;
        }
        if (freeBusyURI != null ? !freeBusyURI.equals(that.freeBusyURI) : that.freeBusyURI != null) {
            return false;
        }
        if (languages != null ? !languages.equals(that.languages) : that.languages != null) {
            return false;
        }
        if (operatingHours != null ? !operatingHours.equals(that.operatingHours) : that.operatingHours != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
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
