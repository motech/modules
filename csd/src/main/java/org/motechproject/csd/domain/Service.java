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
 * <p>Java class for service complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="service">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *       &lt;sequence>
 *         &lt;element name="codedType" type="{urn:ihe:iti:csd:2013}codedtype"/>
 *         &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="record" type="{urn:ihe:iti:csd:2013}record"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity(maxFetchDepth = 1)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "codedType", "extensions", "record" })
public class Service extends BaseMainEntity {

    @UIDisplayable(position = 0)
    @Field(required = true)
    @Cascade(delete = true)
    private CodedType codedType;

    @UIDisplayable(position = 2)
    @Field(name = "service_extensions")
    @Cascade(delete = true)
    private Set<Extension> extensions = new HashSet<>();

    public Service() {
    }

    public Service(String entityID, CodedType codedType, Record record) {
        setEntityID(entityID);
        this.codedType = codedType;
        setRecord(record);
    }

    public Service(String entityID, CodedType codedType, Set<Extension> extensions, Record record) {
        setEntityID(entityID);
        this.codedType = codedType;
        this.extensions = extensions;
        setRecord(record);
    }

    public CodedType getCodedType() {
        return codedType;
    }

    @XmlElement(required = true)
    public void setCodedType(CodedType codedType) {
        this.codedType = codedType;
    }

    public Set<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(Set<Extension> extensions) {
        this.extensions = extensions;
    }

    @Override
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

        Service service = (Service) o;

        if (!codedType.equals(service.codedType)) {
            return false;
        }
        if (extensions != null ? !extensions.equals(service.extensions) : service.extensions != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (codedType != null ? codedType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return codedType.toString();
    }
}
