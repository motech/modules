package org.motechproject.commcare.request;

public class Index {
    public Index(Pregnancy pregnancy) {
        this.mother_id = pregnancy;
    }

    public Pregnancy getMother_id() {
        return mother_id;
    }

    public void setMother_id(Pregnancy mother_id) {
        this.mother_id = mother_id;
    }

    private  Pregnancy mother_id;
}
