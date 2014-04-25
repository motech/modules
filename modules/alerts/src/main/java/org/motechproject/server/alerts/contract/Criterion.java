package org.motechproject.server.alerts.contract;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.motechproject.commons.api.CastUtils;
import org.motechproject.commons.api.Range;
import org.motechproject.server.alerts.domain.Alert;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.enumeration;

public enum Criterion {
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

    public abstract List<Alert> fetch(AlertsDataService alertsDataService, AlertCriteria criteria);

    public abstract List<Alert> filter(List<Alert> alerts, AlertCriteria alertCriteria);

    private static List<Alert> select(List<Alert> alerts, Predicate predicate) {
        Collection collection = CollectionUtils.select(alerts, predicate);
        return CastUtils.cast(Alert.class, enumeration(collection));
    }

}


