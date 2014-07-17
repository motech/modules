package org.motechproject.mtraining.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Abstract class to expose auto-generated fields in MDS
 */
@Entity
public abstract class MdsEntity {

    @Field
    private long id;

    @Field
    private DateTime creationDate;

    @Field
    private DateTime modificationDate;

    public long getId() {
        return id;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public DateTime getModificationDate() {
        return modificationDate;
    }
}
