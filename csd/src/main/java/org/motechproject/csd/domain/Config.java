package org.motechproject.csd.domain;

public class Config {

    private boolean schedulerEnabled;

    private int periodDays;

    private int periodHours;

    private int periodMinutes;

    private String xmlUrl;

    public Config() {
    }

    public Config(boolean schedulerEnabled, int periodDays, int periodHours, int periodMinutes, String xmlUrl) {
        this.schedulerEnabled = schedulerEnabled;
        this.periodDays = periodDays;
        this.periodHours = periodHours;
        this.periodMinutes = periodMinutes;
        this.xmlUrl = xmlUrl;
    }

    public boolean isSchedulerEnabled() {
        return schedulerEnabled;
    }

    public void setSchedulerEnabled(boolean schedulerEnabled) {
        this.schedulerEnabled = schedulerEnabled;
    }

    public int getPeriodDays() {
        return periodDays;
    }

    public void setPeriodDays(int periodDays) {
        this.periodDays = periodDays;
    }

    public int getPeriodHours() {
        return periodHours;
    }

    public void setPeriodHours(int periodHours) {
        this.periodHours = periodHours;
    }

    public int getPeriodMinutes() {
        return periodMinutes;
    }

    public void setPeriodMinutes(int periodMinutes) {
        this.periodMinutes = periodMinutes;
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Config config = (Config) o;

        if (periodDays != config.periodDays) {
            return false;
        }
        if (periodHours != config.periodHours) {
            return false;
        }
        if (periodMinutes != config.periodMinutes) {
            return false;
        }
        if (schedulerEnabled != config.schedulerEnabled) {
            return false;
        }
        if (xmlUrl != null ? !xmlUrl.equals(config.xmlUrl) : config.xmlUrl != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (schedulerEnabled ? 1 : 0);
        result = 31 * result + periodDays;
        result = 31 * result + periodHours;
        result = 31 * result + periodMinutes;
        result = 31 * result + (xmlUrl != null ? xmlUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Config{" +
                "schedulerEnabled=" + schedulerEnabled +
                ", periodDays=" + periodDays +
                ", periodHours=" + periodHours +
                ", periodMinutes=" + periodMinutes +
                ", xmlUrl='" + xmlUrl + '\'' +
                '}';
    }
}
