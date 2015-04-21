package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="component" type="{http://www.w3.org/2001/XMLSchema}string" />
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
public class AddressLine extends AbstractID {

    @UIDisplayable(position = 0)
    @Field
    private String value = "";

    @UIDisplayable(position = 1)
    @Field
    private String component;


    public AddressLine() {
    }

    public AddressLine(String value) {
        this.value = value;
    }

    public AddressLine(String value, String component) {
        this.value = value;
        this.component = component;
    }

    public String getValue() {
        return value;
    }

    @XmlValue
    public void setValue(String value) {
        this.value = value;
    }

    public String getComponent() {
        return component;
    }

    @XmlAttribute
    public void setComponent(String component) {
        this.component = component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddressLine that = (AddressLine) o;

        if (component != null ? !component.equals(that.component) : that.component != null) {
            return false;
        }
        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (component != null ? component.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (component != null && !component.isEmpty()) {
            return component + "='" + value + '\'';
        }
        return  value;
    }
}
