package org.motechproject.commcare.config;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Represents a single Commcare configuration.
 */
public class Config {

    public static final String CONFIG_NAME = "commcareConfigName";
    public static final String CONFIG_BASE_URL = "commcareBaseUrl";
    public static final String CONFIG_DOMAIN = "commcareDomain";
    public static final String CONFIG_USERNAME = "username";
    public static final String CONFIG_PASSWORD = "password";

    public static final String FULL_DATA_EVENT = "full";
    public static final String PARTIAL_DATA_EVENT = "partial";

    private String name;
    private AccountConfig accountConfig;
    private String eventStrategy;
    private boolean forwardCases;
    private boolean forwardForms;
    private boolean forwardStubs;
    private boolean forwardSchema;
    private boolean saved;

    public String getEventStrategy() {
        return eventStrategy;
    }

    public void setEventStrategy(String eventStrategy) {
        this.eventStrategy = eventStrategy;
    }

    public boolean isForwardCases() {
        return forwardCases;
    }

    public void setForwardCases(boolean forwardCases) {
        this.forwardCases = forwardCases;
    }

    public boolean isForwardForms() {
        return forwardForms;
    }

    public void setForwardForms(boolean forwardForms) {
        this.forwardForms = forwardForms;
    }

    public boolean isForwardStubs() {
        return forwardStubs;
    }

    public void setForwardStubs(boolean forwardStubs) {
        this.forwardStubs = forwardStubs;
    }

    public boolean isForwardSchema() {
        return forwardSchema;
    }

    public void setForwardSchema(boolean forwardSchema) {
        this.forwardSchema = forwardSchema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountConfig getAccountConfig() {
        return accountConfig;
    }

    public void setAccountConfig(AccountConfig accountConfig) {
        this.accountConfig = accountConfig;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isSaved() {
        return this.saved;
    }

    @JsonIgnore
    public boolean isEventStrategyFull() {
        return FULL_DATA_EVENT.equals(eventStrategy);
    }

    @JsonIgnore
    public boolean isEventStrategyPartial() {
        return PARTIAL_DATA_EVENT.equals(eventStrategy);
    }

    @JsonIgnore
    public boolean isEventStrategyMinimal() {
        return !isEventStrategyFull() && !isEventStrategyPartial();
    }
}
