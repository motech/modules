package org.motechproject.batch.model;

import java.util.List;

import javax.batch.runtime.JobExecution;

public class JobExecutionHistoryListDTO {
    
     private List<JobExecution> jobExecutionHistoryList;

    public List<JobExecution> getJobExecutionHistoryList() {
        return jobExecutionHistoryList;
    }

    public void setJobExecutionHistoryList(
            List<JobExecution> jobExecutionHistoryList) {
        this.jobExecutionHistoryList = jobExecutionHistoryList;
    }



}
