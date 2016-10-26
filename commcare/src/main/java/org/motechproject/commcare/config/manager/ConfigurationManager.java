package org.motechproject.commcare.config.manager;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.service.CommcareAppStructureService;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareReportService;
import org.motechproject.commcare.service.ReportsMetadataDataService;
import org.motechproject.commcare.tasks.CommcareTasksNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class is responsible for updating the Commcare schema when called.
 * It clears old schema, and in case of updates or creates fetches the new one from Commcare,
 * which is persisted in the database.
 */
@Component
public class ConfigurationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);

    @Autowired
    private CommcareAppStructureService appStructureService;

    @Autowired
    private CommcareReportService reportService;

    @Autowired
    private CommcareApplicationDataService commcareApplicationDataService;

    @Autowired
    private ReportsMetadataDataService reportsMetadataDataService;

    @Autowired
    private CommcareTasksNotifier commcareTasksNotifier;

    @Transactional
    public void configCreated(String configName) {
        LOGGER.info("Configuration [{}] created, fetching Commcare schema, {}", configName);
        reloadConfig(configName);
    }

    @Transactional
    public void configUpdated(String configName) {
        configUpdated(configName, true);
    }

    @Transactional
    public void configUpdated(String configName, boolean downloadApplicationsAndReports) {
        if (downloadApplicationsAndReports) {
            LOGGER.info("Configuration [{}] updated, fetching Commcare schema, {}", configName);
            reloadConfig(configName);
        } else {
            LOGGER.info("Configuration [{}] updated, not fetching new Commcare schema, {}", configName);
            clearApps(configName);
        }
    }

    @Transactional
    public synchronized void configDeleted(String configName) {
        LOGGER.info("Configuration [{}] deleted", configName);
        clearApps(configName);
        commcareTasksNotifier.updateTasksInfo();
    }

    private synchronized void reloadConfig(String configName) {
        List<CommcareApplicationJson> applications = appStructureService.getAllApplications(configName);
        ReportsMetadataInfo reportsMetadata = reportService.getReportsList(configName);

        // clear the configuration before saving the new one
        clearApps(configName);
        clearReports(configName);

        for (CommcareApplicationJson app : applications) {
            app.setConfigName(configName);
            commcareApplicationDataService.create(app);
        }

        reportsMetadata.setConfigName(configName);
        reportsMetadataDataService.create(reportsMetadata);

        commcareTasksNotifier.updateTasksInfo();
    }

    private void clearApps(String configName) {
        for (CommcareApplicationJson application : commcareApplicationDataService.bySourceConfiguration(configName)) {
            commcareApplicationDataService.delete(application);
        }
    }

    private void clearReports(String configName) {
        for (ReportsMetadataInfo report : reportsMetadataDataService.bySourceConfiguration(configName)) {
            reportsMetadataDataService.delete(report);
        }
    }
}
