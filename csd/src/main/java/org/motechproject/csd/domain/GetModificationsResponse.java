package org.motechproject.csd.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:ihe:iti:csd:2013}CSD"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "csd"
})
@XmlRootElement(name = "getModificationsResponse")
public class GetModificationsResponse {

    @XmlElement(name = "CSD", required = true)
    private CSD csd;

    /**
     * Gets the value of the csd property.
     *
     * @return
     *     possible object is
     *     {@link CSD }
     *
     */
    public CSD getCSD() {
        return csd;
    }

    /**
     * Sets the value of the csd property.
     *
     * @param value
     *     allowed object is
     *     {@link CSD }
     *
     */
    public void setCSD(CSD value) {
        this.csd = value;
    }

}
