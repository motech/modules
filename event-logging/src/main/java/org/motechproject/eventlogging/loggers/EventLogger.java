package org.motechproject.eventlogging.loggers;

import org.motechproject.event.MotechEvent;
import org.motechproject.eventlogging.matchers.LoggableEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a generic event logger. All loggers have a list of
 * loggable events that they should be able to provide logging functionality for.
 */
public abstract class EventLogger {

    private List<LoggableEvent> loggableEvents = new ArrayList<>();

    /**
     * Adds data about events to log to loggableEvents list in the form of
     * {@link LoggableEvent} objects.
     *
     * @param loggableEvents list of loggable events, which should be added to list of events to log
     */
    public void addLoggableEvents(List<LoggableEvent> loggableEvents) {
        this.loggableEvents.addAll(loggableEvents);
    }

    /**
     * Removes data about events to log from the loggableEvents list in the form of
     * {@link LoggableEvent} objects.
     *
     * @param loggableEvents list of loggable events, which should be removed from the list of events to log
     */
    public void removeLoggableEvents(List<LoggableEvent> loggableEvents) {
        this.loggableEvents.removeAll(loggableEvents);
    }

    /**
     * Removes all data about events to log by clearing the loggableEvents list.
     */
    public void clearLoggableEvents() {
        loggableEvents.clear();
    }

    /**
     * Returns data about all events to log
     *
     * @return list of {@link LoggableEvent} objects which contains data about events that should be added to the log
     */
    public List<LoggableEvent> getLoggableEvents() {
        return loggableEvents;
    }

    /**
     * Method used to store data about events.
     *
     * @param eventToLog the incoming motech event to log
     */
    public abstract void log(MotechEvent eventToLog);

}
