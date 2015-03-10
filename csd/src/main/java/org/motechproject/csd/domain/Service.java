package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Entity
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "codedType", "extensions", "record" })
public class Service extends AbstractUniqueID {

    @Field(required = true)
    private CodedType codedType;

    @Order(column = "service_extensions_idx")
    @Field(name = "service_extensions")
    private List<Extension> extensions;

    @Field(required = true)
    private Record record;

    public Service() {
    }

    public Service(String entityID, CodedType codedType, Record record) {
        setEntityID(entityID);
        this.codedType = codedType;
        this.record = record;
    }

    public Service(String entityID, CodedType codedType, List<Extension> extensions, Record record) {
        setEntityID(entityID);
        this.codedType = codedType;
        this.extensions = extensions;
        this.record = record;
    }

    public Record getRecord() {
        return record;
    }

    @XmlElement(required = true)
    public void setRecord(Record record) {
        this.record = record;
    }

    public CodedType getCodedType() {
        return codedType;
    }

    @XmlElement(required = true)
    public void setCodedType(CodedType codedType) {
        this.codedType = codedType;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Service service = (Service) o;

        if (!codedType.equals(service.codedType)) {
            return false;
        }
        if (extensions != null ? !extensions.equals(service.extensions) : service.extensions != null) {
            return false;
        }
        if (!record.equals(service.record)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + codedType.hashCode();
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        result = 31 * result + record.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Service{" +
                "codedType=" + codedType +
                ", extensions=" + extensions +
                ", record=" + record +
                '}';
    }
}
