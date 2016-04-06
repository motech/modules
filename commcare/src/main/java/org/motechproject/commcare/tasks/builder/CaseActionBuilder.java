package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.events.constants.DisplayNames;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.motechproject.tasks.domain.mds.ParameterType.BOOLEAN;
import static org.motechproject.tasks.domain.mds.ParameterType.MAP;
import static org.motechproject.tasks.domain.mds.ParameterType.UNICODE;

/**
 * Class responsible for building the Task module actions, related to the case submission.
 * The built list of {@link ActionEventRequest}s instances can be passed to the Task module
 * to register channel actions.
 */
public class CaseActionBuilder implements ActionBuilder {

    private CommcareConfigService configService;

    public CaseActionBuilder(CommcareConfigService configService) {
        this.configService = configService;
    }

    @Override
    public List<ActionEventRequest> buildActions() {

        List<ActionEventRequest> actions = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {
            SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
            ActionParameterRequestBuilder builder;
            int order = 0;

            //  Builds CREATE CASE task action
            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.CASE_TYPE)
                    .setKey(EventDataKeys.CASE_TYPE)
                    .setType(UNICODE.getValue())
                    .setRequired(true)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.OWNER_ID)
                    .setKey(EventDataKeys.OWNER_ID)
                    .setType(UNICODE.getValue())
                    .setRequired(false)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.CASE_NAME)
                    .setKey(EventDataKeys.CASE_NAME)
                    .setType(UNICODE.getValue())
                    .setRequired(true)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.CASE_PROPERTIES)
                    .setKey(EventDataKeys.FIELD_VALUES)
                    .setType(MAP.getValue())
                    .setRequired(false)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            String displayName = DisplayNameHelper.buildDisplayName(DisplayNames.CREATE_CASE, config.getName());
            ActionEventRequestBuilder actionBuilder = new ActionEventRequestBuilder()
                    .setDisplayName(displayName)
                    .setSubject(EventSubjects.CREATE_CASE + "." + config.getName())
                    .setActionParameters(parameters);
            actions.add(actionBuilder.createActionEventRequest());

            // Builds UPDATE CASE task action
            parameters = new TreeSet<>();
            order = 0;
            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.CASE_ID)
                    .setKey(EventDataKeys.CASE_ID)
                    .setType(UNICODE.getValue())
                    .setRequired(true)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.OWNER_ID)
                    .setKey(EventDataKeys.OWNER_ID)
                    .setType(UNICODE.getValue())
                    .setRequired(true)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.CLOSE_CASE)
                    .setKey(EventDataKeys.CLOSE_CASE)
                    .setType(BOOLEAN.getValue())
                    .setRequired(false)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            builder = new ActionParameterRequestBuilder()
                    .setDisplayName(DisplayNames.CASE_PROPERTIES)
                    .setKey(EventDataKeys.FIELD_VALUES)
                    .setType(MAP.getValue())
                    .setRequired(false)
                    .setOrder(order++);
            parameters.add(builder.createActionParameterRequest());

            displayName = DisplayNameHelper.buildDisplayName(DisplayNames.UPDATE_CASE, config.getName());
            actionBuilder = new ActionEventRequestBuilder()
                    .setDisplayName(displayName)
                    .setSubject(EventSubjects.UPDATE_CASE + "." + config.getName())
                    .setActionParameters(parameters);
            actions.add(actionBuilder.createActionEventRequest());
        }

        return actions;
    }

}
