package org.motechproject.alerts.web;

import org.motechproject.alerts.contract.AlertsDataService;
import org.motechproject.alerts.domain.AlertStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class AlertsController {

    private AlertsDataService alertsDataService;

    @Autowired
    public AlertsController(AlertsDataService alertsDataService) {
        this.alertsDataService = alertsDataService;
    }

    /**
     * returns the number of new alerts
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/newAlertCount")
    public String newAlerts() {
        return Long.toString(alertsDataService.countFindByStatus(AlertStatus.NEW));
    }

    /**
     * returns the number of read alerts
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/readAlertCount")
    public String readAlerts() {
        return Long.toString(alertsDataService.countFindByStatus(AlertStatus.READ));
    }

    /**
     * returns the number of closed alerts
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/closedAlertCount")
    public String closedAlerts() {
        return Long.toString(alertsDataService.countFindByStatus(AlertStatus.CLOSED));
    }

    /**
     * returns the number of new or read alerts
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/newOrReadAlertCount")
    public String newOrReadAlerts() {
        return Long.toString(
                alertsDataService.countFindByStatus(AlertStatus.NEW)
                +
                alertsDataService.countFindByStatus(AlertStatus.READ)
        );
    }
}
