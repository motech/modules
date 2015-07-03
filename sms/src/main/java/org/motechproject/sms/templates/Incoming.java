package org.motechproject.sms.templates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * How providers deal with incoming messages
 */
public class Incoming {

    /**
     * The key used by the provider to denote the message.
     */
    private String messageKey;

    /**
     * The key used by the provider to denote the sender.
     */
    private String senderKey;

    /**
     * The regex pattern used for extracting the sender number frm the sender data sent by the provider.
     */
    private String senderRegex;

    /**
     * The key used by the provider to denote the recipient.
     */
    private String recipientKey;

    /**
     * The regex pattern used for extracting the sender number frm the sender data sent by the provider.
     */
    private String recipientRegex;

    /**
     * The key used by the provider to denote the timestamp.
     */
    private String timestampKey;

    /**
     * The key used by the provider to denote the message ID.
     */
    private String msgIdKey;

    // These patterns are compiled using the regex fields from above

    private Pattern extractSenderPattern;
    private Pattern extractRecipientPattern;

    /**
     * @return the key used by the provider to denote the message
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * @param messageKey the key used by the provider to denote the message
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * @return the key used by the provider to denote the sender
     */
    public String getSenderKey() {
        return senderKey;
    }

    /**
     * @param senderKey the key used by the provider to denote the sender
     */
    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    /**
     * @return the key used by the provider to denote the recipient
     */
    public String getRecipientKey() {
        return recipientKey;
    }

    /**
     * @param recipientKey the key used by the provider to denote the recipient
     */
    public void setRecipientKey(String recipientKey) {
        this.recipientKey = recipientKey;
    }

    /**
     * @return the key used by the provider to denote the timestamp
     */
    public String getTimestampKey() {
        return timestampKey;
    }

    /**
     * @param timestampKey the key used by the provider to denote the timestamp
     */
    public void setTimestampKey(String timestampKey) {
        this.timestampKey = timestampKey;
    }

    /**
     * @return the key used by the provider to denote the message ID
     */
    public String getMsgIdKey() {
        return msgIdKey;
    }

    /**
     * @param msgIdKey the key used by the provider to denote the message ID
     */
    public void setMsgIdKey(String msgIdKey) {
        this.msgIdKey = msgIdKey;
    }

    /**
     * Checks whether a regex pattern for extracting senders is set.
     * @return true if this object has a pattern for extracting senders, false otherwise
     */
    public Boolean hasSenderRegex() {
        return senderRegex != null && senderRegex.length() > 0;
    }

    /**
     * Extracts the sender from the given string using the sender regex.
     * @param s the string to parse
     * @return the sender of the message
     */
    public String extractSender(String s) {
        if (extractSenderPattern == null) {
            extractSenderPattern = Pattern.compile(senderRegex);
        }
        Matcher m = extractSenderPattern.matcher(s);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    /**
     * @param senderRegex the regex pattern used for extracting the sender number from the sender data sent by the provider
     */
    public void setSenderRegex(String senderRegex) {
        this.senderRegex = senderRegex;
    }

    /**
     * Checks whether a regex pattern for extracting recipients is set.
     * @return true if this object has a pattern for extracting recipients, false otherwise
     */
    public Boolean hasRecipientRegex() {
        return recipientRegex != null && recipientRegex.length() > 0;
    }

    /**
     * Extracts the recipient from the given string using the sender regex.
     * @param s the string to parse
     * @return the recipient of the message
     */
    public String extractRecipient(String s) {
        if (extractRecipientPattern == null) {
            extractRecipientPattern = Pattern.compile(recipientRegex);
        }
        Matcher m = extractRecipientPattern.matcher(s);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    /**
     * @param recipientRegex the regex pattern used for extracting the sender number from the sender data sent by the provider
     */
    public void setRecipientRegex(String recipientRegex) {
        this.recipientRegex = recipientRegex;
    }

    @Override
    public String toString() {
        return "Incoming{" +
                "messageKey='" + messageKey + '\'' +
                ", senderKey='" + senderKey + '\'' +
                ", senderRegex='" + senderRegex + '\'' +
                ", recipientKey='" + recipientKey + '\'' +
                ", recipientRegex='" + recipientRegex + '\'' +
                ", timestampKey='" + timestampKey + '\'' +
                ", msgIdKey='" + msgIdKey + '\'' +
                ", extractSenderPattern=" + extractSenderPattern +
                ", extractRecipientPattern=" + extractRecipientPattern +
                '}';
    }
}
