package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;

/**
 * Facility
 */
@Entity
public class Facility {
    private String uuid;
    private String primaryName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public Facility(String uuid, String primaryName) {
        this.uuid = uuid;
        this.primaryName = primaryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Facility facility = (Facility) o;

        if (!primaryName.equals(facility.primaryName)) {
            return false;
        }
        if (!uuid.equals(facility.uuid)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + primaryName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "uuid='" + uuid + '\'' +
                ", primaryName='" + primaryName + '\'' +
                '}';
    }
}
