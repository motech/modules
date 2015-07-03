package org.motechproject.sms.templates;


import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * How to interpret provider-specific response statuses.
 */
public class Status {

    /**
     * The key of the ID of the message this status relates to.
     */
    private String messageIdKey;

    /**
     * The key of the status.
     */
    private String statusKey;

    /**
     * The expected success status.
     */
    private String statusSuccess;

    /**
     * The expected failure status.
     */
    private String statusFailure;

    /**
     * Checks whether the provider sends a message ID in the status.
     * @return true if we have a key for message IDs, false otherwise
     */
    public boolean hasMessageIdKey() {
        return isNotBlank(messageIdKey);
    }

    /**
     * @return the key of the ID of the message this status relates to
     */
    public String getMessageIdKey() {
        return messageIdKey;
    }

    /**
     * @param messageIdKey the key of the ID of the message this status relates to
     */
    public void setMessageIdKey(String messageIdKey) {
        this.messageIdKey = messageIdKey;
    }

    /**
     * Checks whether this provider sends a status string.
     * @return true if we have a key for the status field, false otherwise
     */
    public boolean hasStatusKey() {
        return isNotBlank(statusKey);
    }

    /**
     * @return the key of the status
     */
    public String getStatusKey() {
        return statusKey;
    }

    /**
     * @param statusKey the key of the status
     */
    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

    /**
     * Checks whether we know how the success status will look like.
     * @return true if we have an expected success status, false otherwise
     */
    public boolean hasStatusSuccess() {
        return isNotBlank(statusSuccess);
    }

    /**
     * @return the expected success status
     */
    public String getStatusSuccess() {
        return statusSuccess;
    }

    /**
     * @param statusSuccess the expected success status
     */
    public void setStatusSuccess(String statusSuccess) {
        this.statusSuccess = statusSuccess;
    }

    /**
     * Checks whether we know how the failure status will look like.
     * @return true if we have an expected failure status, false otherwise
     */
    public Boolean hasStatusFailure() {
        return isNotBlank(statusFailure);
    }

    /**
     * @return the expected failure status
     */
    public String getStatusFailure() {
        return statusFailure;
    }

    /**
     * @param statusFailure the expected success status
     */
    public void setStatusFailure(String statusFailure) {
        this.statusFailure = statusFailure;
    }

    @Override
    public String toString() {
        return "Status{" +
                "messageIdKey='" + messageIdKey + '\'' +
                ", statusKey='" + statusKey + '\'' +
                ", statusSuccess='" + statusSuccess + '\'' +
                ", statusFailure='" + statusFailure + '\'' +
                '}';
    }
}
