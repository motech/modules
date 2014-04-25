package org.motechproject.server.alerts.service;

import org.apache.commons.collections.MapUtils;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.server.alerts.contract.AlertService;
import org.motechproject.server.alerts.contract.UpdateCriteria;
import org.motechproject.server.alerts.domain.AlertStatus;
import org.motechproject.server.alerts.domain.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.motechproject.server.alerts.EventKeys.ALERT_DATA;
import static org.motechproject.server.alerts.EventKeys.ALERT_DESCRIPTION;
import static org.motechproject.server.alerts.EventKeys.ALERT_ID;
import static org.motechproject.server.alerts.EventKeys.ALERT_NAME;
import static org.motechproject.server.alerts.EventKeys.ALERT_PRIORITY;
import static org.motechproject.server.alerts.EventKeys.ALERT_STATUS;
import static org.motechproject.server.alerts.EventKeys.ALERT_TYPE;
import static org.motechproject.server.alerts.EventKeys.CLOSE_ALERT_SUBJECT;
import static org.motechproject.server.alerts.EventKeys.CREATE_ALERT_SUBJECT;
import static org.motechproject.server.alerts.EventKeys.EXTERNAL_ID_KEY;
import static org.motechproject.server.alerts.EventKeys.MARK_ALERT_READ_SUBJECT;

@Service
public class AlertEventHandler {
    private AlertService alertService;

    @MotechListener(subjects = {CREATE_ALERT_SUBJECT})
    public void create(MotechEvent event) {
        String externalId = getValueAsString(event, EXTERNAL_ID_KEY);
        String alertName = getValueAsString(event, ALERT_NAME);
        String alertDescription = getValueAsString(event, ALERT_DESCRIPTION);
        AlertType alertType = getValueAsEnum(AlertType.class, event, ALERT_TYPE);
        AlertStatus alertStatus = getValueAsEnum(AlertStatus.class, event, ALERT_STATUS);
        Integer alertPriority = Integer.valueOf(getValueAsString(event, ALERT_PRIORITY));
        Map<String, String> alertData = (Map<String, String>) getValue(event, ALERT_DATA);

        alertService.create(
                externalId, alertName, alertDescription, alertType, alertStatus, alertPriority,
                alertData
        );
    }

    @MotechListener(subjects = {CLOSE_ALERT_SUBJECT, MARK_ALERT_READ_SUBJECT})
    public void updateStatus(MotechEvent event) {
        Long id = Long.valueOf(getValueAsString(event, ALERT_ID));
        UpdateCriteria criteria = new UpdateCriteria();

        if (event.getSubject().equals(CLOSE_ALERT_SUBJECT)) {
            criteria.status(AlertStatus.CLOSED);
        } else {
            criteria.status(AlertStatus.READ);
        }

        alertService.update(id, criteria);
    }

    private Object getValue(MotechEvent event, String key) {
        Map<String, Object> parameters = event.getParameters();
        Object value = null;

        if (MapUtils.isNotEmpty(parameters)) {
            value = parameters.get(key);
        }

        return value;
    }

    private String getValueAsString(MotechEvent event, String key) {
        Object value = getValue(event, key);
        return null != value ? value.toString() : "";
    }

    private <T extends Enum> T getValueAsEnum(Class<T> enumClass, MotechEvent event, String key) {
        String string = getValueAsString(event, key);
        T result = null;

        if (isNotBlank(string)) {
            for (T status : enumClass.getEnumConstants()) {
                if (status.name().equalsIgnoreCase(string)) {
                    result = status;
                    break;
                }
            }
        }

        return result;
    }

    @Autowired
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }
}
