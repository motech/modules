package org.motechproject.sms.configs;

import java.util.ArrayList;
import java.util.List;


/**
 * A connection to a particular SMS provider, there may be more than one config for a given provider and/or multiple
 * connections to multiple providers. But realistically, most implementations will have one config.
 */
public class Config {

    public static final String RETRYING = "RETRYING";
    public static final String ABORTED = "ABORTED";

    /**
     * The unique name identifying the configuration.
     */
    private String name;

    /**
     * The maximum number of retries that will be performed when sending an SMS. If there was an error when sending the
     * SMS, we will initiate a retry.
     */
    private Integer maxRetries;

    /**
     * Whether to exclude the split footer in the last part of a split SMS message.
     */
    private Boolean excludeLastFooter;

    /**
     * The header that will be included in split SMS messages.
     */
    private String splitHeader;

    /**
     * The footer that will be included in split SMS messages.
     */
    private String splitFooter;

    /**
     * The name of the configuration template for this config.
     */
    private String templateName;

    /**
     * Additional configuration properties that will be always passed to the template.
     */
    private List<ConfigProp> props = new ArrayList<>();

    /**
     * @return the unique name identifying the configuration
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the unique name identifying the configuration
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the maximum number of retries that will be performed when sending an SMS. If there was an error when sending the
     * SMS, we will initiate a retry.
     * @return the maximum number of retries
     */
    public Integer getMaxRetries() {
        return maxRetries;
    }

    /**
     * Sets the maximum number of retries that will be performed when sending an SMS. If there was an error when sending the
     * SMS, we will initiate a retry.
     * @param maxRetries the maximum number of retries
     */
    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * @return true to exclude the split footer in the last part of a split SMS message, false otherwise
     */
    public Boolean getExcludeLastFooter() {
        return excludeLastFooter;
    }

    /**
     * @param excludeLastFooter true to exclude the split footer in the last part of a split SMS message, false otherwise
     */
    public void setExcludeLastFooter(Boolean excludeLastFooter) {
        this.excludeLastFooter = excludeLastFooter;
    }

    /**
     * @return the header that will be included in split SMS messages
     */
    public String getSplitHeader() {
        return splitHeader;
    }

    /**
     * @param splitHeader the header that will be included in split SMS messages
     */
    public void setSplitHeader(String splitHeader) {
        this.splitHeader = splitHeader;
    }

    /**
     * @return the footer that will be included in split SMS messages
     */
    public String getSplitFooter() {
        return splitFooter;
    }

    /**
     * @param splitFooter the footer that will be included in split SMS messages
     */
    public void setSplitFooter(String splitFooter) {
        this.splitFooter = splitFooter;
    }

    /**
     * @return the name of the configuration template for this config
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName the name of the configuration template for this config
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return additional configuration properties that will be always passed to the template
     */
    public List<ConfigProp> getProps() {
        return props;
    }

    /**
     * @param props additional configuration properties that will be always passed to the template
     */
    public void setProps(List<ConfigProp> props) {
        this.props = props;
    }

    /**
     * Returns an appropriate Delivery Status for the given failure count. If the failure count is higher than
     * the maximum number of retries for this configuration, then ABORTED Status is returned. Otherwise
     * RETRYING Status is returned, indicating the retries are ongoing.
     * @param failureCount the failure count to check
     * @return the delivery status matching the failure count
     */
    public String retryOrAbortStatus(Integer failureCount) {
        if (failureCount < maxRetries) {
            return RETRYING;
        }
        return ABORTED;
    }

    /**
     * Returns an appropriate subject for an event indicating an SMS send failure. If the failure count is higher than
     * the maximum number of retries for this configuration, then a subject indicating that to abort the SMS will be sent.
     * @param failureCount the failure count to check
     * @return the subject for the event
     */
    public String retryOrAbortSubject(Integer failureCount) {
        if (failureCount < maxRetries) {
            return RETRYING;
        }
        return ABORTED;
    }
}
