package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.domain.FormSchemaQuestionJson;
import org.motechproject.commcare.domain.FormSchemaQuestionOptionJson;
import org.motechproject.commcare.events.constants.DisplayNames;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareApplicationService;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ActionParameterRequestBuilder;
import org.motechproject.tasks.domain.ParameterType;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class responsible for building the Task module actions, related to sending the
 * forms via submission API. The built list of {@link ActionEventRequest}s instances can
 * be passed to the Task module to register channel actions.
 */
public class FormActionBuilder implements ActionBuilder {

    private CommcareApplicationService applicationService;
    private CommcareConfigService configService;

    public FormActionBuilder(CommcareApplicationService applicationService, CommcareConfigService configService) {
        this.applicationService = applicationService;
        this.configService = configService;
    }

    @Override
    public List<ActionEventRequest> buildActions() {
        List<ActionEventRequest> actions = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {
            List<ActionEventRequest> builtActions = buildActionsForConfig(config);
            actions.addAll(builtActions);
        }

        return actions;
    }

    private List<ActionEventRequest> buildActionsForConfig(Config config) {
        List<ActionEventRequest> actions = new ArrayList<>();
        
        for (CommcareApplicationJson application : applicationService.getByConfigName(config.getName())) {
            for (CommcareModuleJson module : application.getModules()) {
                for (FormSchemaJson form : module.getFormSchemas()) {
                    SortedSet<ActionParameterRequest> parameters = buildActionParameters(form);

                    String displayName = DisplayNameHelper.buildDisplayName(DisplayNames.SUBMIT_FORM, form.getFormName(),
                            application.getApplicationName(), config.getName());
                    ActionEventRequestBuilder actionBuilder = new ActionEventRequestBuilder()
                            .setDisplayName(displayName)
                            .setSubject(EventSubjects.SUBMIT_FORM + "." + form.getXmlns() + "." + config.getName())
                            .setActionParameters(parameters);
                    actions.add(actionBuilder.createActionEventRequest());
                }
            }
        }

        return actions;
    }

    private SortedSet<ActionParameterRequest> buildActionParameters(FormSchemaJson form) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;

        for (FormSchemaQuestionJson question : form.getQuestions()) {
            ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder();
            builder.setDisplayName(question.getQuestionLabel())
                    .setKey(question.getQuestionValue())
                    .setOrder(order++);

            if (question.getOptions() != null && !question.getOptions().isEmpty()) {
                builder.setType(ParameterType.SELECT.getValue());
                builder.setOptions(convertCommcareoptionsToTaskActionOptions(question.getOptions()));
            } else {
                builder.setType(ParameterType.UNICODE.getValue());
            }

            parameters.add(builder.createActionParameterRequest());
        }

        return parameters;
    }

    private SortedSet<String> convertCommcareoptionsToTaskActionOptions(List<FormSchemaQuestionOptionJson> options) {
        SortedSet availableOptions = new TreeSet<>();

        for (FormSchemaQuestionOptionJson option : options) {
            availableOptions.add(option.getValue());
        }
        return availableOptions;
    }
}
