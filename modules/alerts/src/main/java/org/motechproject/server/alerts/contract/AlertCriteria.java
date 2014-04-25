package org.motechproject.server.alerts.contract;

import org.joda.time.DateTime;
import org.motechproject.server.alerts.domain.AlertStatus;
import org.motechproject.server.alerts.domain.AlertType;

import java.util.ArrayList;
import java.util.List;

/**
 * \ingroup alerts
 * Maintains the criteria by which search for alerts to be done
 */
public class AlertCriteria {
    private String externalId;
    private AlertStatus alertStatus;
    private AlertType alertType;
    private DateTime fromDate;
    private DateTime toDate;
    private Integer alertPriority;

    private List<Criterion> orderedFilters;

    /**
     * Instantiates alert criteria
     */
    public AlertCriteria() {
        orderedFilters = new ArrayList<Criterion>();
    }

    /**
     * Gets all the search criteria that have been added so far
     *
     * @return List of search criteria
     */
    public List<Criterion> getFilters() {
        return orderedFilters;
    }

    /**
     * Adds criterion to search by external id of alert
     *
     * @param id external id of the alert to be searched for
     * @return Instance with the current criterion added to it.
     */
    public AlertCriteria byExternalId(String id) {
        this.externalId = id;
        this.orderedFilters.add(Criterion.EXTERNAL_ID);
        return this;
    }

    public String externalId() {
        return this.externalId;
    }

    /**
     * Adds criterion to search by status of alert
     *
     * @param status status of the alert to be searched for
     * @return Instance with the current criterion added to it.
     */
    public AlertCriteria byStatus(AlertStatus status) {
        this.alertStatus = status;
        this.orderedFilters.add(Criterion.ALERT_STATUS);
        return this;
    }

    public AlertStatus alertStatus() {
        return this.alertStatus;
    }

    /**
     * Adds criterion to search by type of alert
     *
     * @param type type of the alert to be searched for
     * @return Instance with the current criterion added to it.
     */
    public AlertCriteria byType(AlertType type) {
        this.alertType = type;
        this.orderedFilters.add(Criterion.ALERT_TYPE);
        return this;
    }

    public AlertType alertType() {
        return this.alertType;
    }

    /**
     * Adds criterion to search by priority of alert
     *
     * @param priority priority of the alert to be searched for
     * @return Instance with the current criterion added to it.
     */
    public AlertCriteria byPriority(Integer priority) {
        this.alertPriority = priority;
        this.orderedFilters.add(Criterion.ALERT_PRIORITY);
        return this;
    }

    public Integer alertPriority() {
        return this.alertPriority;
    }

    /**
     * Adds criterion to search by the date of alert in the given range
     *
     * @param from Start date of the alerts to be searched for
     * @param to   End date of the alerts to be searched for
     * @return Instance with the current criterion added to it.
     */
    public AlertCriteria byDateRange(DateTime from, DateTime to) {
        this.fromDate = from;
        this.toDate = to;
        this.orderedFilters.add(Criterion.DATE_RANGE);
        return this;
    }

    public DateTime fromDate() {
        return fromDate;
    }

    public DateTime toDate() {
        return toDate;
    }
}
