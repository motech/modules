package org.motechproject.sms.templates;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * How to generate the http request for a specific provider
 */
public class Request {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(\\[[^\\]]+\\])");

    /**
     * The url path for the endpoint under which the provider receives outgoing SMS
     * messages. Can contain placeholders denoted by [propertyName].
     */
    private String urlPath;

    /**
     * The separator used to separate recipients for the SMS.
     */
    private String recipientsSeparator;

    /**
     * The query parameters to be included in the url.
     */
    private Map<String, String> queryParameters = new HashMap<>();

    /**
     * The parameters to be included in the body of the request.
     */
    private Map<String, String> bodyParameters = new HashMap<>();

    /**
     * The type of the HTTP method used.
     */
    private HttpMethodType type;

    /**
     * Properties used for building the current processed url path.
     */
    private Map<String, String> props;

    /**
     * This path is build by replacing placeholders in urlPath (denoted by []) using
     * the props map.
     */
    private String processedUrlPath;

    /**
     * True if the provider expected a json content type.
     */
    private Boolean jsonContentType = false;

    /**
     * Parses the current url path using the provided props. The result is stored
     * in the processedUrlPath property. Parsing will only take place if the props
     * are different from the previously used ones.
     * @param props the props used for replacing placeholders in the url path
     * @return the processed url path
     */
    public String getUrlPath(Map<String, String> props) {
        if (!props.equals(this.props)) {
            StringBuffer sb = new StringBuffer();
            Matcher m = PLACEHOLDER_PATTERN.matcher(urlPath);

            while (m.find()) {
                String repString = props.get(m.group(1).substring(1, m.group(1).length() - 1));
                if (repString != null) {
                    m.appendReplacement(sb, repString);
                }
            }
            m.appendTail(sb);
            processedUrlPath = sb.toString();
            this.props = props;
        }
        return processedUrlPath;
    }

    /**
     * Sets the url path for the endpoint under which the provider receives outgoing SMS
     * messages. Can contain placeholders denoted by [propertyName].
     * @param urlPath the url path to the provider outgoing sms endpoint
     */
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    /**
     *
     * @return the separator used to separate recipients for the SMS
     */
    public String getRecipientsSeparator() {
        return recipientsSeparator;
    }

    /**
     * @param recipientsSeparator the separator used to separate recipients for the SMS
     */
    public void setRecipientsSeparator(String recipientsSeparator) {
        this.recipientsSeparator = recipientsSeparator;
    }

    /**
     * @return the query parameters to be included in the url
     */
    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    /**
     * @param queryParameters the query parameters to be included in the url, null will be ingored
     */
    public void setQueryParameters(Map<String, String> queryParameters) {
        if (queryParameters != null) {
            this.queryParameters = queryParameters;
        }
    }

    /**
     * @return the type of the HTTP method used
     */
    public HttpMethodType getType() {
        return type;
    }

    /**
     * @param type the type of the HTTP method used
     */
    public void setType(HttpMethodType type) {
        this.type = type;
    }

    /**
     * @return the parameters to be included in the body of the request
     */
    public Map<String, String> getBodyParameters() {
        return bodyParameters;
    }

    /**
     * @param bodyParameters the parameters to be included in the body of the request
     */
    public void setBodyParameters(Map<String, String> bodyParameters) {
        this.bodyParameters = bodyParameters;
    }

    /**
     * @return true if the provider expected a json content type, false or null otherwise
     */
    public Boolean getJsonContentType() {
        return jsonContentType;
    }

    /**
     * @param jsonContentType true if the provider expected a json content type, false or null otherwise
     */
    public void setJsonContentType(Boolean jsonContentType) {
        this.jsonContentType = jsonContentType;
    }

    @Override
    public String toString() {
        return "Request{" +
                "urlPath='" + urlPath + '\'' +
                ", recipientsSeparator='" + recipientsSeparator + '\'' +
                ", queryParameters=" + queryParameters +
                ", bodyParameters=" + bodyParameters +
                ", type=" + type +
                ", props=" + props +
                ", processedUrlPath='" + processedUrlPath + '\'' +
                ", jsonContentType=" + jsonContentType +
                '}';
    }
}
