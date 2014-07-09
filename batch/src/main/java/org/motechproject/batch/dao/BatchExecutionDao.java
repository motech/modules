package org.motechproject.batch.dao;

import java.util.List;

import javax.batch.runtime.JobExecution;

import org.motechproject.batch.model.JobExecutionHistoryListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BatchExecutionDao {

    @Autowired
    private JdbcTemplate jdbcTempalte;

    public JobExecutionHistoryListDTO getExecutionHist(String jobName) {
        JobExecutionHistoryListDTO jobExecutionHistoryDTO = new JobExecutionHistoryListDTO();
        String sql = "select je.* from BATCH_JOB_INSTANCE ji "
                + "inner join BATCH_JOB_EXECUTION je on ji.JOB_INSTANCE_ID=je.JOB_INSTANCE_ID where ji.JOB_NAME='"
                + jobName + "'";
        List<JobExecution> jobExecutionHistoryList = jdbcTempalte.query(sql,
                new BeanPropertyRowMapper<JobExecution>(JobExecution.class));
        jobExecutionHistoryDTO
                .setJobExecutionHistoryList(jobExecutionHistoryList);
        return jobExecutionHistoryDTO;

    }
}
