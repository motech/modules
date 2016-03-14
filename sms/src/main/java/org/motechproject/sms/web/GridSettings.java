package org.motechproject.sms.web;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.commons.api.Range;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.util.Order;
import org.motechproject.sms.audit.SmsDirection;
import org.motechproject.sms.audit.SmsRecordSearchCriteria;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Models the audit log filter settings UI
 */
public class GridSettings {

    /**
     * The number of rows to display per page.
     */
    private Integer rows;

    /**
     * The page to display, starting from 1.
     */
    private Integer page;

    /**
     * The name of the column used for sorting.
     */
    private String sortColumn;

    /**
     * The direction used for sorting, either asc or desc.
     */
    private String sortDirection;

    /**
     * The name of the SMS configuration for which to retrieve records.
     */
    private String config;

    /**
     * The phone number for which records should get retrieved.
     */
    private String phoneNumber;

    /**
     * The content of the message to search for.
     */
    private String messageContent;

    /**
     * The datetime describing the start of time range from which records should get retrieved.
     */
    private String timeFrom;

    /**
     * The datetime describing the end of time range from which records should get retrieved.
     */
    private String timeTo;

    /**
     * The delivery status to search for.
     */
    private String deliveryStatus;

    /**
     * The provider status to search for.
     */
    private String providerStatus;

    /**
     * The SMS direction to search for, either inbound or outbound.
     */
    private String smsDirection;

    /**
     * The MOTECH ID to search for.
     */
    private String motechId;

    /**
     * The provider ID to search for.
     */
    private String providerId;

    /**
     * @return the number of rows to display per page
     */
    public Integer getRows() {
        return rows;
    }

    /**
     * @param rows the number of rows to display per page
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    /**
     * @return the page to display, starting from 1
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page the page to display, starting from 1
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return the name of the column used for sorting
     */
    public String getSortColumn() {
        return sortColumn;
    }

    /**
     * @param sortColumn the name of the column used for sorting
     */
    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    /**
     * @return the direction used for sorting, either asc or desc
     */
    public String getSortDirection() {
        return sortDirection;
    }

    /**
     * @param sortDirection the direction used for sorting, either asc or desc
     */
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    /**
     * @return the name of the SMS configuration for which to retrieve records
     */
    public String getConfig() {
        return config;
    }

    /**
     * @param config the name of the SMS configuration for which to retrieve records
     */
    public void setConfig(String config) {
        this.config = config;
    }

    /**
     * @return the phone number for which records should get retrieved
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phone number for which records should get retrieved
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the content of the message to search for
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * @param messageContent the content of the message to search for
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * @return the datetime describing the start of time range from which records should get retrieved
     */
    public String getTimeFrom() {
        return timeFrom;
    }

    /**
     * @param timeFrom the datetime describing the start of time range from which records should get retrieved
     */
    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    /**
     * @return the datetime describing the end of time range from which records should get retrieved
     */
    public String getTimeTo() {
        return timeTo;
    }

    /**
     * @param timeTo the datetime describing the end of time range from which records should get retrieved
     */
    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    /**
     * @return the delivery status to search for
     */
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * @param deliveryStatus the delivery status to search for
     */
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    /**
     * @return the provider status to search for
     */
    public String getProviderStatus() {
        return providerStatus;
    }

    /**
     * @param providerStatus the provider status to search for
     */
    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    /**
     * @return the SMS direction to search for, either inbound or outbound
     */
    public String getSmsDirection() {
        return smsDirection;
    }

    /**
     * @param smsDirection the SMS direction to search for, either inbound or outbound
     */
    public void setSmsDirection(String smsDirection) {
        this.smsDirection = smsDirection;
    }

    /**
     * @return the MOTECH ID to search for
     */
    public String getMotechId() {
        return motechId;
    }

    /**
     * @param motechId the MOTECH ID to search for
     */
    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    /**
     * @return the provider ID to search for
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * @param providerId the provider ID to search for
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    /**
     * Converts these grid settings to a {@link SmsRecordSearchCriteria} object, which
     * contains type safe information and should be used for building database lookups.
     * @return the newly created search crtieria
     */
    public SmsRecordSearchCriteria toSmsRecordSearchCriteria() {
        boolean reverse = "desc".equalsIgnoreCase(sortDirection);

        Order order = new Order(sortColumn, (reverse) ? Order.Direction.ASC : Order.Direction.DESC);
        QueryParams queryParam = new QueryParams(page, rows, order);

        Set<SmsDirection> types = getSmsDirectionFromSettings();
        Set<String> deliveryStatusList = getDeliveryStatusFromSettings();
        Range<DateTime> range = createRangeFromSettings();
        SmsRecordSearchCriteria criteria = new SmsRecordSearchCriteria();
        if (!types.isEmpty()) {
            criteria.withSmsDirections(types);
        }
        if (!deliveryStatusList.isEmpty()) {
            criteria.withDeliverystatuses(deliveryStatusList);
        }
        if (StringUtils.isNotBlank(config)) {
            criteria.withConfig(config);
        }
        if (StringUtils.isNotBlank(phoneNumber)) {
            criteria.withPhoneNumber(phoneNumber);
        }
        if (StringUtils.isNotBlank(messageContent)) {
            criteria.withMessageContent(messageContent);
        }
        if (StringUtils.isNotBlank(motechId)) {
            criteria.withMotechId(motechId);
        }
        if (StringUtils.isNotBlank(providerId)) {
            criteria.withProviderId(providerId);
        }
        if (StringUtils.isNotBlank(providerStatus)) {
            criteria.withProviderStatus(providerStatus);
        }
        criteria.withTimestampRange(range);
        criteria.withQueryParams(queryParam);
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

    private Set<String> getDeliveryStatusFromSettings() {
        return new HashSet<>(Arrays.asList(deliveryStatus.split(",")));
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
        return new Range<>(from, to);
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
