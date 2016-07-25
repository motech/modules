package org.motechproject.commcare.events.imports;

import org.joda.time.DateTime;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.imports.ImportFormActionService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.motechproject.commcare.events.constants.EventDataKeys.END_DATE;
import static org.motechproject.commcare.events.constants.EventDataKeys.START_DATE;

/**
 * Class responsible for handling "Import Forms" action events and forwarding them
 * to {@link ImportFormActionService} service.
 */
@Component
public class ImportFormActionEventHandler {

    @Autowired
    private ImportFormActionService importFormActionService;

    /**
     * Handles the {@code EventSubjects.IMPORT_FORMS} events. This will import commcare forms for specific configuration
     *
     * @param event  the event to be handled
     */
    @MotechListener(subjects = EventSubjects.IMPORT_FORMS + ".*")
    public void handleEvent(MotechEvent event) {
        String configName = EventSubjects.getConfigName(event.getSubject());
        Map<String, Object> parameters = event.getParameters();

        DateTime startDate = (DateTime) parameters.get(START_DATE);
        DateTime endDate = (DateTime) parameters.get(END_DATE);

        importFormActionService.importForms(configName, startDate, endDate);
    }
}
