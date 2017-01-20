package org.motechproject.dhis2.dto;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

public class StageDto {
    private String uuid;

    private String name;

    private List<DataElementDto> dataElements;

    private String program;

    private boolean registration;

    public StageDto() {}

    public StageDto(String uuid, String name, List<DataElementDto> dataElements, String program, boolean registration) {
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

    public List<DataElementDto> getDataElements() {
        return dataElements;
    }

    public void setDataElements(List<DataElementDto> dataElements) {
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

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof StageDto)) {
            return false;
        }

        StageDto other = (StageDto) o;

        return ObjectUtils.equals(uuid, other.uuid) &&
                ObjectUtils.equals(name, other.name) &&
                ObjectUtils.equals(dataElements, other.dataElements) &&
                ObjectUtils.equals(program, other.program) &&
                ObjectUtils.equals(registration, other.registration);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uuid).
                append(name).
                append(dataElements).
                append(program).
                append(registration).toHashCode();
    }
}
