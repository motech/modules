package org.motechproject.csd.domain;

import org.joda.time.DateTime;
import org.motechproject.csd.adapters.DateAdapter;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Java class for credential complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="credential">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codedType" type="{urn:ihe:iti:csd:2013}codedtype"/>
 *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="issuingAuthority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="credentialIssueDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="credentialRenewalDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(propOrder = { "codedType", "number", "issuingAuthority", "credentialIssueDate", "credentialRenewalDate", "extensions" })
public class Credential {

    @UIDisplayable(position = 0)
    @Field(required = true)
    @Cascade(delete = true)
    private CodedType codedType;

    @UIDisplayable(position = 1)
    @Field(required = true)
    private String number;

    @UIDisplayable(position = 2)
    @Field
    private String issuingAuthority;

    @UIDisplayable(position = 3)
    @Field
    private DateTime credentialIssueDate;

    @UIDisplayable(position = 4)
    @Field
    private DateTime credentialRenewalDate;

    @Field(name = "credential_extensions")
    @Cascade(delete = true)
    private Set<Extension> extensions = new HashSet<>();

    public Credential() {
    }

    public Credential(CodedType codedType, String number) {
        this.codedType = codedType;
        this.number = number;
    }

    public Credential(CodedType codedType, String number, String issuingAuthority, DateTime credentialIssueDate,
                      DateTime credentialRenewalDate, Set<Extension> extensions) {
        this.codedType = codedType;
        this.number = number;
        this.issuingAuthority = issuingAuthority;
        this.credentialIssueDate = credentialIssueDate;
        this.credentialRenewalDate = credentialRenewalDate;
        this.extensions = extensions;
    }

    public CodedType getCodedType() {
        return codedType;
    }

    @XmlElement(required = true)
    public void setCodedType(CodedType codedType) {
        this.codedType = codedType;
    }

    public String getNumber() {
        return number;
    }

    @XmlElement(required = true)
    public void setNumber(String number) {
        this.number = number;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    @XmlElement
    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public DateTime getCredentialIssueDate() {
        return credentialIssueDate;
    }

    @XmlElement
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(type = DateTime.class, value = DateAdapter.class)
    public void setCredentialIssueDate(DateTime credentialIssueDate) {
        this.credentialIssueDate = credentialIssueDate;
    }

    public DateTime getCredentialRenewalDate() {
        return credentialRenewalDate;
    }

    @XmlElement
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(type = DateTime.class, value = DateAdapter.class)
    public void setCredentialRenewalDate(DateTime credentialRenewalDate) {
        this.credentialRenewalDate = credentialRenewalDate;
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

        Credential that = (Credential) o;

        if (!codedType.equals(that.codedType)) {
            return false;
        }
        if (!number.equals(that.number)) {
            return false;
        }
        if (credentialIssueDate != null ? !credentialIssueDate.toLocalDate().isEqual(that.credentialIssueDate.toLocalDate()) : that.credentialIssueDate != null) {
            return false;
        }
        if (credentialRenewalDate != null ? !credentialRenewalDate.toLocalDate().isEqual(that.credentialRenewalDate.toLocalDate()) : that.credentialRenewalDate != null) {
            return false;
        }
        if (issuingAuthority != null ? !issuingAuthority.equals(that.issuingAuthority) : that.issuingAuthority != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = codedType.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + (issuingAuthority != null ? issuingAuthority.hashCode() : 0);
        result = 31 * result + (credentialIssueDate != null ? credentialIssueDate.hashCode() : 0);
        result = 31 * result + (credentialRenewalDate != null ? credentialRenewalDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return codedType.toString();
    }
}
