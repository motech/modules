package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * <p>Java class for uniqueID complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="uniqueID">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="entityID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractUniqueID extends AbstractID {

    @Field(required = true)
    private String entityID;

    public String getEntityID() {
        return entityID;
    }

    /**
     *
     * @param entityID
     *
     * Should be a valid UUID represented as an URN. Example:
     * "urn:uuid:53347B2E-185E-4BC3-BCDA-7FAB5D521FE7"
     */
    @XmlAttribute
    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractUniqueID abstractUniqueID = (AbstractUniqueID) o;

        if (!entityID.equals(abstractUniqueID.entityID)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (entityID != null ? entityID.hashCode() : 0);
    }

    @Override
    public String toString() {
        return entityID;
    }
}
