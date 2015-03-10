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
public class CodedType {

    /* The value of Coded Type eg <CodedType>MyValue</CodeType> */
    @Field
    private String value;

    /* Attribute @code */
    @Field(required = true)
    private String code;

    /* Attribute @codingScheme*/
    @Field(required = true)
    private String codingScheme;

    public CodedType() {
    }

    public CodedType(String code, String codingScheme) {
        this.code = code;
        this.codingScheme = codingScheme;
    }

    public CodedType(String value, String code, String codingScheme) {
        this.value = value;
        this.code = code;
        this.codingScheme = codingScheme;
    }

    public String getValue() {
        return value;
    }

    @XmlValue
    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    @XmlAttribute(required = true)
    public void setCode(String code) {
        this.code = code;
    }

    public String getCodingScheme() {
        return codingScheme;
    }

    @XmlAttribute(required = true)
    public void setCodingScheme(String codingScheme) {
        this.codingScheme = codingScheme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CodedType codedType1 = (CodedType) o;

        if (!code.equals(codedType1.code)) {
            return false;
        }
        if (!value.equals(codedType1.value)) {
            return false;
        }
        if (!codingScheme.equals(codedType1.codingScheme)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + code.hashCode();
        result = 31 * result + codingScheme.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CodedType{" +
                "value='" + value + '\'' +
                ", code='" + code + '\'' +
                ", codingScheme='" + codingScheme + '\'' +
                '}';
    }
}
