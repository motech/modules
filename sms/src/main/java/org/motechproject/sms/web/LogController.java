package org.motechproject.sms.web;

import org.motechproject.sms.audit.SmsLoggingRecords;
import org.motechproject.sms.audit.SmsRecordSearchCriteria;
import org.motechproject.sms.audit.SmsRecords;
import org.motechproject.sms.audit.SmsAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.motechproject.sms.util.Constants.HAS_VIEW_SMS_LOGS_ROLE;

/**
 * Returns a list of sms audit records to the UI (which queried the server @
 * {server}/motech-platform-server/module/sms/log
 */
@Controller
@PreAuthorize(HAS_VIEW_SMS_LOGS_ROLE)
public class LogController {

    @Autowired
    private SmsAuditService smsAuditService;

    /**
     * Retrieves SMS records for the log grid.
     * @param settings the grid settings controlling what and how should get displayed
     * @return the records, retrieved based on the grid settings
     */
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    @ResponseBody
    public SmsLoggingRecords getSmsRecords(GridSettings settings) {
        SmsRecords smsRecords = new SmsRecords();
        SmsRecordSearchCriteria criteria = settings.toSmsRecordSearchCriteria();
        long totalRecords = 0;

        if (!criteria.getSmsDirections().isEmpty() && !criteria.getDeliveryStatuses().isEmpty()) {
            smsRecords = smsAuditService.findAllSmsRecords(criteria);
            totalRecords = smsAuditService.countAllSmsRecords(criteria);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / settings.getRows());

        return new SmsLoggingRecords(settings.getPage(), totalPages, totalRecords, smsRecords);
    }
}
