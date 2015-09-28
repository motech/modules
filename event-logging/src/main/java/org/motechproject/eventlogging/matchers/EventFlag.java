package org.motechproject.eventlogging.matchers;

import org.motechproject.event.MotechEvent;

/**
 * Interface representing event flags corresponding to event parameters,
 * used to filter events that should be logged.
 */
public interface EventFlag {

    /**
     * Checks if motech event parameters contains all expected flags.
     *
     * @param event motech event which we check for passing flags
     * @return true if motech event contains all expected flags, false otherwise
     */
    boolean passesFlags(MotechEvent event);
}
