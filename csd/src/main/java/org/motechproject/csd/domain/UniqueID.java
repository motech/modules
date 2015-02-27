package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public abstract class UniqueID {

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

        UniqueID uniqueID = (UniqueID) o;

        if (!entityID.equals(uniqueID.entityID)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return entityID.hashCode();
    }

    @Override
    public String toString() {
        return "UniqueID{" +
                "entityID='" + entityID + '\'' +
                '}';
    }
}
