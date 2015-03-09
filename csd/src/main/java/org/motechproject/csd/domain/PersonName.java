package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ihe:iti:csd:2013}name">
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "commonNames", "honorific", "forename", "otherNames", "surname", "suffix" })
public class PersonName {

    @Field(required = true)
    private List<String> commonNames;

    @Field
    private String honorific;

    @Field
    private String forename;

    @Order(column = "person_name_other_names_idx")
    @Field(name = "person_name_other_names")
    private List<CodedType> otherNames;

    @Field
    private String surname;

    @Field
    private String suffix;

    @Field
    private String lang;

    public PersonName() {
    }

    public PersonName(List<String> commonNames) {
        this.commonNames = commonNames;
    }

    public PersonName(List<String> commonNames, String honorific, String forename, List<CodedType> otherNames, String surname, String suffix, String lang) {
        this.commonNames = commonNames;
        this.honorific = honorific;
        this.forename = forename;
        this.otherNames = otherNames;
        this.surname = surname;
        this.suffix = suffix;
        this.lang = lang;
    }

    public List<String> getCommonNames() {
        return commonNames;
    }

    @XmlElement(name = "commonName", required = true)
    public void setCommonNames(List<String> commonNames) {
        this.commonNames = commonNames;
    }

    public String getHonorific() {
        return honorific;
    }

    @XmlElement
    public void setHonorific(String honorific) {
        this.honorific = honorific;
    }

    public String getSuffix() {
        return suffix;
    }

    @XmlElement
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    public String getForename() {
        return forename;
    }

    @XmlElement
    public void setForename(String forename) {
        this.forename = forename;
    }

    public List<CodedType> getOtherNames() {
        return otherNames;
    }

    public String getLang() {
        return lang;
    }

    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    public void setLang(String lang) {
        this.lang = lang;
    }

    @XmlElement(name = "otherName")
    public void setOtherNames(List<CodedType> otherNames) {
        this.otherNames = otherNames;
    }

    public String getSurname() {
        return surname;
    }

    @XmlElement
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonName that = (PersonName) o;

        if (!commonNames.equals(that.commonNames)) {
            return false;
        }
        if (forename != null ? !forename.equals(that.forename) : that.forename != null) {
            return false;
        }
        if (honorific != null ? !honorific.equals(that.honorific) : that.honorific != null) {
            return false;
        }
        if (otherNames != null ? !otherNames.equals(that.otherNames) : that.otherNames != null) {
            return false;
        }
        if (suffix != null ? !suffix.equals(that.suffix) : that.suffix != null) {
            return false;
        }
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) {
            return false;
        }
        if (lang != null ? !lang.equals(that.lang) : that.lang != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = commonNames.hashCode();
        result = 31 * result + (honorific != null ? honorific.hashCode() : 0);
        result = 31 * result + (forename != null ? forename.hashCode() : 0);
        result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
        result = 31 * result + (otherNames != null ? otherNames.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PersonName{" +
                "commonNames=" + commonNames +
                ", honorific='" + honorific + '\'' +
                ", forename='" + forename + '\'' +
                ", otherNames=" + otherNames +
                ", surname='" + surname + '\'' +
                ", suffix='" + suffix + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}
