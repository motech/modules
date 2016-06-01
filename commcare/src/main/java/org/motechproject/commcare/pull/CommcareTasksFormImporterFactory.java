package org.motechproject.commcare.pull;

import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the factory responsible for providing instances {@link CommcareFormImporter} to
 * import form event handling {@link org.motechproject.commcare.events.imports.ImportFormActionEventHandler}
 */
@Component
public class CommcareTasksFormImporterFactory {

    @Autowired
    private EventRelay eventRelay;

    @Autowired
    private CommcareFormService formService;

    private final Map<String, CommcareFormImporterImpl> importerMap = new HashMap<>();

    /**
     * Retrieves an importer instance for the given event. A new instance will be created if it doesn't yet exist.
     * @param event MotechEvent for which the importer should be retrieved.
     * @return the importer instance for event
     * @throws IllegalArgumentException if event is null
     */
    public CommcareFormImporterImpl getCommcareFormImporter(MotechEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("No event provided, importers must be tied with an MotechEvent");
        }

        String subject = event.getSubject();

        if (!importerMap.containsKey(subject)) {
            importerMap.put(subject, new CommcareFormImporterImpl(eventRelay, formService));
        }

        return importerMap.get(subject);
    }

    public void setEventRelay(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    public void setFormService(CommcareFormService formService) {
        this.formService = formService;
    }

}
