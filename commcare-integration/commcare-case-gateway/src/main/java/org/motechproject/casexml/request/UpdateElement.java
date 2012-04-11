package org.motechproject.casexml.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("update")
public class UpdateElement {
    private String task_id;
    private String date_eligible;

    public UpdateElement(String taskId, String dateEligible, String dateExpires) {
        this.task_id = taskId;
        this.date_eligible = dateEligible;
        this.date_expires = dateExpires;
    }

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
