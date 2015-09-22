package org.motechproject.hub.model;

/**
 * The enum class defining valid modes of request send to hub.
 */
public enum Modes {

    /**
     * Subscribing mode
     */
    SUBSCRIBE("subscribe"),
    /**
     * Unsubscribing mode
     */
    UNSUBSCRIBE("unsubscribe"),
    /**
     * Publishing mode
     */
    PUBLISH("publish");

    private final String mode;

    private Modes(String mode) {
        this.mode = mode;
    }

    /**
     * Gets the mode of hub request.
     *
     * @return the hub request mode as a <code>String</code>
     */
    public String getMode() {
        return this.mode;
    }

}
