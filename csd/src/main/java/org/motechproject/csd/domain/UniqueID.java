package org.motechproject.csd.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class UniqueID {

    private String entityID;

    public UniqueID() {
    }

    public UniqueID(String entityID) {
        this.entityID = entityID;
    }

    @XmlAttribute
    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }
}
