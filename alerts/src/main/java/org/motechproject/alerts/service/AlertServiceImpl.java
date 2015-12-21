package org.motechproject.alerts.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.alerts.contract.AlertCriteria;
import org.motechproject.alerts.contract.AlertService;
import org.motechproject.alerts.contract.AlertsDataService;
import org.motechproject.alerts.contract.UpdateCriteria;
import org.motechproject.alerts.domain.Alert;
import org.motechproject.alerts.domain.AlertStatus;
import org.motechproject.alerts.domain.AlertType;
import org.motechproject.alerts.domain.AlertUpdater;
import org.motechproject.alerts.domain.UpdateCriterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * Creates and maintains alerts.
 */
@Service
public class AlertServiceImpl implements AlertService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertServiceImpl.class);

    private AlertsDataService alertsDataService;
    private AlertFilter alertFilter;

    /**
     * Creates an alert
     *
     * @param entityId    External id for alert
     * @param name        Name of the alert
     * @param description Description of alert
     * @param type        Alert type like critical, high, medium and low
     * @param status      Status of the alert like new, read and closed
     * @param priority    priority of the alert specified by integer values
     * @param data        Extra information of the alert stored as property => value pairs
     */
    @Override
    @Transactional
    public void create(String entityId, String name, String description, AlertType type,
                       AlertStatus status, int priority, Map<String, String> data) {
        alertsDataService.create(
                new Alert(entityId, name, description, type, status, priority, data)
        );
    }


    /**
     * Searches for alerts by the given criteria
     *
     * @param alertCriteria Criteria which is to be used to search for alerts. (Cannot be null)
     * @return List of matched alerts for the given search criteria. If no criterion is added to
     * the alert criteria, it returns all the alerts.
     */
    @Override
    @Transactional
    public List<Alert> search(AlertCriteria alertCriteria) {
        return alertFilter.search(alertCriteria);
    }

    @Override
    @Transactional
    public Alert get(String id) {
        Long idAsLong = StringUtils.isNumeric(id) ? Long.parseLong(id) : null;
        return null == idAsLong ? null : get(idAsLong);
    }

    /**
     * Fetches an alert by id
     *
     * @param id Id of the alert
     * @return Alert object with the given id if found
     */
    @Override
    @Transactional
    public Alert get(Long id) {
        Alert alert = alertsDataService.retrieve("id", id);

        if (alert == null) {
            LOGGER.error("No Alert found for the given id: {}.", id);
        }

        return alert;
    }

    @Override
    @Transactional
    public void update(String alertId, UpdateCriteria updateCriteria) {
        Long idAsLong = StringUtils.isNumeric(alertId) ? Long.parseLong(alertId) : null;

        if (null != idAsLong) {
            update(idAsLong, updateCriteria);
        }
    }

    /**
     * Updates an alert by alert id
     *
     * @param alertId        Id of the alert to be updated
     * @param updateCriteria criteria which specifies the fields to be updated and their new
     *                       values (Cannot be null)
     */
    @Override
    @Transactional
    public void update(Long alertId, UpdateCriteria updateCriteria) {
        Alert alert = get(alertId);
        Map<UpdateCriterion, Object> all = updateCriteria.getAll();
        for (Map.Entry<UpdateCriterion, Object> entry : all.entrySet()) {
            AlertUpdater.get(entry.getKey()).update(alert, entry.getValue());
        }
        alertsDataService.update(alert);
    }

    @Autowired
    public void setAlertsDataService(AlertsDataService alertsDataService) {
        this.alertsDataService = alertsDataService;
        setAlertFilter(new AlertFilter(alertsDataService));
    }

    public void setAlertFilter(AlertFilter alertFilter) {
        this.alertFilter = alertFilter;
    }
}
