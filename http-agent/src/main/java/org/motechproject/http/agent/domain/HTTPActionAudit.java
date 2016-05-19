package org.motechproject.http.agent.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Objects;

/**
 * The <code>HTTPActionAudit</code> class represents a record of a http Action.
 */
@Entity
public class HTTPActionAudit {

    @Field
    private String url;

    @Field
    private String request;

    @Field
    private String responseBody;

    @Field
    private String responseCode;

    public HTTPActionAudit(String url, String request, String responseBody, String responseCode) {
        this.url = url;
        this.request = request;
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }

    public void setUrl(String url)  {
        this.url = url;
    }

    public void setRequest(String request)  {
        this.request = request;
    }

    public void setResponseBody(String responseBody)  {
        this.responseBody = responseBody;
    }

    public void setResponseCode(String responseCode)  {
        this.responseCode = responseCode;
    }

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
        HTTPActionAudit that = (HTTPActionAudit) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(request, that.request) &&
                Objects.equals(responseBody, that.responseBody) &&
                Objects.equals(responseCode, that.responseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, request, responseBody, responseCode);
    }
}

