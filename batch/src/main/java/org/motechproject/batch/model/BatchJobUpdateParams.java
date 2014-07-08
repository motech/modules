package org.motechproject.batch.model;

import java.util.Map;

public class BatchJobUpdateParams {

    private String jobName;
    private Map<String, String> paramsMap;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }
}
