package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.events.constants.DisplayNames;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;
import org.motechproject.tasks.domain.enums.ParameterType;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.commcare.events.constants.EventDataKeys.API_KEY;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_IDS;
import static org.motechproject.commcare.events.constants.EventDataKeys.CONFIG_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.ELEMENT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.FAILED_FORM_MESSAGE;
import static org.motechproject.commcare.events.constants.EventDataKeys.FORM_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.PRODUCT_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.PRODUCT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.QUANTITY;
import static org.motechproject.commcare.events.constants.EventDataKeys.RECEIVED_ON;
import static org.motechproject.commcare.events.constants.EventDataKeys.SECTION_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.STOCK_ON_HAND;
import static org.motechproject.commcare.events.constants.EventDataKeys.SUB_ELEMENTS;
import static org.motechproject.commcare.events.constants.EventDataKeys.TRANSACTION_DATE;
import static org.motechproject.commcare.events.constants.EventDataKeys.TYPE;
import static org.motechproject.commcare.events.constants.EventDataKeys.VALUE;
import static org.motechproject.commcare.events.constants.EventSubjects.DEVICE_LOG_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_FAIL_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORM_STUB_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.RECEIVED_STOCK_TRANSACTION;

/**
 * The <code>CommonTriggerBuilder</code> class builds Commcare related task triggers, that do not depend on the current
 * schema or database state.
 */
public class CommonTriggerBuilder implements TriggerBuilder {

    private static final String CONFIG_NAME_KEY = "commcare.field.configName";

    private CommcareConfigService configService;

    /**
     * Creates an instance of the {@link CommonTriggerBuilder} class, that is used for creating common triggers. It will
     * use the given {@code configService} for building common triggers.
     *
     * @param configService  the configuration service
     */
    public CommonTriggerBuilder(CommcareConfigService configService) {
        this.configService = configService;
    }

    @Override
    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggers = new ArrayList<>();
        triggers.addAll(buildFormStubTrigger());
        triggers.addAll(buildDeviceLogTrigger());
        triggers.addAll(buildFailedMessageTrigger());
        triggers.addAll(buildStockTransactionTriggers());

        return triggers;
    }

    private List<TriggerEventRequest> buildStockTransactionTriggers() {

        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {

            List<EventParameterRequest> parameterRequests = new ArrayList<>();
            parameterRequests.add(new EventParameterRequest(DisplayNames.PRODUCT_ID, PRODUCT_ID));
            parameterRequests.add(new EventParameterRequest(DisplayNames.PRODUCT_NAME, PRODUCT_NAME));
            parameterRequests.add(new EventParameterRequest(DisplayNames.QUANTITY, QUANTITY));
            parameterRequests.add(new EventParameterRequest(DisplayNames.SECTION_ID, SECTION_ID));
            parameterRequests.add(new EventParameterRequest(DisplayNames.STOCK_ON_HAND, STOCK_ON_HAND));
            parameterRequests.add(new EventParameterRequest(DisplayNames.TRANSACTION_DATE, TRANSACTION_DATE, ParameterType.DATE.getValue()));
            parameterRequests.add(new EventParameterRequest(DisplayNames.TYPE, TYPE));

            String displayName = DisplayNameHelper.buildDisplayName(DisplayNames.RETRIEVED_STOCK_TRANSACTION,
                    config.getName());

            triggers.add(new TriggerEventRequest(displayName, RECEIVED_STOCK_TRANSACTION + "." + config.getName(), null,
                    parameterRequests));
        }

        return triggers;
    }

    private List<TriggerEventRequest> buildFailedMessageTrigger() {

        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {

            List<EventParameterRequest> parameterRequests = new ArrayList<>();
            parameterRequests.add(new EventParameterRequest("commcare.message", FAILED_FORM_MESSAGE));
            parameterRequests.add(new EventParameterRequest(CONFIG_NAME_KEY, CONFIG_NAME));

            String displayName = DisplayNameHelper.buildDisplayName("Forms failed", config.getName());

            triggers.add(new TriggerEventRequest(displayName, FORMS_FAIL_EVENT + "." + config.getName(), null,
                    parameterRequests, FORMS_FAIL_EVENT));
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
            parameterRequests.add(new EventParameterRequest(CONFIG_NAME_KEY, CONFIG_NAME));

            String displayName = DisplayNameHelper.buildDisplayName("Received Device Log", config.getName());

            triggers.add(new TriggerEventRequest(displayName, DEVICE_LOG_EVENT + "." + config.getName(), null,
                    parameterRequests, DEVICE_LOG_EVENT));
        }

        return triggers;
    }

    private List<TriggerEventRequest> buildFormStubTrigger() {

        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Config  config : configService.getConfigs().getConfigs()) {
            List<EventParameterRequest> parameterRequests = new ArrayList<>();
            parameterRequests.add(new EventParameterRequest("commcare.receivedOn", RECEIVED_ON, ParameterType.DATE.getValue()));
            parameterRequests.add(new EventParameterRequest("commcare.formId", FORM_ID));
            parameterRequests.add(new EventParameterRequest("commcare.caseIds", CASE_IDS, "LIST"));
            parameterRequests.add(new EventParameterRequest(CONFIG_NAME_KEY, CONFIG_NAME));

            String displayName = DisplayNameHelper.buildDisplayName("Received Form Stub", config.getName());

            triggers.add(new TriggerEventRequest(displayName, FORM_STUB_EVENT + "." + config.getName(), null,
                    parameterRequests, FORM_STUB_EVENT));
        }

        return triggers;
    }
}
