package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareReportService;
import org.motechproject.commcare.service.ReportActionService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("reportActionService")
public class ReportActionServiceImpl implements ReportActionService {

    private CommcareReportService commcareReportService;
    private EventRelay eventRelay;

    @Autowired
    public ReportActionServiceImpl (CommcareReportService commcareReportService, EventRelay eventRelay) {
        this.commcareReportService = commcareReportService;
        this.eventRelay = eventRelay;
    }

    @Override
    public void queryReport (String configName, String reportId, String reportName, String urlParsedFilters) {

        commcareReportService.getReportByIdWithFilters(reportId, configName, urlParsedFilters);

        eventRelay.sendEventMessage(new MotechEvent(EventSubjects.RECEIVED_REPORT + "." + configName + "." + reportId,
                prepareReceiveReportParameters(reportId, reportName)));
    }

    private Map<String, Object> prepareReceiveReportParameters (String reportId, String reportName) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(EventDataKeys.REPORT_ID, reportId);
        parameters.put(EventDataKeys.REPORT_NAME, reportName);

        return parameters;
    }
}
