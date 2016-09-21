package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.events.constants.DisplayNames;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ActionParameterHelper;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.motechproject.tasks.domain.enums.ParameterType.DATE;

/**
 * Responsible for building actions for importing forms for all configurations. There action can then be used in
 * the task module.
 */
public class ImportFormActionBuilder implements ActionBuilder {

    private static final String COMMCARE_ACTION_PROXY_SERVICE = "org.motechproject.commcare.tasks.CommcareActionProxyService";

    private CommcareConfigService configService;

    public ImportFormActionBuilder(CommcareConfigService configService) {
        this.configService = configService;
    }

    /**
     * Builds actions for all configuration that can be used for importing forms.
     *
     * @return the list of "Import Forms [{configName}]" actions for all configurations
     */
    @Override
    public List<ActionEventRequest> buildActions() {

        List<ActionEventRequest> actions = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {

            String configName = config.getName();
            SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
            ActionParameterRequestBuilder builder;
            String serviceMethod = "importForms";
            int order = 0;

            parameters.add(ActionParameterHelper.createConfigNameParameter(configName, order));
            order++;

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.START_DATE)
                    .setKey(EventDataKeys.START_DATE)
                    .setType(DATE.getValue())
                    .setRequired(true)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.END_DATE)
                    .setKey(EventDataKeys.END_DATE)
                    .setType(DATE.getValue())
                    .setRequired(true)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            String displayName = DisplayNameHelper.buildDisplayName(DisplayNames.IMPORT_FORMS, configName);

            ActionEventRequestBuilder actionBuilder = new ActionEventRequestBuilder()
                    .setDisplayName(displayName)
                    .setServiceInterface(COMMCARE_ACTION_PROXY_SERVICE)
                    .setServiceMethod(serviceMethod)
                    .setSubject(EventSubjects.IMPORT_FORMS + "." + configName)
                    .setActionParameters(parameters);
            actions.add(actionBuilder.createActionEventRequest());
        }

        return actions;
    }
}
