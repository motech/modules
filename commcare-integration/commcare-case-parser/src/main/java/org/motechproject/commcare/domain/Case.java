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
    private String case_id;
    private String date_modified;
    private String action;
    
    private Map<String,String> fieldValues;
    private String case_type_id;
    private String case_name;

    public Case(){
        fieldValues = new HashMap<String, String>();
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getAction() {
        return action;
    }


    public void setAction(String action) {
        this.action = action;
    }

    public void setCase_type_id(String case_type_id) {
        this.case_type_id = case_type_id;
    }

    public void setCase_name(String case_name) {
        this.case_name = case_name;
    }

    public void AddFieldvalue(String nodeName, String nodeValue) {
        fieldValues.put(nodeName,nodeValue);
    }

    public String getCase_id() {
        return case_id;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public String getCase_type_id() {
        return case_type_id;
    }

    public String getCase_name() {
        return case_name;
    }

    public Map<String, String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(HashMap<String, String> fieldValues) {
        this.fieldValues = fieldValues;
    }
}
