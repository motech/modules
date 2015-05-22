package org.motechproject.commcare.domain;


import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Represents CommCare location - a part of the Locations API JSON, retrieved from CommCare.
 * @see CommcareLocationsJson
 */
public class CommcareLocation {

    @SerializedName("created_at")
    private String createdAt;

    private String domain;

    @SerializedName("external_id")
    private String externalId;

    private long id;

    @SerializedName("last_modified")
    private String lastModified;

    private String latitude;

    @SerializedName("location_data")
    private Map<String, String> locationData;

    @SerializedName("location_id")
    private String locationId;

    @SerializedName("location_type")
    private String locationType;

    private String longitude;

    private String name;

    private String parent;

    @SerializedName("resource_uri")
    private String resourceUri;

    @SerializedName("site_code")
    private String siteCode;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getLocationId() {
        return locationId;
    }

    public Map<String, String> getLocationData() {
        return locationData;
    }

    public String getLatitude() {
        return latitude;
    }

    public long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getDomain() {
        return domain;
    }
}
