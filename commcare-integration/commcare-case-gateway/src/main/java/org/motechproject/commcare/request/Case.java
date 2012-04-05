package org.motechproject.commcare.request;

public class Case {
    private CreateElement create;

    private UpdateElement update;
    private Index index;
    private String user_id;
    private String xmlns;

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public Case() {
        this.xmlns="http://commcarehq.org/case/transaction/v2";
    }

    public String getDate_modified() {
        return date_modified;
    }

    private String date_modified;

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    private String case_id;


    public CreateElement getCreate() {
        return create;
    }

    public void setCreate(CreateElement create) {
        this.create = create;
    }

    public UpdateElement getUpdate() {
        return update;
    }

    public void setUpdate(UpdateElement update) {
        this.update = update;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public void setUser_id(String userId) {
        this.user_id =  userId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setDate_Modified(String s) {
        this.date_modified = s;
    }
}

