package org.motechproject.eventlogging.matchers;

import java.util.List;

/**
 * Version of {@link LoggableEvent} class used when working with mappings. Additionaly to LoggableEvent it
 * stores {@link LogMappings} used for manipulating event parameters before persisting log.
 */
public class MappedLoggableEvent extends LoggableEvent {

    private LogMappings mappings;

    /**
     * Creates an instance of MappedLoggableEvent using passed parameters
     *
     * @param eventSubjects list of event subjects to log
     * @param flags event flags used for filtering events by parameters
     * @param mappings {@link LogMappings} object used for replacing, including and excluding parameters before logging to the database
     */
    public MappedLoggableEvent(List<String> eventSubjects, List<? extends EventFlag> flags, LogMappings mappings) {
        super(eventSubjects, flags);
        this.mappings = mappings;
    }

    public LogMappings getMappings() {
        return mappings;
    }

    public void setMappings(LogMappings mappings) {
        this.mappings = mappings;
    }
}
