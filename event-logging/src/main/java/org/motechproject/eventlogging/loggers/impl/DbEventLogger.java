package org.motechproject.eventlogging.loggers.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.eventlogging.converter.impl.DefaultDbToLogConverter;
import org.motechproject.eventlogging.domain.EventLog;
import org.motechproject.eventlogging.loggers.EventLogger;
import org.motechproject.eventlogging.matchers.LoggableEvent;
import org.motechproject.eventlogging.matchers.MappedLoggableEvent;
import org.motechproject.eventlogging.service.EventLogService;

/**
 * Implementation of the {@link EventLogger} class.
 * The <code>DbEventLogger</code> class is responsible for logging
 * event records in the database.
 */
public class DbEventLogger extends EventLogger {

    private EventLogService eventLogService;
    private DefaultDbToLogConverter eventConverter;

    /**
     * Creates an instance of DbEventLogger which provides logic for
     * persisting logs in the database.
     *
     * @param eventLogService Motech Data Service for EventLogs, generated and injected by MDS module
     * @param eventConverter {@link DefaultDbToLogConverter} responsible for converting incoming events to database persisting state
     */
    public DbEventLogger(EventLogService eventLogService, DefaultDbToLogConverter eventConverter) {
        this.eventLogService = eventLogService;
        this.eventConverter = eventConverter;
    }

    @Override
    public void log(MotechEvent eventToLog) {
        for (LoggableEvent loggableEvent : getLoggableEvents()) {
            if (loggableEvent.isLoggableEvent(eventToLog)) {
                if (eventConverter != null) {
                    EventLog eventLog;
                    if (loggableEvent instanceof MappedLoggableEvent) {
                        eventLog = eventConverter.configuredConvertEventToDbLog(eventToLog, (MappedLoggableEvent) loggableEvent);
                    } else {
                        eventLog = eventConverter.convertToLog(eventToLog);
                    }
                    eventLogService.create(eventLog);
                } else {
                    return;
                }
            }
        }
    }

}
