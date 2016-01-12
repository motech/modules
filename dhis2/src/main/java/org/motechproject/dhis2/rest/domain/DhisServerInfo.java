package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DhisServerInfo {

    private String version;
    private String serverDate;
    private String environmentVariable;
    private String dateFormat;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getEnvironmentVariable() {
        return environmentVariable;
    }

    public void setEnvironmentVariable(String environmentVariable) {
        this.environmentVariable = environmentVariable;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }
}
