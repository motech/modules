package org.motechproject.dhis2.domain;

import org.motechproject.dhis2.dto.DataElementDto;
import org.motechproject.dhis2.dto.StageDto;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a DHIS2 program stage event
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureDhis"})
public class Stage {
    @Field(required = true)
    @Unique
    private String uuid;

    @Field
    private String name;

    @Field
    private List<DataElement> dataElements;

    @Field
    private String program;

    @Field
    private boolean registration;

    public Stage() {}

    public Stage(String uuid, String name, List<DataElement> dataElements, String program, boolean registration) {
        this.uuid = uuid;
        this.name = name;
        this.dataElements = dataElements;
        this.program = program;
        this.registration = registration;
    }

    public boolean hasRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public List<DataElement> getDataElements() {
        return dataElements;
    }

    public void setDataElements(List<DataElement> dataElements) {
        this.dataElements = dataElements;
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

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public StageDto toDto() {
        List<DataElementDto> dataElementDtos = new ArrayList<>();

        for (DataElement dataElement : dataElements) {
            dataElementDtos.add(dataElement.toDto());
        }

        return new StageDto(uuid, name, dataElementDtos, program, registration);
    }
}
