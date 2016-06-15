package org.motechproject.ihe.interop.domain;

/**
 * Represents HL7 recipient.
 */
public class HL7Recipient {

    private String recipientName;

    private String recipientUrl;

    private String recipientUsername;

    private String recipientPassword;

    public HL7Recipient() {
    }

    public HL7Recipient(String recipientName, String recipientUrl) {
        this(recipientName, recipientUrl, null, null);
    }

    public HL7Recipient(String recipientName, String recipientUrl, String recipientUsername, String recipientPassword) {
        this.recipientName = recipientName;
        this.recipientUrl = recipientUrl;
        this.recipientUsername = recipientUsername;
        this.recipientPassword = recipientPassword;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientUrl() {
        return recipientUrl;
    }

    public void setRecipientUrl(String recipientUrl) {
        this.recipientUrl = recipientUrl;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getRecipientPassword() {
        return recipientPassword;
    }

    public void setRecipientPassword(String recipientPassword) {
        this.recipientPassword = recipientPassword;
    }
}
