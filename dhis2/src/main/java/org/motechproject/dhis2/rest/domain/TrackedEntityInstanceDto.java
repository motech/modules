package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;
import java.util.Objects;

/**
 * A class to model tracked entity instances posted to the DHIS2 API.
 */
@JsonInclude(Include.NON_NULL)
public class TrackedEntityInstanceDto {
    private String trackedEntity;
    private String trackedEntityInstance;
    private String orgUnit;
    private String created;
    private List<AttributeDto> attributes;

    public String getTrackedEntity() {
        return trackedEntity;
    }

    public void setTrackedEntity(String trackedEntity) {
        this.trackedEntity = trackedEntity;
    }

    public String getTrackedEntityInstance() {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance(String trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<AttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDto> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackedEntity, trackedEntityInstance, orgUnit, created, attributes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TrackedEntityInstanceDto other = (TrackedEntityInstanceDto) obj;
        return Objects.equals(this.trackedEntity, other.trackedEntity)
                && Objects.equals(this.trackedEntityInstance, other.trackedEntityInstance)
                && Objects.equals(this.orgUnit, other.orgUnit)
                && Objects.equals(this.created, other.created)
                && Objects.equals(this.attributes, other.attributes);
    }
}
