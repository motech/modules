package org.motechproject.csd.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Config {

    public static final String DATE_TIME_PICKER_FORMAT = "yyyy-MM-dd HH:mm";

    private boolean schedulerEnabled;

    private String timePeriod;

    private String timePeriodMultiplier;

    private String startDate;

    private String xmlUrl;

    private CommunicationProtocol communicationProtocol;

    private String lastModified;

    public Config() {
    }

    public Config(boolean schedulerEnabled, String timePeriod, String timePeriodMultiplier, String startDate, String xmlUrl) {
        this.schedulerEnabled = schedulerEnabled;
        this.timePeriod = timePeriod;
        this.timePeriodMultiplier = timePeriodMultiplier;
        this.startDate = startDate;
        this.xmlUrl = xmlUrl;
    }

    public boolean isSchedulerEnabled() {
        return schedulerEnabled;
    }

    public void setSchedulerEnabled(boolean schedulerEnabled) {
        this.schedulerEnabled = schedulerEnabled;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getTimePeriodMultiplier() {
        return timePeriodMultiplier;
    }

    public void setTimePeriodMultiplier(String timePeriodMultiplier) {
        this.timePeriodMultiplier = timePeriodMultiplier;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonIgnore
    public int getPeriodDays() {
        if ("days".equals(timePeriodMultiplier)) {
            return Integer.valueOf(timePeriod);
        } else {
            return 0;
        }
    }

    @JsonIgnore
    public int getPeriodHours() {
        if ("hours".equals(timePeriodMultiplier)) {
            return Integer.valueOf(timePeriod);
        } else {
            return 0;
        }
    }

    @JsonIgnore
    public int getPeriodMinutes() {
        if ("minutes".equals(timePeriodMultiplier)) {
            return Integer.valueOf(timePeriod);
        } else {
            return 0;
        }
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public CommunicationProtocol getCommunicationProtocol() {
        return communicationProtocol;
    }

    public void setCommunicationProtocol(CommunicationProtocol communicationProtocol) {
        this.communicationProtocol = communicationProtocol;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
