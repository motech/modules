package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;

import java.util.List;

/**
 * Created by kosh on 5/29/14.
 */
@Entity
public class Course {

    private String name;

    private String location;

    private boolean active;

    private List<Module> modules;
}
