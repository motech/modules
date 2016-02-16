package org.motechproject.ipf.domain;

/**
 * Represents HL7 recipient.
 */
public class IPFRecipient {

    private String recipientName;

    private String recipientUrl;

    public IPFRecipient() {
    }

    public IPFRecipient(String recipientName, String recipientUrl) {
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
