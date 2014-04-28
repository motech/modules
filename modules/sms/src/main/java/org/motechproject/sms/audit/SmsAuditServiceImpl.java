package org.motechproject.sms.audit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public List<SmsRecord> findAllSmsRecords() {
        return smsRecordsDataService.retrieveAll();
    }

    @Override
    public SmsRecords findAllSmsRecords(SmsRecordSearchCriteria criteria) {
        Set<String> directions = criteria.getSmsDirections();
        Set<SmsDirection> directionsEnum = asEnumList(SmsDirection.class, directions);

        Set<String> statuses = criteria.getDeliveryStatuses();
        Set<DeliveryStatus> statusesEnum = asEnumList(DeliveryStatus.class, statuses);

        Range<DateTime> timestampRange = criteria.getTimestampRange();

        String config = criteria.getConfig();
        String phoneNumber = criteria.getPhoneNumber();
        String messageContent = criteria.getMessageContent();
        String providerStatus = criteria.getProviderStatus();
        String motechId = criteria.getMotechId();
        String providerId = criteria.getProviderId();
        String errorMessage = criteria.getErrorMessage();

        List<SmsRecord> records = smsRecordsDataService.findByCriteria(
                config, directionsEnum, phoneNumber, messageContent, timestampRange, statusesEnum,
                providerStatus, motechId, providerId, errorMessage
        );

        return new SmsRecords(records.size(), records);
    }

    private <T extends Enum> Set<T> asEnumList(Class<T> enumClass, Set<String> strings) {
        Set<T> set = new HashSet<>();

        for (String string : strings) {
            set.add(asEnum(enumClass, string));
        }

        return set;
    }

    private <T extends Enum> T asEnum(Class<T> enumClass, String string) {
        T result = null;

        if (StringUtils.isNotBlank(string)) {
            for (T status : enumClass.getEnumConstants()) {
                if (status.name().equalsIgnoreCase(string)) {
                    result = status;
                    break;
                }
            }
        }

        return result;
    }
}
