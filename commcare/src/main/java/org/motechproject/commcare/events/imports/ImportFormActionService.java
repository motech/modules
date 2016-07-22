package org.motechproject.commcare.events.imports;

import org.joda.time.DateTime;

/**
 * Created by root on 22.07.16.
 */
public interface ImportFormActionService {
    void importForms(String configName, DateTime startDate, DateTime endDate);
}
