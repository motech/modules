package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.DisplayNames;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.constant.TasksDataTypes;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ActionParameterRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Builds a list of {@link ActionEventRequest} from a list of {@link FormDefinition}
 */
public class ActionBuilder {

    private List<FormDefinition> formDefinitions;
    private int count;


    public ActionBuilder(List<FormDefinition> formDefinitions) {
        this.count = 0;
        this.formDefinitions = formDefinitions;
    }

    public List<ActionEventRequest> build() {
        List<ActionEventRequest> actionEventRequests = new ArrayList<>();

        for (FormDefinition formDefinition : formDefinitions) {
            SortedSet<ActionParameterRequest> actionParameterRequests = createParameterRequestsForFormDef(formDefinition);
            ActionEventRequestBuilder builder = new ActionEventRequestBuilder();
            builder
                    .setDisplayName(DisplayNames.SAVE_FORM_INSTANCE + " [Configuration : " + formDefinition.getConfigurationName() + "]" + "[Title: " + formDefinition.getTitle() + "]")
                    .setActionParameters(actionParameterRequests)
                    .setSubject(EventSubjects.PERSIST_FORM_INSTANCE)
                    .setName(formDefinition.getConfigurationName() + "_" + formDefinition.getTitle() + "_" + EventSubjects.PERSIST_FORM_INSTANCE);
            actionEventRequests.add(builder.createActionEventRequest());
        }
        actionEventRequests.add(createFormFailureAction());
        return actionEventRequests;
    }

    private ActionEventRequest createFormFailureAction() {
        SortedSet<ActionParameterRequest> actionParameterRequests = createFormFailureParameters();
        ActionEventRequestBuilder builder = new ActionEventRequestBuilder();
        builder
                .setDisplayName(DisplayNames.SAVE_FORM_FAILURE)
                .setActionParameters(actionParameterRequests)
                .setSubject(EventSubjects.FORM_FAIL)
                .setName(EventSubjects.FORM_FAIL);

        return builder.createActionEventRequest();
    }

    private SortedSet<ActionParameterRequest> createFormFailureParameters() {
        SortedSet<ActionParameterRequest> actionParameterRequests = new TreeSet<ActionParameterRequest>();
        actionParameterRequests.addAll(createTitleAndConfigParameters());

        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder();
        builder
                .setDisplayName(DisplayNames.MESSAGE)
                .setOrder(count++)
                .setType(TasksDataTypes.UNICODE)
                .setKey(EventParameters.MESSAGE);

        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder
                .setDisplayName(DisplayNames.EXCEPTION)
                .setOrder(count++)
                .setType(TasksDataTypes.UNICODE)
                .setKey(EventParameters.EXCEPTION);

        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder
                .setDisplayName(DisplayNames.JSON_CONTENT)
                .setOrder(count++)
                .setType(TasksDataTypes.UNICODE)
                .setKey(EventParameters.JSON_CONTENT);

        actionParameterRequests.add(builder.createActionParameterRequest());

        return actionParameterRequests;
    }

    private SortedSet<ActionParameterRequest> createParameterRequestsForFormDef(FormDefinition formDefinition) {
        SortedSet<ActionParameterRequest> actionParameterRequests = createRequiredFields();
        List<FormElement> formElements = formDefinition.getFormElements();
        ActionParameterRequestBuilder builder;

        for (FormElement formElement : formElements) {
            if (!formElement.isPartOfRepeatGroup() && !formElement.getName().equals(EventParameters.INSTANCE_ID)) {
                builder = new ActionParameterRequestBuilder();
                builder
                        .setDisplayName(formElement.getLabel())
                        .setKey(formElement.getName())
                        .setOrder(count++)
                        .setType(TypeMapper.getType(formElement.getType()));
                actionParameterRequests.add(builder.createActionParameterRequest());
            }
        }
        return actionParameterRequests;
    }

    private SortedSet<ActionParameterRequest> createRequiredFields() {
        SortedSet<ActionParameterRequest> actionParameterRequests = new TreeSet<ActionParameterRequest>();
        actionParameterRequests.addAll(createTitleAndConfigParameters());

        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder();
        builder
                .setDisplayName(DisplayNames.INSTANCE_ID)
                .setOrder(count++)
                .setType(TasksDataTypes.UNICODE)
                .setKey(EventParameters.INSTANCE_ID)
                .setRequired(true);

        actionParameterRequests.add(builder.createActionParameterRequest());

        return actionParameterRequests;
    }

    private SortedSet<ActionParameterRequest> createTitleAndConfigParameters() {
        SortedSet<ActionParameterRequest> actionParameterRequests = new TreeSet<ActionParameterRequest>();
        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder();
        builder
                .setDisplayName(DisplayNames.FORM_TITLE)
                .setType(TasksDataTypes.UNICODE)
                .setKey(EventParameters.FORM_TITLE)
                .setOrder(count++)
                .setRequired(true);

        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder
                .setDisplayName(DisplayNames.CONFIG_NAME)
                .setOrder(count++)
                .setType(TasksDataTypes.UNICODE)
                .setKey(EventParameters.CONFIGURATION_NAME)
                .setRequired(true);

        actionParameterRequests.add(builder.createActionParameterRequest());
        return actionParameterRequests;
    }
}
