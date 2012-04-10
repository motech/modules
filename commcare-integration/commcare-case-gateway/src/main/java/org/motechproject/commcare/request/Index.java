package org.motechproject.commcare.request;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/1/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Index {
    public Index(Pregnancy pregnancy) {
        this.pregnancy_id = pregnancy;
    }

    public Pregnancy getPregnancy_id() {
        return pregnancy_id;
    }

    public void setPregnancy_id(Pregnancy pregnancy_id) {
        this.pregnancy_id = pregnancy_id;
    }

    private  Pregnancy pregnancy_id;
}
