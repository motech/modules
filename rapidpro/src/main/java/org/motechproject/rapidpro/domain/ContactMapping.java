package org.motechproject.rapidpro.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;

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
    private String rapidproUUID;

    public ContactMapping(String externalId, String rapidproUUID) {
        this.externalId = externalId;
        this.rapidproUUID = rapidproUUID;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getRapidproUUID() {
        return rapidproUUID;
    }
}
