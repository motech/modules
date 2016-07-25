package org.motechproject.commcare.service.imports;

import org.joda.time.DateTime;

/**
 * This service is responsible for handling "Import Forms" action in tasks.
 */
public interface ImportFormActionService {
    void importForms(String configName, DateTime startDate, DateTime endDate);
}
