package org.motechproject.dhis2.domain;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import java.util.List;

/**
 * Represents a DHIS2 Data Set.
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureDhis"})
public class DataSet {

    @Field(required = true)
    private String uuid;

    @Field
    private String name;

    @Field
    private List<DataElement> dataElementList;

    public DataSet() {}

    public DataSet(String uuid, String name, List<DataElement> dataElementList) {
        this.uuid = uuid;
        this.name = name;
        this.dataElementList = dataElementList;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataElement> getDataElementList() {
        return dataElementList;
    }

    public void setDataElementList(List<DataElement> dataElementList) {
        this.dataElementList = dataElementList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DataSet)) {
            return false;
        }

        DataSet other = (DataSet) o;

        return ObjectUtils.equals(uuid, other.uuid) && ObjectUtils.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uuid).append(name).toHashCode();
    }
}
