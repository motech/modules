package org.motechproject.odk.domain;

/**
 * Contains information regarding the success or failure of a connection to an external service.
 */
public class Verification {

    private boolean verified;

    public Verification(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
