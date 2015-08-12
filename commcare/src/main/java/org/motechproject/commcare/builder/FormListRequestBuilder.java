package org.motechproject.commcare.builder;

import org.joda.time.DateTime;
import org.motechproject.commcare.request.FormListRequest;

/**
 * A builder utility for instances of {@link FormListRequest}.
 */
public class FormListRequestBuilder {

    /**
     * Form XML namespace (optional).
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
     * Sets the form XML namespace for the request being built (optional).
     * You can find this for any given form by going to your form -> Advanced -> View (Source xml) and
     * look for the address in the header of your xform. It is also in the raw xml of any submitted form.
     * @param xmlns the xmlns
     * @return this builder instance
     */
    public FormListRequestBuilder withXmlns(String xmlns) {
        this.xmlns = xmlns;
        return this;
    }

    /**
     * Sets the starting boundary for the received on datetime field in the request being built.
     * @param receivedOnStart the starting boundary for 'received on' in the request
     * @return this builder instance
     */
    public FormListRequestBuilder withReceivedOnStart(DateTime receivedOnStart) {
        this.receivedOnStart = receivedOnStart;
        return this;
    }

    /**
     * Sets the end boundary for the received on datetime field in the request being built.
     * @param receivedOnEnd the end boundary for 'received on' in the request
     * @return this builder instance
     */
    public FormListRequestBuilder withReceivedOnEnd(DateTime receivedOnEnd) {
        this.receivedOnEnd = receivedOnEnd;
        return this;
    }

    /**
     * Sets the number of the page to fetch in the request being built.
     * @param pageNumber the page to fetch
     * @return this builder instance
     */
    public FormListRequestBuilder withPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    /**
     * Sets the size of the page to fetch in the request being built.
     * @param pageSize the size of the page
     * @return this builder instance
     */
    public FormListRequestBuilder withPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * Sets the exact version of the CommCare application used to submit the form in the request being built.
     * @param appVersion the app version
     * @return this builder instance
     */
    public FormListRequestBuilder withAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    /**
     * Sets whether to include archived forms in the response.
     * @param includeArchived true to include archived forms, false otherwise
     * @return this builder instance
     */
    public FormListRequestBuilder withArchived(Boolean includeArchived) {
        this.includeArchived = includeArchived;
        return this;
    }

    /**
     * Creates a new instance of {@link FormListRequest} with parameters represented by this builder.
     * @return the form list request
     */
    public FormListRequest build() {
        final FormListRequest request = new FormListRequest();

        request.setXmlns(xmlns);
        request.setReceivedOnStart(receivedOnStart);
        request.setReceivedOnEnd(receivedOnEnd);
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setAppVersion(appVersion);
        request.setIncludeArchived(includeArchived);

        return request;
    }

    /**
     * Increments the current page number. Will set the current page to 1 if it was null.
     * @return this builder instance
     */
    public FormListRequestBuilder withNextPage() {
        if (pageNumber == null) {
            pageNumber = 0;
        }

        pageNumber++;

        return this;
    }

    /**
     * Decrements the current page number.
     * @return this builder instance
     */
    public FormListRequestBuilder withPreviousPage() {
        if (pageNumber == null || pageNumber == 1) {
            throw new IllegalStateException("The page cannot go down below 1");
        }

        pageNumber--;

        return this;
    }
}
