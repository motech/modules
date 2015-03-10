package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@Entity
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class OtherName {

    /* Text value of this node */
    @Field(required = true)
    private String value;

    /* Attribute @xml:lang */
    @Field
    private String lang;

    public OtherName() {
    }

    public OtherName(String value) {
        this.value = value;
    }

    public OtherName(String value, String lang) {
        this.value = value;
        this.lang = lang;
    }

    public String getValue() {
        return value;
    }

    @XmlValue
    public void setValue(String value) {
        this.value = value;
    }

    public String getLang() {
        return lang;
    }

    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OtherName otherName1 = (OtherName) o;

        if (lang != null ? !lang.equals(otherName1.lang) : otherName1.lang != null) {
            return false;
        }
        if (!value.equals(otherName1.value)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OtherName{" +
                "value='" + value + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}
