package org.motechproject.commcare.tasks.imports;

import org.joda.time.DateTime;

/**
 * This service is responsible for handling import form actions in tasks.
 */
public interface ImportFormActionService {

    /**
     * This will import commcare forms for specific configuration. All forms will be imported or
     * a subset of forms based on a particular datetime range.
     *
     * @param configName  the name of the configuration used for connecting to CommcareHQ
     * @param startDate  the beginning of a datetime range
     * @param endDate  the finish of a datetime range
     */
    void importForms(String configName, DateTime startDate, DateTime endDate);
}
