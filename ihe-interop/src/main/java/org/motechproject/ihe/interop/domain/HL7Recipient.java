package org.motechproject.ihe.interop.domain;

/**
 * Represents HL7 recipient.
 */
public class HL7Recipient {

    private String recipientName;

    private String recipientUrl;

    public HL7Recipient() {
    }

    public HL7Recipient(String recipientName, String recipientUrl) {
        this.recipientName = recipientName;
        this.recipientUrl = recipientUrl;
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
}
