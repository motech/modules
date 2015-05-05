package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 * A class to model program stage data elements returned by the DHIS2 API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramStageDataElementDto {

    private ProgramStageDto programStage;
    private DataElementDto dataElement;

    public ProgramStageDto getProgramStage() {
        return programStage;
    }

    public void setProgramStage(ProgramStageDto programStage) {
        this.programStage = programStage;
    }

    public DataElementDto getDataElement() {
        return dataElement;
    }

    public void setDataElement(DataElementDto dataElement) {
        this.dataElement = dataElement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(programStage, dataElement);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProgramStageDataElementDto other = (ProgramStageDataElementDto) obj;
        return Objects.equals(this.programStage, other.programStage)
                && Objects.equals(this.dataElement, other.dataElement);
    }
}
