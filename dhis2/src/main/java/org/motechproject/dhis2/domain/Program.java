package org.motechproject.dhis2.domain;

import org.apache.commons.lang.ObjectUtils;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;
import java.util.List;
import java.util.Objects;

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

    public Program() {}

    public Program(String uuid, String name, TrackedEntity trackedEntity, List<Stage> stages,
                   List<TrackedEntityAttribute> attributes, boolean singleEvent, boolean registration, String programType) {
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

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Program)) {
            return false;
        }

        Program other = (Program) o;

        return ObjectUtils.equals(uuid, other.uuid) &&
                ObjectUtils.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name);
    }
}
