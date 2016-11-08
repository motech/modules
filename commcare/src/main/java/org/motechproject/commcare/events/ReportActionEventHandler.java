package org.motechproject.commcare.events;

import org.joda.time.DateTime;
import org.motechproject.commcare.domain.report.constants.FilterDataType;
import org.motechproject.commcare.domain.report.constants.FilterType;
import org.motechproject.commcare.events.constants.DisplayNames;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.ReportActionService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class serves as the event handler for the task actions, exposed by the Commcare module.
 * Respective methods extract the necessary data from the {@link MotechEvent} instance and
 * handles all the operations on Commcare reports.
 */
/**
 * Class responsible for handling "Report" events. The service will extract the necessary data
 * from the {@link MotechEvent} and pass them to the {@link ReportActionService} service that will
 * query the CommCare Report UCR API and then send a motech event for selected report.
 */
@Component
public class ReportActionEventHandler {

    private static final String TYPE_KEY_IDENTIFIER = ".type";
    private static final String START_DATE_FILTER_SUFFIX = "-start";
    private static final String END_DATE_FILTER_SUFFIX = "-end";
    private static final String NUMERIC_OPERATOR_FILTER_SUFFIX = "-operator";
    private static final String NUMERIC_OPERAND_FILTER_SUFFIX = "-operand";
    private static final String COMMCARE_REPORT_FILTER_DATE_FORMAT = "yyyy-MM-dd";

    private ReportActionService reportActionService;

    @Autowired
    public ReportActionEventHandler (ReportActionService reportActionService) {
        this.reportActionService = reportActionService;
    }

    /**
     * Handles events, connected with getting Commcare report. The event subject should have the following syntax:
     * {@code EventSubjects.REPORT_EVENT.report_name.config_name}
     *
     * @param event the event, containing parameters necessary to get the Commcare report
     */
    @MotechListener(subjects = EventSubjects.REPORT_EVENT + ".*")
    public void receiveReport (MotechEvent event) {
        String configName = EventSubjects.getConfigName(event.getSubject());
        Map<String, Object> parameters = event.getParameters();

        String reportId = (String) parameters.get(EventDataKeys.REPORT_ID);
        String reportName = (String) parameters.get(EventDataKeys.REPORT_NAME);
        List<String> fields = getFieldNamesFromParameters(parameters);

        String parsedFilters = convertFieldsToRequest(fields, parameters);

        reportActionService.queryReport(configName, reportId, reportName, parsedFilters);
    }

    private List<String> getFieldNamesFromParameters (Map<String, Object> parameters) {
        List<String> fieldNames = new ArrayList<>();

        for (String key : parameters.keySet()) {
            if (key.endsWith(TYPE_KEY_IDENTIFIER)) {
                fieldNames.add(key.substring(0, key.indexOf(TYPE_KEY_IDENTIFIER)));
            }
        }

        return fieldNames;
    }

    private String convertFieldsToRequest (List<String> fieldNames, Map<String, Object> parameters) {
        StringBuilder urlFilterBuilder = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldType = (String) parameters.get(fieldName.concat(TYPE_KEY_IDENTIFIER));

            if (fieldType.equals(FilterType.CHOICE_LIST.toString())) {
                String fieldValue = (String) parameters.get(fieldName);
                if (fieldValue != null) {
                    urlFilterBuilder.append("&")
                            .append(fieldName)
                            .append("=")
                            .append(fieldValue);
                }
            } else if (fieldType.equals(FilterType.DATE.toString())) {
                urlFilterBuilder.append(parseDateField(fieldName, parameters));
            } else if (fieldType.equals(FilterDataType.DECIMAL.toString())) {
                urlFilterBuilder.append(parseNumericField(fieldName, parameters));
            }
        }
        return urlFilterBuilder.toString();
    }

    private String parseDateField (String fieldName, Map<String, Object> parameters) {
        StringBuilder parsedDataFilterBuilder = new StringBuilder();

        DateTime startDate = (DateTime) parameters.get(fieldName);
        DateTime endDate = (DateTime) parameters.get(fieldName + "." + DisplayNames.END_DATE);

        if (startDate != null) {
            parsedDataFilterBuilder.append("&")
                    .append(fieldName)
                    .append(START_DATE_FILTER_SUFFIX)
                    .append("=")
                    .append(startDate.toString(COMMCARE_REPORT_FILTER_DATE_FORMAT));
        }
        if (endDate != null) {
            parsedDataFilterBuilder.append("&")
                    .append(fieldName)
                    .append(END_DATE_FILTER_SUFFIX)
                    .append("=")
                    .append(endDate.toString(COMMCARE_REPORT_FILTER_DATE_FORMAT));
        }

        return parsedDataFilterBuilder.toString();
    }

    private String parseNumericField (String fieldName, Map<String, Object> parameters) {
        StringBuilder parsedNumericFilterBuilder = new StringBuilder();

        String operator = (String) parameters.get(fieldName);
        String operand = (String) parameters.get(fieldName + "." + EventDataKeys.VALUE);

        if (operator != null && operand != null) {
            parsedNumericFilterBuilder.append("&")
                    .append(fieldName)
                    .append(NUMERIC_OPERATOR_FILTER_SUFFIX)
                    .append("=")
                    .append(operator)
                    .append("&")
                    .append(fieldName)
                    .append(NUMERIC_OPERAND_FILTER_SUFFIX)
                    .append("=")
                    .append(operand);
        }

        return parsedNumericFilterBuilder.toString();
    }
}
