package org.motechproject.eventlogging.service.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.eventlogging.matchers.LoggableEvent;
import org.motechproject.eventlogging.loggers.EventLogger;
import org.motechproject.eventlogging.loggers.impl.FileEventLogger;
import org.motechproject.eventlogging.service.EventLoggingService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link org.motechproject.eventlogging.service.EventLoggingService}
 * It is used to log events to files.
 */
public class FileEventLoggingService implements EventLoggingService {

    private List<FileEventLogger> fileEventLoggers = new ArrayList<>();

    /**
     * Creates an instance of FileEventLoggingService without any loggers.
     */
    public FileEventLoggingService() {

    }

    /**
     * Creates an instance of FileEventLoggingService and adds to it all loggers
     * passed as a parameter.
     *
     * @param fileEventLoggers list of loggers to add to this service
     */
    public FileEventLoggingService(List<FileEventLogger> fileEventLoggers) {
        if (fileEventLoggers != null) {
            this.fileEventLoggers = fileEventLoggers;
        }
    }

    @Override
    public void logEvent(MotechEvent event) {
        for (FileEventLogger fileEventLogger : fileEventLoggers) {
            fileEventLogger.log(event);
        }
    }

    @Override
    public Set<String> getLoggedEventSubjects() {
        Set<String> eventSubjectsSet = new HashSet<String>();

        for (EventLogger eventLogger : fileEventLoggers) {
            List<LoggableEvent> events = eventLogger.getLoggableEvents();
            for (LoggableEvent event : events) {
                List<String> eventSubjects = event.getEventSubjects();
                eventSubjectsSet.addAll(eventSubjects);
            }
        }

        return eventSubjectsSet;
    }

}
