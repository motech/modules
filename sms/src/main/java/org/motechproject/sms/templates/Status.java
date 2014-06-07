package org.motechproject.sms.templates;


import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * How to interpret provider-specific response statuses
 */
public class Status {
    private String messageIdKey;
    private String statusKey;
    private String statusSuccess;
    private String statusFailure;

    public boolean hasMessageIdKey() {
        return isNotBlank(messageIdKey);
    }

    public String getMessageIdKey() {
        return messageIdKey;
    }

    public void setMessageIdKey(String messageIdKey) {
        this.messageIdKey = messageIdKey;
    }

    public boolean hasStatusKey() {
        return isNotBlank(statusKey);
    }

    public String getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

    public boolean hasStatusSuccess() {
        return isNotBlank(statusSuccess);
    }

    public String getStatusSuccess() {
        return statusSuccess;
    }

    public void setStatusSuccess(String statusSuccess) {
        this.statusSuccess = statusSuccess;
    }

    public Boolean hasStatusFailure() {
        return isNotBlank(statusFailure);
    }

    public String getStatusFailure() {
        return statusFailure;
    }

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
