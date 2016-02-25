package org.motechproject.sms.templates;

import org.motechproject.config.SettingsFacade;

/**
 * How providers deal with outgoing messages.
 */
public class Outgoing {
    private static final String SMS_DEFAULT_MILLISECONDS_BETWEEN_MESSAGES = "sms.default.millisecond_between_messages";
    private static final String SMS_DEFAULT_MAX_SMS_SIZE = "sms.default.max_sms_size";
    private static final String SMS_DEFAULT_MAX_RECIPIENT = "sms.default.max_recipient";
    private static final String SMS_DEFAULT_RECIPIENT_SEPARATOR = "sms.default.recipient_separator";

    /**f
     * The {@link Request} object used for generating an outgoing SMS request.
     */
    private Request request;

    /**
     * The {@link Response} object used for dealing with the provider response.
     */
    private Response response;

    /**
     * True if the provider requires authentication.
     */
    private Boolean hasAuthentication;

    /**
     * Whether we should back off exponentially between retries. Not used currently.
     */
    private Boolean exponentialBackOffRetries;

    /**
     * The minimal number of milliseconds which we should wait between sending messages to the provider,
     * prevents issues with sending too many requests per second.
    */
    private Integer millisecondsBetweenMessages;

    /**
     * The maximum size of an SMS message.
     */
    private Integer maxSmsSize;

    /**
     * The maximum number of recipients.
     */
    private Integer maxRecipient;

    /**
     * The separator used to separate recipients for the SMS.
     */
    private String recipientSeparator;

    /**
     * The default minimal number of milliseconds between sending SMS requests to the provider.
     * This values is used millisecondsBetweenMessages is used.
     */
    private Integer defaultMillisecondsBetweenMessages;

    /**
     * The default maximum size of an SMS message. Will be used if maxSmsSize is not set.
     */
    private Integer defaultMaxSmsSize;

    /**
     * The default maximum number of recipients, will be used if maxRecipient is not set.
     */
    private Integer defaultMaxRecipient;

    /**
     * The default separator used for separating recipient of the SMS message, will be used if recipientSeparator
     * is not set.
     */
    private String defaultRecipientSeparator;

    /**
     * Reads the default values from MOTECH settings.
     * @param settingsFacade the setting facade that will be used for reading the settings
     */
    public void readDefaults(SettingsFacade settingsFacade) {
        defaultMillisecondsBetweenMessages = Integer.valueOf(settingsFacade.getProperty(
                SMS_DEFAULT_MILLISECONDS_BETWEEN_MESSAGES));
        defaultMaxSmsSize = Integer.valueOf(settingsFacade.getProperty(SMS_DEFAULT_MAX_SMS_SIZE));
        defaultMaxRecipient = Integer.valueOf(settingsFacade.getProperty(SMS_DEFAULT_MAX_RECIPIENT));
        defaultRecipientSeparator = settingsFacade.getProperty(SMS_DEFAULT_RECIPIENT_SEPARATOR);
    }

    /**
     * @return the {@link Request} object used for generating an outgoing SMS request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * @param request the {@link Request} object used for generating an outgoing SMS request
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * @return the {@link Response} object used for dealing with the provider response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * @param response the {@link Response} object used for dealing with the provider response
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * @return true if the provider requires authentication, false otherwise
     */
    public Boolean hasAuthentication() {
        return hasAuthentication;
    }

    /**
     * @param hasAuthentication true if the provider requires authentication, false otherwise
     */
    public void setHasAuthentication(Boolean hasAuthentication) {
        this.hasAuthentication = hasAuthentication;
    }

    /**
     * Returns the minimal number of milliseconds which we should wait between sending messages to the provider,
     * prevents issues with sending too many requests per second. If the value is not set, the
     * default value from the settings will be returned.
     * @return the minimal number of milliseconds between message requests sent to the provider
     */
    public Integer getMillisecondsBetweenMessages() {
        if (millisecondsBetweenMessages == null) {
            millisecondsBetweenMessages = defaultMillisecondsBetweenMessages;
        }
        return millisecondsBetweenMessages;
    }

    /**
     * Sets the minimal number of milliseconds which we should wait between sending messages to the provider,
     * prevents issues with sending too many requests per second.
     * @param millisecondsBetweenMessages the number of milliseconds to wait between message requests
     */
    public void setMillisecondsBetweenMessages(Integer millisecondsBetweenMessages) {
        this.millisecondsBetweenMessages = millisecondsBetweenMessages;
    }

    /**
     * Not used currently.
     * @return whether we should back off exponentially between retries
     */
    public Boolean getExponentialBackOffRetries() {
        return exponentialBackOffRetries;
    }

    /**
     * Not used currently.
     * @param exponentialBackOffRetries whether we should back off exponentially between retries
     */
    public void setExponentialBackOffRetries(Boolean exponentialBackOffRetries) {
        this.exponentialBackOffRetries = exponentialBackOffRetries;
    }

    /**
     * Returns the maximum size for an SMS message. If not set, the default value
     * from settings is used.
     * @return the maximum size of an SMS
     */
    public Integer getMaxSmsSize() {
        if (maxSmsSize == null) {
            maxSmsSize = defaultMaxSmsSize;
        }
        return maxSmsSize;
    }

    /**
     * @param maxSmsSize the maximum size for an SMS message
     */
    public void setMaxSmsSize(Integer maxSmsSize) {
        this.maxSmsSize = maxSmsSize;
    }

    /**
     * Returns the maximum number of recipients for an SMS message. If not set
     * the default value from settings is used.
     * @return the maximum number of SMS recipients
     */
    public Integer getMaxRecipient() {
        if (maxRecipient == null) {
            maxRecipient = defaultMaxRecipient;
        }
        return maxRecipient;
    }

    /**
     * @param maxRecipient the maximum number of recipients for an SMS
     */
    public void setMaxRecipient(Integer maxRecipient) {
        this.maxRecipient = maxRecipient;
    }

    /**
     * Returns the separator used to separate recipients for the SMS. If a separator
     * is not set, then the default one from settings is used.
     * @return the separator used to separate recipients for the SMS
     */
    public String getRecipientSeparator() {
        if (recipientSeparator == null) {
            recipientSeparator = defaultRecipientSeparator;
        }
        return recipientSeparator;
    }

    /**
     * @param recipientSeparator the separator used to separate recipients for the SMS
     */
    public void setRecipientSeparator(String recipientSeparator) {
        this.recipientSeparator = recipientSeparator;
    }

    @Override
    public String toString() {
        return "Outgoing{" +
                "request=" + request +
                ", response=" + response +
                ", hasAuthentication=" + hasAuthentication +
                ", millisecondsBetweenMessages=" + millisecondsBetweenMessages +
                ", exponentialBackOffRetries=" + exponentialBackOffRetries +
                ", maxSmsSize=" + maxSmsSize +
                ", maxRecipient=" + maxRecipient +
                ", recipientSeparator='" + recipientSeparator + '\'' +
                '}';
    }
}
