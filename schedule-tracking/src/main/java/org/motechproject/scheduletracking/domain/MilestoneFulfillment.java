package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;

import static org.motechproject.commons.date.util.DateUtil.setTimeZone;

@Entity
public class MilestoneFulfillment {

    private String milestoneName;
    private DateTime fulfillmentDateTime;

    public MilestoneFulfillment() {
        this(null, null);
    }

    public MilestoneFulfillment(String milestoneName, DateTime fulfillmentDateTime) {
        this.milestoneName = milestoneName;
        this.fulfillmentDateTime = fulfillmentDateTime;
    }

    public DateTime getFulfillmentDateTime() {
        return setTimeZone(fulfillmentDateTime);
    }

    public void setFulfillmentDateTime(DateTime fulfillmentDateTime) {
        this.fulfillmentDateTime = fulfillmentDateTime;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }
}
