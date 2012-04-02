package org.motechproject.commcare.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/1/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("Create")
public class CreateElement {
    private String case_type;
    private String case_name;

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

    private String owner_id;
}
