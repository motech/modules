package org.motechproject.rapidpro.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import java.util.UUID;

/**
 * Contains Mapping from an External ID to a RapidProUUID
 */
@Entity(name = "Contact Mapping")
public class ContactMapping {

    @Field(name = "externalId", displayName = "External ID", required = true)
    @Unique
    private String externalId;

    @Field(name = "rapidproUUID", displayName = "Rapidpro UUID", required = true)
    @Unique
    private UUID rapidproUUID;

    public ContactMapping(String externalId, UUID rapidproUUID) {
        this.externalId = externalId;
        this.rapidproUUID = rapidproUUID;
    }

    public String getExternalId() {
        return externalId;
    }

    public UUID getRapidproUUID() {
        return rapidproUUID;
    }
}
