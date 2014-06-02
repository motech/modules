package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;

import java.util.List;

/**
 * Created by kosh on 5/29/14.
 */
@Entity
public class Course extends BaseMeta {

    private int version;

    private String location;

    private List<Module> modules;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
