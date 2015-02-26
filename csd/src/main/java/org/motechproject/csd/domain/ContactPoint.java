package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class ContactPoint {

    @Field(required = true)
    private CodedType codedType;

    @Field
    private String equipment;

    @Field
    private String purpose;

    @Field
    private String certificate;

    public ContactPoint() {
    }

    public ContactPoint(CodedType codedType) {
        this.codedType = codedType;
    }

    public ContactPoint(CodedType codedType, String equipment, String purpose, String certificate) {
        this.codedType = codedType;
        this.equipment = equipment;
        this.purpose = purpose;
        this.certificate = certificate;
    }

    public CodedType getCodedType() {
        return codedType;
    }

    public void setCodedType(CodedType codedType) {
        this.codedType = codedType;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactPoint that = (ContactPoint) o;

        if (certificate != null ? !certificate.equals(that.certificate) : that.certificate != null) {
            return false;
        }
        if (!codedType.equals(that.codedType)) {
            return false;
        }
        if (equipment != null ? !equipment.equals(that.equipment) : that.equipment != null) {
            return false;
        }
        if (purpose != null ? !purpose.equals(that.purpose) : that.purpose != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = codedType.hashCode();
        result = 31 * result + (equipment != null ? equipment.hashCode() : 0);
        result = 31 * result + (purpose != null ? purpose.hashCode() : 0);
        result = 31 * result + (certificate != null ? certificate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContactPoint{" +
                "codedType=" + codedType +
                ", equipment='" + equipment + '\'' +
                ", purpose='" + purpose + '\'' +
                ", certificate='" + certificate + '\'' +
                '}';
    }
}
