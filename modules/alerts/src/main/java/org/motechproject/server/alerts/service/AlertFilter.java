package org.motechproject.server.alerts.service;

import org.motechproject.server.alerts.contract.AlertCriteria;
import org.motechproject.server.alerts.contract.AlertsDataService;
import org.motechproject.server.alerts.contract.Criterion;
import org.motechproject.server.alerts.domain.Alert;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class AlertFilter {
    private AlertsDataService alertsDataService;

    public AlertFilter(final AlertsDataService alertsDataService) {
        this.alertsDataService = alertsDataService;
    }

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
