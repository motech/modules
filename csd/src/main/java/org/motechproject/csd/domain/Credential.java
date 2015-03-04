package org.motechproject.csd.domain;

import org.joda.time.DateTime;
import org.motechproject.csd.adapters.DateAdapter;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Entity
@XmlType(propOrder = { "codedType", "number", "issuingAuthority", "credentialIssueDate", "credentialRenewalDate", "extensions" })
public class Credential {

    @Field(required = true)
    private CodedType codedType;

    @Field(required = true)
    private String number;

    @Field
    private String issuingAuthority;

    @Field
    private DateTime credentialIssueDate;

    @Field
    private DateTime credentialRenewalDate;

    @Order(column = "credential_extensions_idx")
    @Field(name = "credential_extensions")
    private List<Extension> extensions;

    public Credential() {
    }

    public Credential(CodedType codedType, String number) {
        this.codedType = codedType;
        this.number = number;
    }

    public Credential(CodedType codedType, String number, String issuingAuthority, DateTime credentialIssueDate, DateTime credentialRenewalDate, List<Extension> extensions) {
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

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public DateTime getCredentialIssueDate() {
        return credentialIssueDate;
    }

    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(type = DateTime.class, value = DateAdapter.class)
    public void setCredentialIssueDate(DateTime credentialIssueDate) {
        this.credentialIssueDate = credentialIssueDate;
    }

    public DateTime getCredentialRenewalDate() {
        return credentialRenewalDate;
    }

    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(type = DateTime.class, value = DateAdapter.class)
    public void setCredentialRenewalDate(DateTime credentialRenewalDate) {
        this.credentialRenewalDate = credentialRenewalDate;
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

        Credential that = (Credential) o;

        if (!codedType.equals(that.codedType)) {
            return false;
        }
        if (credentialIssueDate != null ? !credentialIssueDate.equals(that.credentialIssueDate) : that.credentialIssueDate != null) {
            return false;
        }
        if (credentialRenewalDate != null ? !credentialRenewalDate.equals(that.credentialRenewalDate) : that.credentialRenewalDate != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        if (issuingAuthority != null ? !issuingAuthority.equals(that.issuingAuthority) : that.issuingAuthority != null) {
            return false;
        }
        if (!number.equals(that.number)) {
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
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "codedType=" + codedType +
                ", number='" + number + '\'' +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                ", credentialIssueDate=" + credentialIssueDate +
                ", credentialRenewalDate=" + credentialRenewalDate +
                ", extensions=" + extensions +
                '}';
    }
}
