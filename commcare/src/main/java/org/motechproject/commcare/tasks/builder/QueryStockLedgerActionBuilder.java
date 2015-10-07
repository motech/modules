package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.events.constants.DisplayNames;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ActionParameterRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.motechproject.tasks.domain.ParameterType.DATE;
import static org.motechproject.tasks.domain.ParameterType.MAP;
import static org.motechproject.tasks.domain.ParameterType.UNICODE;

/**
 * Responsible for building actions for querying stock ledger for all configurations. There action can then be used in
 * the task module.
 */
public class QueryStockLedgerActionBuilder {

    private CommcareConfigService configService;

    public QueryStockLedgerActionBuilder(CommcareConfigService configService) {
        this.configService = configService;
    }

    /**
     * Builds actions for all configuration that can be used for querying stock ledger on the CommCareHQ server.
     *
     * @return the list of "Query Stock Ledger [{configName}]" actions for all configurations
     */
    public List<ActionEventRequest> buildActions() {

        List<ActionEventRequest> actions = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {
            SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
            ActionParameterRequestBuilder builder;
            int order = 0;

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.CASE_ID)
                    .setKey(EventDataKeys.CASE_ID)
                    .setType(UNICODE.getValue())
                    .setRequired(true)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.SECTION_ID)
                    .setKey(EventDataKeys.SECTION_ID)
                    .setType(UNICODE.getValue())
                    .setRequired(true)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.START_DATE)
                    .setKey(EventDataKeys.START_DATE)
                    .setType(DATE.getValue())
                    .setRequired(false)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.END_DATE)
                    .setKey(EventDataKeys.END_DATE)
                    .setType(DATE.getValue())
                    .setRequired(false)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.EXTRA_DATA)
                    .setKey(EventDataKeys.EXTRA_DATA)
                    .setType(MAP.getValue())
                    .setRequired(false)
                    .setOrder(order);
            parameters.add(builder.createActionParameterRequest());

            String displayName = DisplayNameHelper.buildDisplayName(DisplayNames.QUERY_STOCK_LEDGER, config.getName());

            ActionEventRequestBuilder actionBuilder = new ActionEventRequestBuilder()
                    .setDisplayName(displayName)
                    .setSubject(EventSubjects.QUERY_STOCK_LEDGER + "." + config.getName())
                    .setActionParameters(parameters);
            actions.add(actionBuilder.createActionEventRequest());
        }

        return actions;
    }
}
