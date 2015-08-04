package org.motechproject.csd.domain;

import org.motechproject.csd.constants.CSDConstants;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.util.SecurityMode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * The text content of this element may optionally be used to provide translations to the local language of a coded value.
 *
 * <p>Java class for codedtype complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="codedtype">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="codingScheme" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
@Access(value = SecurityMode.PERMISSIONS, members = {CSDConstants.MANAGE_CSD})
public class CodedType extends AbstractID {

    /* The value of Coded Type eg <CodedType>MyValue</CodeType> */
    @UIDisplayable(position = 0)
    @Field(tooltip = "The human readable value for that code (i.e. \"Community Health Worker\").")
    private String value = "";

    /* Attribute @code */
    @UIDisplayable(position = 1)
    @Field(required = true, tooltip = "The code provided by the coding organization (i.e. \"3253\").")
    private String code;

    /* Attribute @codingScheme*/
    @UIDisplayable(position = 2)
    @Field(required = true, tooltip = "The coding scheme used to identify this code (i.e. \"ISCO-08\").")
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

        CodedType codedType = (CodedType) o;

        if (!code.equals(codedType.code)) {
            return false;
        }
        if (!codingScheme.equals(codedType.codingScheme)) {
            return false;
        }
        if (value != null ? !value.equals(codedType.value) : codedType.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + code.hashCode();
        result = 31 * result + codingScheme.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return code;
    }
}
