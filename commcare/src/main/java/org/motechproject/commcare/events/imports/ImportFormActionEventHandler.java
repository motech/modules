package org.motechproject.commcare.events.imports;

import org.joda.time.DateTime;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.pull.CommcareFormImporterImpl;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.commons.api.Range;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.commcare.events.constants.EventDataKeys.END_DATE;
import static org.motechproject.commcare.events.constants.EventDataKeys.START_DATE;

/**
 * Class responsible for handling "Import Forms" action in tasks.
 */
@Component
public class ImportFormActionEventHandler {

    @Autowired
    private EventRelay eventRelay;

    @Autowired
    private CommcareFormService formService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportFormActionEventHandler.class);

    /**
     * Handles the {@code EventSubjects.IMPORT_FORMS} events. This will import commcare forms for specific configuration
     *
     * @param event  the event to be handled
     */
    @MotechListener(subjects = EventSubjects.IMPORT_FORMS + ".*")
    public void handleEvent(MotechEvent event) {

        String configName = EventSubjects.getConfigName(event.getSubject());
        DateTime startDate = (DateTime) event.getParameters().get(START_DATE);
        DateTime endDate = (DateTime) event.getParameters().get(END_DATE);
        Range<DateTime> dateRange = new Range<>(startDate, endDate);

        CommcareFormImporterImpl importer = new CommcareFormImporterImpl(eventRelay, formService);
        int formsToImport = importer.countForImport(dateRange, configName);

        LOGGER.info("{} commcare forms to be imported.", formsToImport);

        importer.startImport(dateRange, configName);
    }
}
