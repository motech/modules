package org.motechproject.sms.audit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.mds.query.QueryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static org.motechproject.commons.api.MotechEnumUtils.toEnumSet;

/**
 * Reading and writing to the SMS audit log
 */
@Service
public class SmsAuditServiceImpl implements SmsAuditService {
    private SmsRecordsDataService smsRecordsDataService;
    private Logger logger = LoggerFactory.getLogger(SmsAuditServiceImpl.class);

    @Autowired
    public SmsAuditServiceImpl(SmsRecordsDataService smsRecordsDataService) {
        this.smsRecordsDataService = smsRecordsDataService;
    }

    @Override
    public void log(SmsRecord smsRecord) {
        logger.info(smsRecord.toString());
        smsRecordsDataService.create(smsRecord);
    }

    @Override
    public List<SmsRecord> findAllSmsRecords() {
        return smsRecordsDataService.retrieveAll();
    }

    @Override
    public SmsRecords findAllSmsRecords(SmsRecordSearchCriteria criteria) {
        List<SmsRecord> recordList = (List<SmsRecord>) executeQuery(criteria, false);
        return new SmsRecords(recordList.size(), recordList);
    }

    @Override
    public long countAllSmsRecords(SmsRecordSearchCriteria criteria) {
        return (long) executeQuery(criteria, true);
    }

    private Object executeQuery(SmsRecordSearchCriteria criteria, boolean count) {
        Set<String> directions = criteria.getSmsDirections();
        Set<SmsDirection> directionsEnum = toEnumSet(SmsDirection.class, directions);

        Set<String> statuses = criteria.getDeliveryStatuses();
        Set<DeliveryStatus> statusesEnum = toEnumSet(DeliveryStatus.class, statuses);

        Range<DateTime> timestampRange = criteria.getTimestampRange();

        String config = asQuery(criteria.getConfig());
        String phoneNumber = asQuery(criteria.getPhoneNumber());
        String messageContent = asQuery(criteria.getMessageContent());
        String providerStatus = asQuery(criteria.getProviderStatus());
        String motechId = asQuery(criteria.getMotechId());
        String providerId = asQuery(criteria.getProviderId());
        String errorMessage = asQuery(criteria.getErrorMessage());

        QueryParams queryParams = criteria.getQueryParams();

        if (count) {
            return smsRecordsDataService.countFindByCriteria(config, directionsEnum, phoneNumber, messageContent,
                    timestampRange, statusesEnum, providerStatus, motechId, providerId, errorMessage);
        } else {
            return smsRecordsDataService.findByCriteria(
                    config, directionsEnum, phoneNumber, messageContent, timestampRange, statusesEnum,
                    providerStatus, motechId, providerId, errorMessage, queryParams);
        }
    }

    private String asQuery(String value) {
        return StringUtils.isNotBlank(value) ? String.format(".*%s.*", value) : value;
    }
}
