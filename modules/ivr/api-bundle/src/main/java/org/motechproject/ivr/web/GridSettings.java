package org.motechproject.ivr.web;


import org.motechproject.commons.couchdb.query.QueryParam;
import org.motechproject.ivr.domain.CallRecordSearchParameters;


/**
 * Grid Settings holds search parameters for searching call logs, formatted for easy searching.
 * It covers the same fields as CallRecordSearchParameters.
 */
public class GridSettings {
    private Integer rows;
    private Integer page;
    private String sortColumn;
    private String sortDirection;
    private String phoneNumber;

    private String fromDate;
    private String toDate;

    private Integer minDuration;
    private Integer maxDuration;

    private boolean answered;
    private boolean busy;
    private boolean failed;
    private boolean noAnswer;
    private boolean unknown;

    private boolean inbound;
    private boolean outbound;

    public static final Integer DEFAULT_PAGE_SIZE = 100;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean getBusy() {
        return busy;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean getAnswered() {
        return answered;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public boolean getFailed() {
        return failed;
    }

    public void setNoAnswer(boolean noAnswer) {
        this.noAnswer = noAnswer;
    }

    public boolean getNoAnswer() {
        return noAnswer;
    }

    public void setUnknown(boolean unknown) {
        this.unknown = unknown;
    }

    public boolean getUnknown() {
        return unknown;
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

    public CallRecordSearchParameters toCallRecordSearchParameters() {
        CallRecordSearchParameters params = new CallRecordSearchParameters();
        boolean reverse = "desc".equalsIgnoreCase(sortDirection);
        params.setAnswered(answered);
        params.setBusy(busy);
        params.setFailed(failed);
        params.setNoAnswer(noAnswer);
        params.setUnknown(unknown);
        params.setToDate(toDate);
        params.setFromDate(fromDate);
        params.setMaxDuration(maxDuration);
        params.setMinDuration(minDuration);
        params.setPhoneNumber(phoneNumber);
        params.setInbound(inbound);
        params.setOutbound(outbound);
        if(page == null) {
            page = 1;
        }
        if(rows == null) {
            rows = DEFAULT_PAGE_SIZE;
        }
        params.setQueryParam(new QueryParam(page, rows, sortColumn, reverse));
        return params;
    }

}


