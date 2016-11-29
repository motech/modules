package org.motechproject.dhis2.domain;

import org.motechproject.dhis2.dto.ProgramDto;
import org.motechproject.dhis2.dto.StageDto;
import org.motechproject.dhis2.dto.TrackedEntityAttributeDto;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a DHIS2 Program
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureDhis"})
public class Program {
    @Field(required = true)
    @Unique
    private String uuid;

    @Field
    private String name;

    @Field
    private TrackedEntity trackedEntity;

    @Field
    @Cascade(delete = true)
    private List<Stage> stages;

    @Field
    private List<TrackedEntityAttribute> attributes;

    @Field
    private boolean singleEvent;

    @Field
    private boolean registration;

    @Field
    private String programType;


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

    public TrackedEntity getTrackedEntity() {
        return trackedEntity;
    }

    public void setTrackedEntity(TrackedEntity trackedEntity) {
        this.trackedEntity = trackedEntity;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public List<TrackedEntityAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<TrackedEntityAttribute> attributes) {
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

    public ProgramDto toDto() {
        List<StageDto> stageDtos = new ArrayList<>();
        List<TrackedEntityAttributeDto> trackedEntityAttributeDtos = new ArrayList<>();

        for (Stage stage : stages) {
            stageDtos.add(stage.toDto());
        }

        for (TrackedEntityAttribute trackedEntityAttribute : attributes) {
            trackedEntityAttributeDtos.add(trackedEntityAttribute.toDto());
        }

        return new ProgramDto(uuid, name, trackedEntity.toDto(), stageDtos, trackedEntityAttributeDtos, singleEvent, registration, programType);
    }
}
