package org.motechproject.csd.domain;

import org.joda.time.DateTime;
import org.motechproject.csd.adapters.DateTimeAdapter;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlType
@Entity
public class Record {

    @Field(required = true)
    private DateTime created;

    @Field(required = true)
    private DateTime updated;

    @Field(required = true)
    private String status;

    @Field
    private String sourceDirectory;

    public Record() {
    }

    public Record(DateTime created, String status, DateTime updated) {
        this.created = created;
        this.status = status;
        this.updated = updated;
    }

    public Record(DateTime created, DateTime updated, String status, String sourceDirectory) {
        this.created = created;
        this.updated = updated;
        this.status = status;
        this.sourceDirectory = sourceDirectory;
    }

    public DateTime getCreated() {
        return created;
    }

    @XmlAttribute
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(type = DateTime.class, value = DateTimeAdapter.class)
    public void setCreated(DateTime created) {
        this.created = created;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public String getStatus() {
        return status;
    }

    @XmlAttribute
    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getUpdated() {
        return updated;
    }

    @XmlAttribute
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(type = DateTime.class, value = DateTimeAdapter.class)
    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Record record = (Record) o;

        if (!created.equals(record.created)) {
            return false;
        }
        if (sourceDirectory != null ? !sourceDirectory.equals(record.sourceDirectory) : record.sourceDirectory != null) {
            return false;
        }
        if (!status.equals(record.status)) {
            return false;
        }
        if (!updated.equals(record.updated)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = created.hashCode();
        result = 31 * result + updated.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + (sourceDirectory != null ? sourceDirectory.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Record{" +
                "created=" + created +
                ", updated=" + updated +
                ", status='" + status + '\'' +
                ", sourceDirectory='" + sourceDirectory + '\'' +
                '}';
    }
}
