package org.motechproject.commcare.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Create")
public class CreateElement {
    private String case_type;
    private String case_name;
    private String owner_id;

    public String getCase_type() {
        return case_type;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    public String getCase_name() {
        return case_name;
    }

    public void setCase_name(String case_name) {
        this.case_name = case_name;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
}
