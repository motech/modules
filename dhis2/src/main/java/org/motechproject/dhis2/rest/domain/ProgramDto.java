package org.motechproject.dhis2.rest.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;
import java.util.Objects;


/**
 * A class to model the DHIS2 API's program resource.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramDto extends BaseDto {

    private boolean registration;
    private boolean singleEvent;
    private String programType;
    private TrackedEntityDto trackedEntity;
    private List<ProgramStageDto> programStages;
    private List<OrganisationUnitDto> organisationUnits;
    private List<ProgramTrackedEntityAttributeDto> programTrackedEntityAttributes;

    public ProgramDto() {}

    public ProgramDto(String uuid, String name) {
        this.setId(uuid);
        this.setName(name);
    }

    public boolean getRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public boolean getSingleEvent() {
        return singleEvent;
    }

    public void setSingleEvent(boolean singleEvent) {
        this.singleEvent = singleEvent;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public TrackedEntityDto getTrackedEntity() {
        return trackedEntity;
    }

    public void setTrackedEntity(TrackedEntityDto trackedEntity) {
        this.trackedEntity = trackedEntity;
    }

    public List<ProgramStageDto> getProgramStages() {
        return programStages;
    }

    public void setProgramStages(List<ProgramStageDto> programStages) {
        this.programStages = programStages;
    }

    public List<OrganisationUnitDto> getOrganisationUnits() {
        return organisationUnits;
    }

    public void setOrganisationUnits(List<OrganisationUnitDto> organisationUnits) {
        this.organisationUnits = organisationUnits;
    }

    public List<ProgramTrackedEntityAttributeDto> getProgramTrackedEntityAttributes() {
        return programTrackedEntityAttributes;
    }

    public void setProgramTrackedEntityAttributes(List<ProgramTrackedEntityAttributeDto> programTrackedEntityAttributes) {
        this.programTrackedEntityAttributes = programTrackedEntityAttributes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(registration, singleEvent, trackedEntity, programStages, organisationUnits, programTrackedEntityAttributes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProgramDto other = (ProgramDto) obj;
        return Objects.equals(this.registration, other.registration)
                && Objects.equals(this.singleEvent, other.singleEvent)
                && Objects.equals(this.trackedEntity, other.trackedEntity)
                && Objects.equals(this.programStages, other.programStages)
                && Objects.equals(this.organisationUnits, other.organisationUnits)
                && Objects.equals(this.programTrackedEntityAttributes, other.programTrackedEntityAttributes);
    }
}
