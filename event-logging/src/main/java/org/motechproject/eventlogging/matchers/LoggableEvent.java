package org.motechproject.eventlogging.matchers;

import org.motechproject.event.MotechEvent;

import java.util.Collections;
import java.util.List;

/**
 * Class containing event data. The field eventSubjects contains names of subjects for
 * events that should be logged by {@link org.motechproject.eventlogging.loggers.EventLogger} and
 * flags are used to filter those events.
 */
public class LoggableEvent {

    private List<String> eventSubjects;
    private List<? extends EventFlag> flags;

    public List<String> getEventSubjects() {
        return eventSubjects;
    }

    public List<? extends EventFlag> getFlags() {
        return flags;
    }

    public void setFlags(List<? extends EventFlag> flags) {
        this.flags = flags;
    }

    public void setEventSubjects(List<String> eventSubjects) {
        this.eventSubjects = eventSubjects;
    }

    /**
     * Create an instance of LoggableEvent using passed parameters
     *
     * @param eventSubjects list of event subjects to log
     * @param flags event flags used for filter events by parameters
     */
    public LoggableEvent(List<String> eventSubjects, List<? extends EventFlag> flags) {
        if (eventSubjects == null) {
            this.eventSubjects = Collections.<String> emptyList();
        } else {
            this.eventSubjects = eventSubjects;
        }
        if (flags == null) {
            this.flags = Collections.<EventFlag> emptyList();
        } else {
            this.flags = flags;
        }
    }

    /**
     * Checks if the event should be logged by {@link org.motechproject.eventlogging.loggers.EventLogger}.
     *
     * @param eventToLog incoming motech event to check if should be logged
     * @return true if event should be logged or false if it should not
     */
    public boolean isLoggableEvent(MotechEvent eventToLog) {
        for (String eventSubject : eventSubjects) {
            if (eventToLog.getSubject().equals(eventSubject)) {
                return checkFlags(eventToLog);
            }
            if (eventSubject.endsWith("*")) {
                if (checkWildCardMatch(eventToLog, eventSubject)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkWildCardMatch(MotechEvent eventToLog, String eventSubject) {
        String[] eventPath = eventToLog.getSubject().split("\\.");
        String[] eventLogPath = eventSubject.split("\\.");

        if (eventLogPath.length <= 1) {
            return checkFlags(eventToLog);
        }

        if (eventLogPath.length <= eventPath.length) {

            for (int i = 0; i < eventLogPath.length - 1; i++) {
                if (!eventLogPath[i].equals(eventPath[i])) {
                    break;
                } else if (i == eventLogPath.length - 2) {
                    return checkFlags(eventToLog);
                }
            }
        } else if (eventLogPath.length - 1 == eventPath.length) {
            for (int i = 0; i < eventLogPath.length - 1; i++) {
                if (!eventLogPath[i].equals(eventPath[i])) {
                    break;
                } else if (i == eventLogPath.length - 2) {
                    return checkFlags(eventToLog);
                }
            }
        }

        return false;
    }

    private boolean checkFlags(MotechEvent eventToLog) {
        for (EventFlag eventFlag : flags) {
            if (!eventFlag.passesFlags(eventToLog)) {
                return false;
            }
        }
        return true;
    }

}
