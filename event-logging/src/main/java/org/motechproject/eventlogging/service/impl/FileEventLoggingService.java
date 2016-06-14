package org.motechproject.eventlogging.service.impl;

import org.motechproject.config.SettingsFacade;
import org.motechproject.event.MotechEvent;
import org.motechproject.eventlogging.converter.impl.DefaultFileToLogConverter;
import org.motechproject.eventlogging.loggers.EventLogger;
import org.motechproject.eventlogging.loggers.impl.FileEventLogger;
import org.motechproject.eventlogging.matchers.LoggableEvent;
import org.motechproject.eventlogging.matchers.MappingsJson;
import org.motechproject.eventlogging.repository.AllEventMappings;
import org.motechproject.eventlogging.service.Constants;
import org.motechproject.eventlogging.service.EventLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link org.motechproject.eventlogging.service.EventLoggingService}
 * It is used to log events to files.
 */
@Service
public class FileEventLoggingService implements EventLoggingService {

    private List<FileEventLogger> fileEventLoggers = new ArrayList<>();

    @Autowired
    private DefaultFileToLogConverter defaultFileToLogConverter;

    @Autowired
    private AllEventMappings allEventMappings;

    private SettingsFacade settingsFacade;


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

    @Autowired
    public void setSettingsFacade(@Qualifier("eventLoggingSettings") SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
    }

    @PostConstruct
    public void configureFileLoggingService() {
        List<File> loggingFiles = new ArrayList<>();
        String logFilePath = settingsFacade.getProperty(Constants.LOGFILE_PATH_PROPERTY);
        loggingFiles.add(new File(logFilePath));

        List<LoggableEvent> loggableEvents = new ArrayList<>();
        List<MappingsJson> allMappings = allEventMappings.getAllMappings();
        for (MappingsJson mapping : allMappings) {
            LoggableEvent event = new LoggableEvent(mapping.getSubjects(), mapping.getFlags());
            loggableEvents.add(event);
        }

        FileEventLogger logger = new FileEventLogger(loggableEvents, loggingFiles, defaultFileToLogConverter);
        fileEventLoggers.add(logger);
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
