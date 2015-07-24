package org.motechproject.batch.model;

import javax.batch.runtime.JobExecution;
import java.util.List;

/**
 * A representation of batch job executions that took place.
 */
public class JobExecutionHistoryListDTO {

    /**
     * Job executions.
     */
    private List<JobExecution> jobExecutionHistoryList;

    /**
     * @return a list containing job execution history
     */
    public List<JobExecution> getJobExecutionHistoryList() {
        return jobExecutionHistoryList;
    }

    /**
     * @param jobExecutionHistoryList a list containing job execution history
     */
    public void setJobExecutionHistoryList(List<JobExecution> jobExecutionHistoryList) {
        this.jobExecutionHistoryList = jobExecutionHistoryList;
    }

}
