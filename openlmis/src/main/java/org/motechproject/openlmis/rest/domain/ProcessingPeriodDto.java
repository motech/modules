package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's ProcessingPeriod resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessingPeriodDto {
    
    private Integer id; //(integer, optional),
    private Integer scheduleId; //(integer, optional),
    private String startdate; //(string, optional),
    private String description; //(string, optional),
    private String name; //(string, optional),
    private String enddate; //(string, optional)
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scheduleId, startdate, description, name, enddate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProcessingPeriodDto other = (ProcessingPeriodDto) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.scheduleId, other.scheduleId)
                && Objects.equals(this.startdate, other.startdate)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.enddate, other.enddate);
    }
}
