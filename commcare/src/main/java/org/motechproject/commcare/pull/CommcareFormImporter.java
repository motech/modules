package org.motechproject.commcare.pull;

import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;

/**
 * Created by GE0S0_000 on 2015-08-10.
 */
public interface CommcareFormImporter {

    int countForFormImport(Range<DateTime> dateRange, String configName);

    void startFormImport(final Range<DateTime> dateRange, final String configName);

    void stopImport();

    boolean isImportInProgress();

    FormImportStatus importStatus();
}
