package org.motechproject.commcare.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 3/22/12
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Case {
    private String caseId;
    private String dateModified;
    private String action;
    
    private Map<String,String> fieldValues;
    private String caseTypeId;
    private String caseName;

    public Case(){
        fieldValues = new HashMap<String, String>();
    }

    public void setCaseId(String case_id) {
        this.caseId = case_id;
    }

    public void setDateModified(String date_modified) {
        this.dateModified = date_modified;
    }

    public String getAction() {
        return action;
    }


    public void setAction(String action) {
        this.action = action;
    }

    public void setCaseTypeId(String caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public void setCaseName(String case_name) {
        this.caseName = case_name;
    }

    public void AddFieldvalue(String nodeName, String nodeValue) {
        fieldValues.put(nodeName,nodeValue);
    }

    public String getCaseId() {
        return caseId;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getCaseTypeId() {
        return caseTypeId;
    }

    public String getCaseName() {
        return caseName;
    }

    public Map<String, String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(HashMap<String, String> fieldValues) {
        this.fieldValues = fieldValues;
    }
}
