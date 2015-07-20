package org.motechproject.batch.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * A representation of a one time job to be scheduled.
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class OneTimeJobScheduleParams extends BatchJobUpdateParams {

    /**
     * The date to run this job on.
     */
    private String date;

    /**
     * @return the date to run this job on
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date run this job on
     */
    public void setDate(String date) {
        this.date = date;
    }
}
