package org.motechproject.commcare.events;

import org.motechproject.commcare.domain.CaseXml;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class for convenience when a MotechEvent representing a CaseEvent is
 * received.
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

    public CaseEvent(String caseId) {
        this.caseId = caseId;
    }

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

    public MotechEvent toMotechEventWithoutData() {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(EventDataKeys.CASE_ID, this.caseId);
        return new MotechEvent(EventSubjects.CASE_EVENT, parameters);
    }

    public MotechEvent toMotechEventWithData() {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(EventDataKeys.SERVER_MODIFIED_ON, this.serverModifiedOn);
        parameters.put(EventDataKeys.CASE_ID, this.caseId);
        parameters.put(EventDataKeys.USER_ID, this.userId);
        parameters.put(EventDataKeys.CASE_ACTION, this.action);
        parameters.put(EventDataKeys.API_KEY, this.apiKey);
        parameters.put(EventDataKeys.CASE_NAME, this.caseName);
        parameters.put(EventDataKeys.CASE_TYPE, this.caseType);
        parameters.put(EventDataKeys.DATE_MODIFIED, this.dateModified);
        parameters.put(EventDataKeys.OWNER_ID, this.ownerId);
        parameters.put(EventDataKeys.CASE_DATA_XMLNS, this.caseDataXmlns);
        return new MotechEvent(EventSubjects.CASE_EVENT, parameters);
    }

    public CaseEvent eventFromCase(CaseXml caseInstance) {
        setServerModifiedOn(caseInstance.getServerModifiedOn());
        setCaseId(caseInstance.getCaseId());
        setUserId(caseInstance.getUserId());
        setAction(caseInstance.getAction());
        setApiKey(caseInstance.getApiKey());
        setCaseName(caseInstance.getCaseName());
        setCaseType(caseInstance.getCaseType());
        setDateModified(caseInstance.getDateModified());
        setFieldValues(caseInstance.getFieldValues());
        setOwnerId(caseInstance.getOwnerId());
        setCaseDataXmlns(caseInstance.getCaseDataXmlns());

        return this;
    }
}
