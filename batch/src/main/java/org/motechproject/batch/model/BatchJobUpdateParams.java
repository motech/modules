package org.motechproject.batch.model;

import java.util.Map;

/**
 * A request for updating params of a batch job.
 */
public class BatchJobUpdateParams {

    /**
     * The name of the job to update.
     */
    private String jobName;

    /**
     * The map representing the params to update.
     */
    private Map<String, String> paramsMap;

    /**
     * @return the name of the job to update
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the name of the job to update
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return the map representing the params to update
     */
    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    /**
     * @param paramsMap the map representing the params to update
     */
    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }
}
