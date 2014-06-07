package org.motechproject.sms.web;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.commons.api.Range;
import org.motechproject.commons.couchdb.query.QueryParam;
import org.motechproject.sms.audit.DeliveryStatus;
import org.motechproject.sms.audit.SmsRecordSearchCriteria;
import org.motechproject.sms.audit.SmsDirection;

import java.util.HashSet;
import java.util.Set;

/**
 * Models the audit log filter settings UI
 */
public class GridSettings {

    private Integer rows;
    private Integer page;
    private String sortColumn;
    private String sortDirection;
    private String config;
    private String phoneNumber;
    private String messageContent;
    private String timeFrom;
    private String timeTo;
    private String deliveryStatus;
    private String providerStatus;
    private String smsDirection;
    private String motechId;
    private String providerId;

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    public String getSmsDirection() {
        return smsDirection;
    }

    public void setSmsDirection(String smsDirection) {
        this.smsDirection = smsDirection;
    }

    public String getMotechId() {
        return motechId;
    }

    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public SmsRecordSearchCriteria toSmsRecordSearchCriteria() {
        boolean reverse = "desc".equalsIgnoreCase(sortDirection);
        QueryParam queryParam = new QueryParam(page - 1, rows, sortColumn, reverse);
        Set<SmsDirection> types = getSmsDirectionFromSettings();
        Set<DeliveryStatus> deliveryStatusList = getDeliveryStatusFromSettings();
        Range<DateTime> range = createRangeFromSettings();
        SmsRecordSearchCriteria criteria = new SmsRecordSearchCriteria();
        if (!types.isEmpty()) {
            criteria.withSmsDirections(types);
        }
        if (!deliveryStatusList.isEmpty()) {
            criteria.withDeliverystatuses(deliveryStatusList);
        }
        if (StringUtils.isNotBlank(config)) {
            criteria.withConfig(config + "*");
        }
        if (StringUtils.isNotBlank(phoneNumber)) {
            criteria.withPhoneNumber(phoneNumber + "*");
        }
        if (StringUtils.isNotBlank(messageContent)) {
            criteria.withMessageContent(messageContent + "*");
        }
        if (StringUtils.isNotBlank(motechId)) {
            criteria.withMotechId(motechId + "*");
        }
        if (StringUtils.isNotBlank(providerId)) {
            criteria.withProviderId(providerId + "*");
        }
        if (StringUtils.isNotBlank(providerStatus)) {
            criteria.withProviderStatus(providerStatus + "*");
        }
        criteria.withTimestampRange(range);
        criteria.withQueryParam(queryParam);
        return criteria;
    }

    private Set<SmsDirection> getSmsDirectionFromSettings() {
        Set<SmsDirection> smsDirections = new HashSet<>();
        String[] smsDirectionList = smsDirection.split(",");
        for (String type : smsDirectionList) {
            if (!type.isEmpty()) {
                smsDirections.add(SmsDirection.valueOf(type));
            }
        }
        return smsDirections;
    }

    private Set<DeliveryStatus> getDeliveryStatusFromSettings() {
        Set<DeliveryStatus> statusList = new HashSet<>();
        String[] statuses = deliveryStatus.split(",");
        for (String status : statuses) {
            if (!status.isEmpty()) {
                statusList.add(DeliveryStatus.valueOf(status));
            }
        }
        return statusList;
    }

    private Range<DateTime> createRangeFromSettings() {
        DateTime from;
        DateTime to;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(timeFrom)) {
            from = formatter.parseDateTime(timeFrom);
        } else {
            from = new DateTime(0);
        }
        if (StringUtils.isNotBlank(timeTo)) {
            to = formatter.parseDateTime(timeTo);
        } else {
            to = DateTime.now();
        }
        return new Range<DateTime>(from, to);
    }

    @Override
    public String toString() {
        return "GridSettings{" +
                "rows=" + rows +
                ", page=" + page +
                ", sortColumn='" + sortColumn + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                ", config='" + config + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", timeFrom='" + timeFrom + '\'' +
                ", timeTo='" + timeTo + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", providerStatus='" + providerStatus + '\'' +
                ", smsDirection='" + smsDirection + '\'' +
                ", motechId='" + motechId + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
