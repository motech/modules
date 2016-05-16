package org.motechproject.http.agent.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * The <code>HTTPActionRecord</code> class represents a record of a http Action.
 */
@Entity
public class HTTPActionRecord {

    @Field
    private String url;

    @Field
    private String request;

    @Field
    private String responseBody;

    @Field
    private String responseCode;

    public HTTPActionRecord(String url, String request, String responseBody, String responseCode) {
        this.url = url;
        this.request = request;
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }

    public void setUrl()  { this.url = url; }

    public void setRequesrt()  { this.request = request; }

    public void setResponseBody()  { this.responseBody = responseBody; }

    public void setResponseCode()  { this.responseCode = responseCode; }

    public String getUrl() {
        return url;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {

        return responseBody;
    }

    public String getRequest() {

        return request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HTTPActionRecord that = (HTTPActionRecord) o;

        return new EqualsBuilder()
                .append(url, that.url)
                .append(request, that.request)
                .append(responseBody, that.responseBody)
                .append(responseCode, that.responseCode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(url)
                .append(request)
                .append(responseBody)
                .append(responseCode)
                .toHashCode();
    }
}

