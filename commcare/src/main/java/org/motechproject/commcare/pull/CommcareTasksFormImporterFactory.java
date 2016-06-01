package org.motechproject.commcare.pull;

import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    /**
     * Retrieves an importer instance.
     * @return the importer instance for event
     */
    public CommcareFormImporterImpl getCommcareFormImporter() {
        return new CommcareFormImporterImpl(eventRelay, formService);
    }

    public void setEventRelay(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    public void setFormService(CommcareFormService formService) {
        this.formService = formService;
    }

}
