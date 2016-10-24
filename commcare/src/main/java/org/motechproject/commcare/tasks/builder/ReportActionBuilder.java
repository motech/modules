package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.report.ReportMetadataFilter;
import org.motechproject.commcare.domain.report.ReportMetadataInfo;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.domain.report.constants.FilterDataType;
import org.motechproject.commcare.domain.report.constants.FilterType;
import org.motechproject.commcare.events.constants.DisplayNames;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;
import org.motechproject.tasks.domain.enums.ParameterType;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class responsible for building the Task module actions, related to sending the
 * forms via submission API. The built list of {@link ActionEventRequest}s instances can
 * be passed to the Task module to register channel actions.
 */
public class ReportActionBuilder implements ActionBuilder {

    private CommcareSchemaService schemaService;
    private CommcareConfigService configService;

    public ReportActionBuilder (CommcareSchemaService schemaService, CommcareConfigService configService) {
        this.schemaService = schemaService;
        this.configService = configService;
    }

    @Override
    public List<ActionEventRequest> buildActions () {
        List<ActionEventRequest> actions = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {
            List<ActionEventRequest> builtActions = buildActionsForConfigs(config);
            actions.addAll(builtActions);
        }

        return actions;
    }

    private List<ActionEventRequest> buildActionsForConfigs (Config config) {
        List<ActionEventRequest> actions = new ArrayList<>();

        for (ReportsMetadataInfo reportsMetadataInfo : schemaService.getReportsMetadata(config.getName())) {
            for (ReportMetadataInfo reportMetadata : reportsMetadataInfo.getReportMetadataInfoList()) {
                SortedSet<ActionParameterRequest> parameters = buildActionParameters(reportMetadata);

                parameters.addAll(prepareCommonParameters(parameters.size(), reportMetadata.getId(), reportMetadata.getTitle()));

                String displayName = DisplayNameHelper.buildDisplayName(DisplayNames.CREATE_REPORT, reportMetadata.getTitle(),
                        config.getName());
                ActionEventRequestBuilder actionBuilder = new ActionEventRequestBuilder()
                        .setDisplayName(displayName)
                        .setSubject(EventSubjects.REPORT_EVENT + "." + reportMetadata.getTitle() + "." + config.getName())
                        .setActionParameters(parameters);

                actions.add(actionBuilder.createActionEventRequest());
            }
        }

        return actions;
    }

    private SortedSet<ActionParameterRequest> buildActionParameters (ReportMetadataInfo reportMetadata) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;

        for (ReportMetadataFilter filter : reportMetadata.getFilters()) {
            if (filter.getDatatype().equals(FilterDataType.DECIMAL) || filter.getDatatype().equals(FilterDataType.INTEGER)) {
                parameters.addAll(prepareNumericParameters(filter, order));
                order = parameters.size();
            } else if (filter.getType().equals(FilterType.DATE)) {
                parameters.addAll(prepareDateParameters(filter, order));
                order = parameters.size();
            } else {
                parameters.addAll(prepareUnicodeParameters(filter, order));
                order = parameters.size();
            }
        }

        return parameters;
    }

    private SortedSet<ActionParameterRequest> prepareNumericParameters (ReportMetadataFilter filter, int startOrder) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = startOrder;

        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder()
                .setDisplayName(filter.getSlug())
                .setKey(filter.getSlug())
                .setType(ParameterType.SELECT.getValue())
                .setOptions(createNumericFilterOperators())
                .setOrder(order++);

        parameters.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder()
                .setDisplayName(filter.getSlug() + " " + EventDataKeys.VALUE)
                .setKey(filter.getSlug() + "." + EventDataKeys.VALUE)
                .setType(ParameterType.UNICODE.getValue())
                .setOrder(order++);

        parameters.add(builder.createActionParameterRequest());

        parameters.add(prepareFilterTypeParameter(filter, FilterDataType.DECIMAL.toString(), order++));

        return parameters;
    }

    private SortedSet<ActionParameterRequest> prepareDateParameters (ReportMetadataFilter filter, int startOrder) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = startOrder;

        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder()
                .setDisplayName(filter.getSlug())
                .setKey(filter.getSlug())
                .setType(ParameterType.DATE.getValue())
                .setOrder(order++);

        parameters.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder()
                .setDisplayName(DisplayNames.END_DATE)
                .setKey(filter.getSlug() + "." + DisplayNames.END_DATE)
                .setType(ParameterType.DATE.getValue())
                .setOrder(order++);

        parameters.add(builder.createActionParameterRequest());

        parameters.add(prepareFilterTypeParameter(filter, FilterType.DATE.toString(), order++));

        return parameters;
    }

    private SortedSet<ActionParameterRequest> prepareUnicodeParameters (ReportMetadataFilter filter, int startOrder) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = startOrder;

        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder()
                .setDisplayName(filter.getSlug())
                .setKey(filter.getSlug())
                .setType(ParameterType.UNICODE.getValue())
                .setOrder(order++);

        parameters.add(builder.createActionParameterRequest());

        parameters.add(prepareFilterTypeParameter(filter, FilterType.CHOICE_LIST.toString(), order++));

        return parameters;
    }

    private ActionParameterRequest prepareFilterTypeParameter (ReportMetadataFilter filter, String type, int order) {
        return new ActionParameterRequestBuilder()
                .setDisplayName(filter.getSlug() + "." + EventDataKeys.TYPE)
                .setKey(filter.getSlug() + "." + EventDataKeys.TYPE)
                .setValue(type)
                .setHidden(true)
                .setOrder(order)
                .createActionParameterRequest();
    }

    private SortedSet<ActionParameterRequest> prepareCommonParameters (int startOrder, String reportId, String reportName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = startOrder;

        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder()
                .setDisplayName(EventDataKeys.REPORT_ID)
                .setKey(EventDataKeys.REPORT_ID)
                .setValue(reportId)
                .setHidden(true)
                .setOrder(order++);

        parameters.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder()
                .setDisplayName(EventDataKeys.REPORT_NAME)
                .setKey(EventDataKeys.REPORT_NAME)
                .setValue(reportName)
                .setHidden(true)
                .setOrder(order);

        parameters.add(builder.createActionParameterRequest());

        return parameters;
    }

    private SortedSet<String> createNumericFilterOperators () {
        SortedSet<String> availableOperators = new TreeSet<>();

        availableOperators.add("=");
        availableOperators.add("!=");
        availableOperators.add("<");
        availableOperators.add("<=");
        availableOperators.add(">");
        availableOperators.add(">=");

        return availableOperators;
    }
}
