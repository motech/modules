package org.motechproject.commcare.events;

import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CaseXml;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.impl.CommcareCaseEventParser;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class for convenience when a MotechEvent representing a CaseEvent is received.
 */
public class CaseEvent {

    private String serverModifiedOn;
    private String caseId;
    private String userId;
    private String apiKey;
    private String dateModified;
    private String action;
    private Map<String, String> fieldValues;
    private String caseType;
    private String caseName;
    private String ownerId;
    private String caseDataXmlns;
    private String configName;

    /**
     * Creates an instance of the {@link CaseEvent} class with the given {@code caseId}.
     *
     * @param caseId  the ID of the case to create
     */
    public CaseEvent(String caseId) {
        this.caseId = caseId;
    }

    /**
     * Creates an instance of the {@link CaseEvent} class based on the data passed as the given {@code event}
     * parameters.
     *
     * @param event  the event that stores data used for creating {@link CaseEvent}
     */
    public CaseEvent(MotechEvent event) {
        this.serverModifiedOn = ((String) event.getParameters().get(EventDataKeys.SERVER_MODIFIED_ON));
        this.caseId = ((String) event.getParameters().get(EventDataKeys.CASE_ID));
        this.userId = ((String) event.getParameters().get(EventDataKeys.USER_ID));
        this.apiKey = ((String) event.getParameters().get(EventDataKeys.API_KEY));
        this.dateModified = ((String) event.getParameters().get(EventDataKeys.DATE_MODIFIED));
        this.action = ((String) event.getParameters().get(EventDataKeys.CASE_ACTION));
        this.fieldValues = ((Map<String, String>) event.getParameters().get(
                EventDataKeys.FIELD_VALUES));
        this.caseType = ((String) event.getParameters().get(EventDataKeys.CASE_TYPE));
        this.caseName = ((String) event.getParameters().get(EventDataKeys.CASE_NAME));
        this.ownerId = ((String) event.getParameters().get(EventDataKeys.OWNER_ID));
        this.caseDataXmlns = ((String) event.getParameters().get(EventDataKeys.CASE_DATA_XMLNS));
        this.configName = ((String) event.getParameters().get(EventDataKeys.CONFIG_NAME));
    }

    /**
     * Creates an instance of the {@link MotechEvent} class based on this {@link CaseEvent}. No data but case ID, custom
     * parser event key, case type and configuration name will be included in the event as parameters.
     *
     * @return an instance of the {@link MotechEvent}
     */
    public MotechEvent toMotechEventWithoutData() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.CASE_ID, this.caseId);
        parameters.put(TasksEventParser.CUSTOM_PARSER_EVENT_KEY, CommcareCaseEventParser.PARSER_NAME);
        parameters.put(EventDataKeys.CONFIG_NAME, this.configName);
        parameters.put(EventDataKeys.CASE_TYPE, this.caseType);
        return new MotechEvent(EventSubjects.CASE_EVENT, parameters);
    }

    /**
     * Creates an instance of the {@link MotechEvent} class based on this {@link CaseEvent}. All data stored by this
     * {@link CaseEvent} will be included in the event as parameters.
     *
     * @return an instance of the {@link MotechEvent}
     */
    public MotechEvent toMotechEventWithData() {

        MotechEvent event = toMotechEventWithoutData();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.SERVER_MODIFIED_ON, this.serverModifiedOn);
        parameters.put(EventDataKeys.USER_ID, this.userId);
        parameters.put(EventDataKeys.CASE_ACTION, this.action);
        parameters.put(EventDataKeys.API_KEY, this.apiKey);
        parameters.put(EventDataKeys.CASE_NAME, this.caseName);
        parameters.put(EventDataKeys.CASE_TYPE, this.caseType);
        parameters.put(EventDataKeys.DATE_MODIFIED, this.dateModified);
        parameters.put(EventDataKeys.OWNER_ID, this.ownerId);
        parameters.put(EventDataKeys.CASE_DATA_XMLNS, this.caseDataXmlns);
        event.getParameters().putAll(parameters);

        return event;
    }

    /**
     * Creates an instance of the {@link CaseEvent} class based on the given {@code caseInstance} and
     * {@code configName}.
     *
     * @param caseInstance  the case instance
     * @param configName  the configuration name
     * @return an instance of the {@link CaseEvent}
     */
    public static CaseEvent fromCaseXml(CaseXml caseInstance, String configName) {
        CaseEvent event = new CaseEvent(caseInstance.getCaseId());
        event.setServerModifiedOn(caseInstance.getServerModifiedOn());
        event.setCaseId(caseInstance.getCaseId());
        event.setUserId(caseInstance.getUserId());
        event.setAction(caseInstance.getAction());
        event.setApiKey(caseInstance.getApiKey());
        event.setCaseName(caseInstance.getCaseName());
        event.setCaseType(caseInstance.getCaseType());
        event.setDateModified(caseInstance.getDateModified());
        event.setFieldValues(caseInstance.getFieldValues());
        event.setOwnerId(caseInstance.getOwnerId());
        event.setCaseDataXmlns(caseInstance.getCaseDataXmlns());
        event.setConfigName(configName);
        return event;
    }

    /**
     * Creates an instance of the {@link CaseEvent} class based on the given {@code caseInfo} and
     * {@code configName}.
     *
     * @param caseInfo the case information
     * @param configName the configuration name
     * @return an instance of the {@link CaseEvent}
     */
    public static CaseEvent fromCaseInfo(CaseInfo caseInfo, String configName) {
        CaseEvent event = new CaseEvent(caseInfo.getCaseId());
        event.setServerModifiedOn(caseInfo.getServerDateModified());
        event.setUserId(caseInfo.getUserId());
        event.setCaseName(caseInfo.getCaseName());
        event.setCaseType(caseInfo.getCaseType());
        event.setDateModified(caseInfo.getDateModified());
        event.setFieldValues(caseInfo.getFieldValues());
        event.setOwnerId(caseInfo.getOwnerId());
        event.setConfigName(configName);

        return event;
    }

    public String getServerModifiedOn() {
        return this.serverModifiedOn;
    }

    public void setServerModifiedOn(String serverModifiedOn) {
        this.serverModifiedOn = serverModifiedOn;
    }

    public String getCaseId() {
        return this.caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDateModified() {
        return this.dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, String> getFieldValues() {
        return this.fieldValues;
    }

    public void setFieldValues(Map<String, String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public String getCaseType() {
        return this.caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseName() {
        return this.caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCaseDataXmlns() {
        return caseDataXmlns;
    }

    public void setCaseDataXmlns(String caseDataXmlns) {
        this.caseDataXmlns = caseDataXmlns;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
