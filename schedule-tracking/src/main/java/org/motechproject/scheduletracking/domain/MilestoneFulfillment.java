package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

import static org.motechproject.commons.date.util.DateUtil.setTimeZone;

/**
 * Represent details about milestone fulfillment.
 */
@Entity
@CrudEvents(CrudEventType.NONE)
public class MilestoneFulfillment {

    /**
     * The name of the milestone.
     */
    @Field
    private String milestoneName;

    /**
     * The date and time of the fulfillment.
     */
    @Field
    private DateTime fulfillmentDateTime;

    /**
     * Creates a MilestoneFulfillment.
     */
    public MilestoneFulfillment() {
        this(null, null);
    }

    /**
     * Creates a MilestoneFulfillment with the milestoneName attribute set to {@code milestoneName}, the fulfillmentDateTime
     * attribute set to {@code fulfillmentDateTime}.
     *
     * @param milestoneName the name of the fulfilled milestone
     * @param fulfillmentDateTime the date and time of the fulfillment
     */
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
