package org.motechproject.commcare.request;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.motechproject.commcare.util.CommcareParamHelper;

import java.io.Serializable;

/**
 * Represents a request for a form list to the Commcare HQ.
 */
public class FormListRequest implements Serializable {

    private static final long serialVersionUID = -3733035486824488516L;

    /**
     * Form XML namespace (optional)
     * You can find this for any given form by going to your form -> Advanced -> View (Source xml) and
     * look for the address in the header of your xform. It is also in the raw xml of any submitted form.
     */
    private String xmlns;

    /**
     * The starting boundary for the received on datetime field.
     */
    private DateTime receivedOnStart;

    /**
     * The end boundary for the received on datetime field.
     */
    private DateTime receivedOnEnd;

    /**
     * The size of the page to fetch.
     */
    private Integer pageSize;

    /**
     * Which page to fetch.
     */
    private Integer pageNumber;

    /**
     * The exact version of the CommCare application used to submit the form.
     */
    private String appVersion;

    /**
     * Whether to include archived forms in the response.
     */
    private Boolean includeArchived;

    /**
     * Returns the form XML namespace for this query. (optional)
     * You can find this for any given form by going to your form -> Advanced -> View (Source xml) and
     * look for the address in the header of your xform. It is also in the raw xml of any submitted form.
     * @return the xmlns
     */
    public String getXmlns() {
        return xmlns;
    }

    /**
     * Sets the form XML namespace for this query. (optional)
     * You can find this for any given form by going to your form -> Advanced -> View (Source xml) and
     * look for the address in the header of your xform. It is also in the raw xml of any submitted form.
     * @param xmlns the xmlns
     */
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    /**
     * @return the starting boundary for the received on datetime field
     */
    public DateTime getReceivedOnStart() {
        return receivedOnStart;
    }

    /**
     * @param receivedOnStart the starting boundary for the received on datetime field
     */
    public void setReceivedOnStart(DateTime receivedOnStart) {
        this.receivedOnStart = receivedOnStart;
    }

    /**
     * @return the end boundary for the received on datetime field
     */
    public DateTime getReceivedOnEnd() {
        return receivedOnEnd;
    }

    /**
     * @param receivedOnEnd the end boundary for the received on datetime field
     */
    public void setReceivedOnEnd(DateTime receivedOnEnd) {
        this.receivedOnEnd = receivedOnEnd;
    }

    /**
     * @return the size of the page to fetch
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the size of the page to fetch
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the number of the page to fetch
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber the number of the page to fetch
     */
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return the exact version of the CommCare application used to submit the form
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion the exact version of the CommCare application used to submit the form
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * @return true if archived forms should be returned, false otherwise
     */
    public Boolean isIncludeArchived() {
        return includeArchived;
    }

    /**
     * @param includeArchived true if archived forms should be returned, false otherwise
     */
    public void setIncludeArchived(Boolean includeArchived) {
        this.includeArchived = includeArchived;
    }

    /**
     * Adds the params for this request to the {@link URIBuilder provided}.
     * Allows to easily add params for this query to a Commcare url.
     * @param uriBuilder the uri builder that should be used
     */
    public void addQueryParams(URIBuilder uriBuilder) {
        if (StringUtils.isNotBlank(xmlns)) {
            uriBuilder.addParameter("xmlns", xmlns);
        }
        if (receivedOnStart != null) {
            uriBuilder.addParameter("received_on_start", CommcareParamHelper.printDateTime(receivedOnStart));
        }
        if (receivedOnEnd != null) {
            uriBuilder.addParameter("received_on_end", CommcareParamHelper.printDateTime(receivedOnEnd));
        }
        if (pageSize != null) {
            uriBuilder.addParameter("limit", String.valueOf(pageSize));
        }
        if (pageNumber != null) {
            uriBuilder.addParameter("offset", String.valueOf(CommcareParamHelper.toOffset(pageSize, pageNumber)));
        }
        if (appVersion != null) {
            uriBuilder.addParameter("appVersion", appVersion);
        }
        if (includeArchived != null) {
            uriBuilder.addParameter("includeArchived", String.valueOf(includeArchived));
        }
    }

    /**
     * Increments the current page number. Will set the current page to 1 if it was null.
     * @return the new page number
     */
    public int nextPage() {
        if (pageNumber == null) {
            pageNumber = 0;
        }

        pageNumber++;

        return pageNumber;
    }

    /**
     * Decrements the current page number.
     * @return the new page number
     */
    public int previousPage() {
        if (pageNumber == null || pageNumber == 1) {
            throw new IllegalStateException("The page cannot go down below 1");
        }

        pageNumber--;

        return pageNumber;
    }
}
