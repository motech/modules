package org.motechproject.commcare.service.impl.imports;

import org.joda.time.DateTime;
import org.motechproject.commcare.pull.CommcareFormImporterImpl;
import org.motechproject.commcare.pull.CommcareTasksFormImporterFactory;
import org.motechproject.commcare.service.imports.ImportFormActionService;
import org.motechproject.commons.api.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("importFormActionService")
public class ImportFormActionServiceImpl implements ImportFormActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportFormActionServiceImpl.class);

    private CommcareTasksFormImporterFactory importerFactory;

    @Autowired
    public ImportFormActionServiceImpl(CommcareTasksFormImporterFactory importerFactory) {
        this.importerFactory = importerFactory;
    }

    @Override
    public void importForms(String configName, DateTime startDate, DateTime endDate) {
        CommcareFormImporterImpl importer = importerFactory.getCommcareFormImporter();

        Range<DateTime> dateRange = new Range<>(startDate, endDate);

        int formsToImport = importer.countForImport(dateRange, configName);

        LOGGER.info("{} commcare forms to be imported.", formsToImport);

        importer.startImport(dateRange, configName);
    }
}
