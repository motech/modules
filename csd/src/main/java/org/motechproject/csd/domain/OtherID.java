package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class OtherID {

    @Field(required = true)
    private String code;

    @Field(required = true)
    private String assigningAuthorityName;

    private OtherID() {
    }

    public OtherID(String code) {
        this.code = code;
    }

    private OtherID(String code, String assigningAuthorityName) {
        this.code = code;
        this.assigningAuthorityName = assigningAuthorityName;
    }

    public String getAssigningAuthorityName() {
        return assigningAuthorityName;
    }

    public void setAssigningAuthorityName(String assigningAuthorityName) {
        this.assigningAuthorityName = assigningAuthorityName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OtherID otherID = (OtherID) o;

        if (!assigningAuthorityName.equals(otherID.assigningAuthorityName)) {
            return false;
        }
        if (!code.equals(otherID.code)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + assigningAuthorityName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OtherID{" +
                "code='" + code + '\'' +
                ", assigningAuthorityName='" + assigningAuthorityName + '\'' +
                '}';
    }
}
