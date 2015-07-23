package org.motechproject.messagecampaign.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class used by exception indicating that there were some problems with person enrollment.
 */
public abstract class MessageCampaignException extends RuntimeException {

    private MessageKey messageKey;

    protected MessageCampaignException(String message, String messageKey, Throwable cause) {
        this(message, messageKey, new ArrayList<String>(), cause);
    }

    protected MessageCampaignException(String message, String messageKey, List<String> params, Throwable cause) {
        super(message, cause);
        this.messageKey = new MessageKey(messageKey, params);
;    }

    public MessageKey getMessageKey() {
        return messageKey;
    }

    /**
     * Represents a single parametrized message.
     */
    public static class MessageKey {

        private String key;
        private List<String> params;

        public MessageKey(String key, List<String> params) {
            this.key = key;
            this.params = params;
        }

        public String getKey() {
            return key;
        }

        public List<String> getParams() {
            return params;
        }
    }
}
