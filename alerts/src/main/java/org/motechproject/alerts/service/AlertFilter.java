package org.motechproject.alerts.service;

import org.motechproject.alerts.contract.AlertCriteria;
import org.motechproject.alerts.contract.AlertsDataService;
import org.motechproject.alerts.contract.Criterion;
import org.motechproject.alerts.domain.Alert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * This class is responsible for taking an {@link org.motechproject.alerts.contract.AlertCriteria}
 * object and returning the list of matching alerts.
 */
public class AlertFilter {
    private AlertsDataService alertsDataService;

    /**
     * Constructs an instance using the provided data service. The data service will be used for retrieving alerts.
     * @param alertsDataService the data service for alerts
     */
    public AlertFilter(final AlertsDataService alertsDataService) {
        this.alertsDataService = alertsDataService;
    }

    /**
     * Retrieves the list of alerts matching the provided criteria.
     * @param alertCriteria the criteria for alert retrieval
     * @return the list of matching alerts
     */
    public List<Alert> search(AlertCriteria alertCriteria) {
        List<Criterion> filters = alertCriteria.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            return alertsDataService.retrieveAll();
        }

        Criterion primaryCriterion = filters.get(0);
        List<Alert> filtered = primaryCriterion.fetch(alertsDataService, alertCriteria);
        for (Criterion secondaryCriterion : filters.subList(1, filters.size())) {
            filtered = secondaryCriterion.filter(filtered, alertCriteria);
        }

        return filtered;
    }
}
