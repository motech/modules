package org.motechproject.commcare.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/1/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("Update")
public class UpdateElement {
    private String task_id;
    private String date_eligible;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getDate_eligible() {
        return date_eligible;
    }

    public void setDate_eligible(String date_eligible) {
        this.date_eligible = date_eligible;
    }

    public String getDate_expires() {
        return date_expires;
    }

    public void setDate_expires(String date_expires) {
        this.date_expires = date_expires;
    }

    private String date_expires;
}
