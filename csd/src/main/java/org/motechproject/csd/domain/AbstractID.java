package org.motechproject.csd.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.motechproject.mds.annotations.Entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlAccessorType(XmlAccessType.NONE)
@XmlTransient
public abstract class AbstractID {

    private Long id;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
