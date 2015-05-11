package org.motechproject.alerts.contract;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.motechproject.commons.api.ClassUtils;
import org.motechproject.commons.api.Range;
import org.motechproject.alerts.domain.Alert;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.enumeration;

/**
 * An enum that represents a search criteria for alerts. The enum has two methods - fetch and filter,
 * that will retrieve or filter the data for a criterion respectively.
 * To be used with {@link org.motechproject.alerts.contract.AlertCriteria}.
 */
public enum Criterion {

    /**
     * A criterion that will return alerts with the matching <b>externalId</b>.
     */
    EXTERNAL_ID {
        @Override
        public List<Alert> fetch(AlertsDataService alertsDataService, AlertCriteria criteria) {
            return alertsDataService.findByExternalId(criteria.externalId());
        }

        @Override
        public List<Alert> filter(List<Alert> alerts, AlertCriteria alertCriteria) {
            return select(alerts, new Equal<>("externalId", alertCriteria.externalId()));
        }

    },

    /**
     * A criterion that will return alerts with matching <b>status</b>.
     */
    ALERT_STATUS {
        @Override
        public List<Alert> fetch(AlertsDataService alertsDataService, AlertCriteria criteria) {
            return alertsDataService.findByStatus(criteria.alertStatus());
        }

        @Override
        public List<Alert> filter(List<Alert> alerts, AlertCriteria alertCriteria) {
            return select(alerts, new Equal<>("status", alertCriteria.alertStatus()));
        }
    },

    /**
     * A criterion that will return alerts with matching <b>type</b>.
     */
    ALERT_TYPE {
        @Override
        public List<Alert> fetch(AlertsDataService alertsDataService, AlertCriteria criteria) {
            return alertsDataService.findByAlertType(criteria.alertType());
        }

        @Override
        public List<Alert> filter(List<Alert> alerts, AlertCriteria alertCriteria) {
            return select(alerts, new Equal<>("alertType", alertCriteria.alertType()));
        }
    },

    /**
     * A criterion that will return alerts with matching <b>priority</b>.
     */
    ALERT_PRIORITY {
        @Override
        public List<Alert> fetch(AlertsDataService alertsDataService, AlertCriteria criteria) {
            return alertsDataService.findByPriority(criteria.alertPriority().longValue());
        }

        @Override
        public List<Alert> filter(List<Alert> alerts, AlertCriteria alertCriteria) {
            return select(alerts, new Equal<>("priority", alertCriteria.alertPriority()));
        }
    },

    /**
     * A criterion that will return alerts with their <b>dateTime</b> within the date range from the criteria.
     */
    DATE_RANGE {
        @Override
        public List<Alert> fetch(AlertsDataService alertsDataService, AlertCriteria criteria) {
            Range<DateTime> range = new Range<>(criteria.fromDate(), criteria.toDate());
            return alertsDataService.findByDateTime(range);
        }

        @Override
        public List<Alert> filter(List<Alert> alerts, AlertCriteria alertCriteria) {
            List<Alert> onOrAfter = select(
                    alerts,
                    new GreaterOrEqual<>("dateTimeInMillis", alertCriteria.fromDate().getMillis())
            );
            return select(
                    onOrAfter,
                    new LessOrEqual<>("dateTimeInMillis", alertCriteria.toDate().getMillis())
            );
        }
    };

    /**
     * Fetches the data for this criterion from the database using the {@link org.motechproject.alerts.contract.AlertsDataService}.
     * @param alertsDataService the service used for fetching the data
     * @param criteria the criteria used for querying the data
     * @return the list of fetched alerts
     */
    public abstract List<Alert> fetch(AlertsDataService alertsDataService, AlertCriteria criteria);

    /**
     * Filters a list of alerts using this criterion. Should create a new list.
     * @param alerts the list of alerts to filter
     * @param alertCriteria the criteria used for filtering
     * @return the matching alerts, a new list
     */
    public abstract List<Alert> filter(List<Alert> alerts, AlertCriteria alertCriteria);

    private static List<Alert> select(List<Alert> alerts, Predicate predicate) {
        Collection collection = CollectionUtils.select(alerts, predicate);
        return ClassUtils.filterByClass(Alert.class, enumeration(collection));
    }
}
