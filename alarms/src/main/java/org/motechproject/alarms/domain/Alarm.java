package org.motechproject.alarms.domain;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.alarms.constants.AlarmsConstants.MANAGE_ALARMS;

@Access(value = SecurityMode.PERMISSIONS, members = { MANAGE_ALARMS })
@Entity
public class Alarm {

    @Field
    private Long id;

    @Field(required = true)
    private String name;

    @Field
    private String subject;

    @Field
    private String messageContent;

    @Field
    private List<Recipient> recipients;

    @Field(required = true)
    private Integer schedulePeriod;

    @Field
    private AlarmStatus status;

    public Alarm() {
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public List<Recipient> getRecipients() {
        if (recipients == null) {
            recipients = new ArrayList<>();
        }

        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public Integer getSchedulePeriod() {
        return schedulePeriod;
    }

    public void setSchedulePeriod(Integer schedulePeriod) {
        this.schedulePeriod = schedulePeriod;
    }

    public AlarmStatus getStatus() {
        return status;
    }

    public void setStatus(AlarmStatus status) {
        this.status = status;
    }
}
