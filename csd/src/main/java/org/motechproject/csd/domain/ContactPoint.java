package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contactPoint complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="contactPoint">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codedType" type="{urn:ihe:iti:csd:2013}codedtype"/>
 *         &lt;element name="equipment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="purpose" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "codedType", "equipment", "purpose", "certificate" })
public class ContactPoint {

    @Field(required = true)
    @Cascade(delete = true)
    private CodedType codedType;

    @Field
    private String equipment;

    @Field
    private String purpose;

    @Field
    private String certificate;

    public ContactPoint() {
    }

    public ContactPoint(CodedType codedType) {
        this.codedType = codedType;
    }

    public ContactPoint(CodedType codedType, String equipment, String purpose, String certificate) {
        this.codedType = codedType;
        this.equipment = equipment;
        this.purpose = purpose;
        this.certificate = certificate;
    }

    public CodedType getCodedType() {
        return codedType;
    }

    @XmlElement(required = true)
    public void setCodedType(CodedType codedType) {
        this.codedType = codedType;
    }

    public String getEquipment() {
        return equipment;
    }

    @XmlElement
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getPurpose() {
        return purpose;
    }

    @XmlElement
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCertificate() {
        return certificate;
    }

    @XmlElement
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactPoint that = (ContactPoint) o;

        if (certificate != null ? !certificate.equals(that.certificate) : that.certificate != null) {
            return false;
        }
        if (!codedType.equals(that.codedType)) {
            return false;
        }
        if (equipment != null ? !equipment.equals(that.equipment) : that.equipment != null) {
            return false;
        }
        if (purpose != null ? !purpose.equals(that.purpose) : that.purpose != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = codedType.hashCode();
        result = 31 * result + (equipment != null ? equipment.hashCode() : 0);
        result = 31 * result + (purpose != null ? purpose.hashCode() : 0);
        result = 31 * result + (certificate != null ? certificate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return codedType.toString();
    }
}
