package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.report.ReportMetadataColumn;
import org.motechproject.commcare.domain.report.ReportMetadataInfo;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.commcare.events.constants.EventDataKeys.CONFIG_NAME;

/**
 * The <code>ReportTriggerBuilder</code> class builds report triggers for each report
 * present in MOTECH database. Each trigger has got its own attributes, depending on
 * the case properties of each type. Some fields are common for all reports.
 */
public class ReportTriggerBuilder implements TriggerBuilder {

    private CommcareSchemaService schemaService;
    private CommcareConfigService configService;

    private static final String RECEIVED_CASE = "Received Report";
    private static final String COLUMN_NAME_SUFFIX = " Column Name";

    /**
     * Creates an instance of the {@link ReportTriggerBuilder} class. It will use the given {@code schemaService} and
     * {@code configService} for creating report triggers.
     *
     * @param schemaService the schema service
     * @param configService  the configuration service
     */
    public ReportTriggerBuilder (CommcareSchemaService schemaService, CommcareConfigService configService) {
        this.schemaService = schemaService;
        this.configService = configService;
    }

    @Override
    public List<TriggerEventRequest> buildTriggers () {
        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {
            triggers.addAll(buildTriggersForConfig(config));
        }

        return triggers;
    }

    private List<TriggerEventRequest> buildTriggersForConfig (Config config) {
        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (ReportsMetadataInfo reportMetadataInfo : schemaService.getReportsMetadata(config.getName())) {
            for (ReportMetadataInfo reportMetadata : reportMetadataInfo.getReportMetadataInfoList()) {
                String displayName = DisplayNameHelper.buildDisplayName(RECEIVED_CASE, reportMetadata.getTitle(),
                        config.getName());
                triggers.add(new TriggerEventRequest(displayName, EventSubjects.RECEIVED_REPORT + "." + config.getName() + "." + reportMetadata.getId(),
                        null, createTriggerParameters(reportMetadata)));
            }
        }

        return triggers;
    }

    private List<EventParameterRequest> createTriggerParameters (ReportMetadataInfo reportMetadata) {
        List<EventParameterRequest> parameters = new ArrayList<>();

        parameters.add(new EventParameterRequest("commcare.field.configName", CONFIG_NAME));

        for (ReportMetadataColumn column : reportMetadata.getColumns()) {
            parameters.add(new EventParameterRequest(column.getDisplay() + COLUMN_NAME_SUFFIX, column.getDisplay()));
        }

        return parameters;
    }
}
