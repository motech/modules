package org.motechproject.odk.domain;

/**
 * Contains information regarding the success or failure of a connection to an external service.
 */
public class Verification {

    private boolean verified;
    private String message;

    public Verification(boolean verified) {
        this.verified = verified;
    }

    public Verification(boolean verified, String message) {
        this.verified = verified;
        this.message = message;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
