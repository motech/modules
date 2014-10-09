package org.motechproject.commcare.tasks.builder;

import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.commcare.events.constants.EventDataKeys.API_KEY;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_IDS;
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

    @Override
    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggers = new ArrayList<>();
        triggers.add(buildFormStubTrigger());
        triggers.add(buildDeviceLogTrigger());
        triggers.add(buildFailedMessageTrigger());

        return triggers;
    }

    private TriggerEventRequest buildFailedMessageTrigger() {
        List<EventParameterRequest> parameterRequests = new ArrayList<>();
        parameterRequests.add(new EventParameterRequest("commcare.message", FAILED_FORM_MESSAGE));

        return new TriggerEventRequest("commcare.forms.failed", FORMS_FAIL_EVENT, null, parameterRequests);
    }

    private TriggerEventRequest buildDeviceLogTrigger() {
        List<EventParameterRequest> parameterRequests = new ArrayList<>();
        parameterRequests.add(new EventParameterRequest("commcare.elementName", ELEMENT_NAME));
        parameterRequests.add(new EventParameterRequest("commcare.subElements", SUB_ELEMENTS, "MAP"));
        parameterRequests.add(new EventParameterRequest("commcare.attributes", API_KEY, "MAP"));
        parameterRequests.add(new EventParameterRequest("commcare.value", VALUE));

        return new TriggerEventRequest("commcare.devicelog", DEVICE_LOG_EVENT, null, parameterRequests);
    }

    private TriggerEventRequest buildFormStubTrigger() {
        List<EventParameterRequest> parameterRequests = new ArrayList<>();
        parameterRequests.add(new EventParameterRequest("commcare.receivedOn", RECEIVED_ON));
        parameterRequests.add(new EventParameterRequest("commcare.formId", FORM_ID));
        parameterRequests.add(new EventParameterRequest("commcare.caseIds", CASE_IDS, "LIST"));

        return new TriggerEventRequest("commcare.formstub", FORM_STUB_EVENT, null, parameterRequests);
    }
}
