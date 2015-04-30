package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.commcare.events.constants.EventDataKeys.API_KEY;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_IDS;
import static org.motechproject.commcare.events.constants.EventDataKeys.CONFIG_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.ELEMENT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.FAILED_FORM_MESSAGE;
import static org.motechproject.commcare.events.constants.EventDataKeys.FORM_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.RECEIVED_ON;
import static org.motechproject.commcare.events.constants.EventDataKeys.SUB_ELEMENTS;
import static org.motechproject.commcare.events.constants.EventDataKeys.VALUE;
import static org.motechproject.commcare.events.constants.EventSubjects.DEVICE_LOG_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_FAIL_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORM_STUB_EVENT;

/**
 * The <code>CommonTriggerBuilder</code> class builds Commcare related task
 * triggers, that do not depend on the current schema or database state.
 */
public class CommonTriggerBuilder implements TriggerBuilder {

    private CommcareConfigService configService;

    public CommonTriggerBuilder(CommcareConfigService configService) {
        this.configService = configService;
    }

    @Override
    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggers = new ArrayList<>();
        triggers.addAll(buildFormStubTrigger());
        triggers.addAll(buildDeviceLogTrigger());
        triggers.addAll(buildFailedMessageTrigger());

        return triggers;
    }

    private List<TriggerEventRequest> buildFailedMessageTrigger() {

        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {

            List<EventParameterRequest> parameterRequests = new ArrayList<>();
            parameterRequests.add(new EventParameterRequest("commcare.message", FAILED_FORM_MESSAGE));
            parameterRequests.add(new EventParameterRequest("commcare.field.configName", CONFIG_NAME));

            triggers.add(new TriggerEventRequest("Forms failed - " + config.getName(),
                    FORMS_FAIL_EVENT + "." + config.getName(), null, parameterRequests, FORMS_FAIL_EVENT));
        }

        return triggers;
    }

    private List<TriggerEventRequest> buildDeviceLogTrigger() {

        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {

            List<EventParameterRequest> parameterRequests = new ArrayList<>();
            parameterRequests.add(new EventParameterRequest("commcare.elementName", ELEMENT_NAME));
            parameterRequests.add(new EventParameterRequest("commcare.subElements", SUB_ELEMENTS, "MAP"));
            parameterRequests.add(new EventParameterRequest("commcare.attributes", API_KEY, "MAP"));
            parameterRequests.add(new EventParameterRequest("commcare.value", VALUE));
            parameterRequests.add(new EventParameterRequest("commcare.field.configName", CONFIG_NAME));

            triggers.add(new TriggerEventRequest("Received Device Log - " + config.getName(),
                    DEVICE_LOG_EVENT + "." + config.getName(), null, parameterRequests, DEVICE_LOG_EVENT));
        }

        return triggers;
    }

    private List<TriggerEventRequest> buildFormStubTrigger() {

        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Config  config : configService.getConfigs().getConfigs()) {
            List<EventParameterRequest> parameterRequests = new ArrayList<>();
            parameterRequests.add(new EventParameterRequest("commcare.receivedOn", RECEIVED_ON));
            parameterRequests.add(new EventParameterRequest("commcare.formId", FORM_ID));
            parameterRequests.add(new EventParameterRequest("commcare.caseIds", CASE_IDS, "LIST"));
            parameterRequests.add(new EventParameterRequest("commcare.field.configName", CONFIG_NAME));

            triggers.add(new TriggerEventRequest("Received Form Stub - " + config.getName(),
                    FORM_STUB_EVENT + "." + config.getName(), null, parameterRequests, FORM_STUB_EVENT));
        }

        return triggers;
    }
}
