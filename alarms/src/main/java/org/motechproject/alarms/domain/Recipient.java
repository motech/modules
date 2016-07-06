package org.motechproject.alarms.domain;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;

import static org.motechproject.alarms.constants.AlarmsConstants.MANAGE_ALARMS;

@Access(value = SecurityMode.PERMISSIONS, members = { MANAGE_ALARMS })
@Entity
public class Recipient {

    @Field
    private Long id;

    @Field
    private String name;

    @Unique
    @Field(required = true)
    private String emailAddress;

    public Recipient() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
