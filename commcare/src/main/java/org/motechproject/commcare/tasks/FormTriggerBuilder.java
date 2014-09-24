package org.motechproject.commcare.tasks;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.domain.FormSchemaQuestionJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.commcare.events.constants.EventDataKeys.META_APP_VERSION;
import static org.motechproject.commcare.events.constants.EventDataKeys.META_DEVICE_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.META_INSTANCE_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.META_TIME_END;
import static org.motechproject.commcare.events.constants.EventDataKeys.META_TIME_START;
import static org.motechproject.commcare.events.constants.EventDataKeys.META_USERNAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.META_USER_ID;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_EVENT;

/**
 * The <code>FormTriggerBuilder</code> class builds tasks triggers for each form
 * present in MOTECH database. Each trigger has got its own attributes, depending on the
 * actual fields present in a certain form.
 */
public class FormTriggerBuilder implements TriggerBuilder {

    private CommcareApplicationDataService applicationDataService;

    private static final String RECEIVED_FORM_PREFIX = "Received Form: ";

    public FormTriggerBuilder(CommcareApplicationDataService applicationDataService) {
        this.applicationDataService = applicationDataService;
    }

    @Override
    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (FormSchemaJson form : getAllFormSchemas()) {
            List<EventParameterRequest> parameters = new ArrayList<>();
            String formName = form.getFormNames().get("en");
            addMetadataFields(parameters);

            for (FormSchemaQuestionJson question : form.getQuestions()) {
                parameters.add(new EventParameterRequest(question.getQuestionLabel(), question.getQuestionValue()));
            }

            triggers.add(new TriggerEventRequest(RECEIVED_FORM_PREFIX + formName, FORMS_EVENT + "." + formName, null, parameters, FORMS_EVENT));
        }

        return triggers;
    }

    private void addMetadataFields(List<EventParameterRequest> parameters) {
        parameters.add(new EventParameterRequest("commcare.form.field.instanceId", META_INSTANCE_ID));
        parameters.add(new EventParameterRequest("commcare.form.field.userId", META_USER_ID));
        parameters.add(new EventParameterRequest("commcare.form.field.deviceId", META_DEVICE_ID));
        parameters.add(new EventParameterRequest("commcare.form.field.username", META_USERNAME));
        parameters.add(new EventParameterRequest("commcare.form.field.appVersion", META_APP_VERSION));
        parameters.add(new EventParameterRequest("commcare.form.field.timeStart", META_TIME_START));
        parameters.add(new EventParameterRequest("commcare.form.field.timeEnd", META_TIME_END));
    }

    private List<FormSchemaJson> getAllFormSchemas() {
        List<FormSchemaJson> allFormSchemas = new ArrayList<>();

        for (CommcareApplicationJson app : applicationDataService.retrieveAll()) {
            for (CommcareModuleJson module : app.getModules()) {
                allFormSchemas.addAll(module.getFormSchemas());
            }
        }

        return allFormSchemas;
    }

}
