package org.motechproject.dhis2.rest.domain;

import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * A class to model program stages returned by the DHIS2 API.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramStageDto extends BaseDto {

    private ProgramDto program;
    private List<ProgramStageDataElementDto> programStageDataElements;

    public ProgramStageDto() { }

    public ProgramStageDto(String uuid, String name, ProgramDto program, List<ProgramStageDataElementDto> programStageDataElements) {
        this.setId(uuid);
        this.setName(name);
        this.program = program;
        this.programStageDataElements = programStageDataElements;
    }

    public ProgramDto getProgram() {
        return program;
    }

    public void setProgram(ProgramDto program) {
        this.program = program;
    }

    public List<ProgramStageDataElementDto> getProgramStageDataElements() {
        return programStageDataElements;
    }

    public void setProgramStageDataElements(List<ProgramStageDataElementDto> programStageDataElements) {
        this.programStageDataElements = programStageDataElements;
    }

    @Override
    public int hashCode() {
        return Objects.hash(program, programStageDataElements);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProgramStageDto other = (ProgramStageDto) obj;
        return Objects.equals(this.program, other.program)
                && Objects.equals(this.programStageDataElements, other.programStageDataElements);
    }
}
