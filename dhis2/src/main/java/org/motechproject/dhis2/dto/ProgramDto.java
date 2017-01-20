package org.motechproject.dhis2.dto;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

public class ProgramDto {

    private String uuid;

    private String name;

    private TrackedEntityDto trackedEntity;

    private List<StageDto> stages;

    private List<TrackedEntityAttributeDto> attributes;

    private boolean singleEvent;

    private boolean registration;

    private String programType;

    public ProgramDto() {}

    public ProgramDto(String uuid, String name, TrackedEntityDto trackedEntity, List<StageDto> stages, List<TrackedEntityAttributeDto> attributes, boolean singleEvent, boolean registration, String programType) {
        this.uuid = uuid;
        this.name = name;
        this.trackedEntity = trackedEntity;
        this.stages = stages;
        this.attributes = attributes;
        this.singleEvent = singleEvent;
        this.registration = registration;
        this.programType = programType;
    }

    public boolean isSingleEvent() {
        return singleEvent;
    }

    public void setSingleEvent(boolean singleEvent) {
        this.singleEvent = singleEvent;
    }

    public boolean hasRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public TrackedEntityDto getTrackedEntity() {
        return trackedEntity;
    }

    public void setTrackedEntity(TrackedEntityDto trackedEntity) {
        this.trackedEntity = trackedEntity;
    }

    public List<StageDto> getStages() {
        return stages;
    }

    public void setStages(List<StageDto> stages) {
        this.stages = stages;
    }

    public List<TrackedEntityAttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<TrackedEntityAttributeDto> attributes) {
        this.attributes = attributes;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof ProgramDto)) {
            return false;
        }

        ProgramDto other = (ProgramDto) o;

        return ObjectUtils.equals(uuid, other.uuid) &&
                ObjectUtils.equals(name, other.name) &&
                ObjectUtils.equals(trackedEntity, other.trackedEntity) &&
                ObjectUtils.equals(stages, other.stages) &&
                ObjectUtils.equals(attributes, other.attributes) &&
                ObjectUtils.equals(singleEvent, other.singleEvent) &&
                ObjectUtils.equals(registration, other.registration) &&
                ObjectUtils.equals(programType, other.programType);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uuid).
                append(name).
                append(trackedEntity).
                append(stages).
                append(attributes).
                append(singleEvent).
                append(registration).
                append(programType).toHashCode();
    }
}
