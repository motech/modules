package org.motechproject.ivr.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.commons.couchdb.query.QueryParam;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class CallRecordSearchParameters {
    private static final String DEFAULT_DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";
    private String phoneNumber;

    private DateTime fromDate;
    private DateTime toDate;

    private Integer minDuration;
    private Integer maxDuration;


    private boolean answered;
    private boolean busy;
    private boolean failed;
    private boolean noAnswer;
    private boolean unknown;

    private boolean inbound;
    private boolean outbound;

    //Query Param holds page number to return, records per page,
    //what column to sort by, and whether or not to sort in reverse order
    private QueryParam queryParam = new QueryParam();

    public void setQueryParam(QueryParam queryParam) {
        this.queryParam = queryParam;
    }

    public QueryParam getQueryParam() {
        return queryParam;
    }

    public void setInbound(boolean inbound) {
        this.inbound = inbound;
    }

    public boolean isInbound() {
        return inbound;
    }

    public void setOutbound(boolean outbound) {
        this.outbound = outbound;
    }

    public boolean isOutbound() {
        return outbound;
    }

    public String getToDate() {
        return toDate.toString(DEFAULT_DATE_FORMAT);
    }

    public DateTime getToDateAsDateTime() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate(toDate);
    }

    private DateTime toDate(String date) {
        return isNotBlank(date) ? DateTime.parse(date, DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT)) : null;
    }

    public String getFromDate() {
        return fromDate.toString(DEFAULT_DATE_FORMAT);
    }


    public DateTime getFromDateAsDateTime() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = toDate(fromDate);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public boolean getAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean getBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean getFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public boolean getNoAnswer() {
        return noAnswer;
    }

    public void setNoAnswer(boolean noAnswer) {
        this.noAnswer = noAnswer;
    }

    public boolean getUnknown() {
        return unknown;
    }

    public void setUnknown(boolean unknown) {
        this.unknown = unknown;
    }
}
