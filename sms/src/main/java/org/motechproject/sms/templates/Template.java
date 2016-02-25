package org.motechproject.sms.templates;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.motechproject.config.SettingsFacade;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Models how we can talk to a specific SMS provider.
 */
public class Template {

    public static final Pattern FIND_TOKEN_PATTERN = Pattern.compile("\\[(\\w*)\\]");

    /**
     * Models the handling of outgoing SMS messages.
     */
    private Outgoing outgoing;

    /**
     * Models how the status updates from the provider are being handled.
     */
    private Status status;

    /**
     * Models how incoming SMS messages are being handled.
     */
    private Incoming incoming;

    /**
     * The unique name of the template.
     */
    private String name;

    /**
     * Configurable values that the user can edit on the UI.
     */
    private List<String> configurables;

    /**
     * Generates an HTTP request for an outgoing SMS from the provided properties.
     * @param props the properties used for building the request
     * @return the HTTP request to execute
     */
    public HttpMethod generateRequestFor(Map<String, String> props) {
        HttpMethod httpMethod;
        if (HttpMethodType.POST.equals(outgoing.getRequest().getType())) {
            httpMethod = new PostMethod(outgoing.getRequest().getUrlPath(props));
            if (outgoing.getRequest().getJsonContentType()) {
                Map<String, String> jsonParams = getJsonParameters(outgoing.getRequest().getBodyParameters(), props);
                Gson gson = new Gson();
                JsonObject jsonObject = new JsonObject();
                for (Map.Entry<String, String> entry : jsonParams.entrySet()) {
                    JsonElement obj;
                    try {
                        obj = new JsonParser().parse(entry.getValue());
                    } catch (JsonSyntaxException e) {
                        obj = new JsonPrimitive(entry.getValue());
                    }
                    jsonObject.add(entry.getKey(), obj);
                }
                String json = gson.toJson(jsonObject);
                StringRequestEntity requestEntity;
                try {
                    requestEntity = new StringRequestEntity(json, MediaType.APPLICATION_JSON_VALUE, "UTF-8");
                } catch  (UnsupportedEncodingException e) {
                    throw new IllegalStateException(String.format("Template error: %s: invalid json", name), e);
                }
                ((PostMethod) httpMethod).setRequestEntity(requestEntity);
            } else {
                httpMethod.setRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE);
                addBodyParameters((PostMethod) httpMethod, props);
            }
        } else {
            httpMethod = new GetMethod(outgoing.getRequest().getUrlPath(props));
        }
        httpMethod.setQueryString(addQueryParameters(props));

        return httpMethod;
    }

    /**
     * Formats the recipient list into a single string that can be understood by the provider.
     * @param recipients the list of recipients
     * @return the recipient string for the provider
     */
    public String recipientsAsString(List<String> recipients) {
        return StringUtils.join(recipients.iterator(), outgoing.getRequest().getRecipientsSeparator());
    }

    /**
     * @return the {@link Outgoing} instance used by this template for outgoing SMS messages
     */
    public Outgoing getOutgoing() {
        return outgoing;
    }

    /**
     * @return the {@link Incoming} instance used by this template for incoming SMS messages
     */
    public Incoming getIncoming() {
        return incoming;
    }

    /**
     * @return the unique name of this template
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the unique name of this template
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return configurable values that the user can edit on the UI
     */
    public List<String> getConfigurables() {
        return configurables;
    }

    /**
     * @return the {@link Status} object used for dealing with status updates from the provider
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the {@link Status} object used for dealing with status updates from the provider
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Reads the default values from Motech settings, updating this template.
     * @param settingsFacade the settings facade used for reading the settings
     */
    public void readDefaults(SettingsFacade settingsFacade) {
        outgoing.readDefaults(settingsFacade);
    }

    private NameValuePair[] addQueryParameters(Map<String, String> props) {
        List<NameValuePair> queryStringValues = new ArrayList<>();
        Map<String, String> queryParameters = outgoing.getRequest().getQueryParameters();
        for (Map.Entry< String, String > entry : queryParameters.entrySet()) {
            String value = placeHolderOrLiteral(entry.getValue(), props);
            queryStringValues.add(new NameValuePair(entry.getKey(), value));
        }
        return queryStringValues.toArray(new NameValuePair[queryStringValues.size()]);
    }

    private Map<String, String> getJsonParameters(Map<String, String> bodyParameters, Map<String, String> props) {
        Map<String, String> ret = new HashMap<>();
        for (Map.Entry<String, String> entry: bodyParameters.entrySet()) {
            String value = placeHolderOrLiteral(entry.getValue(), props);
            ret.put(entry.getKey(), value);
        }
        return ret;
    }

    private void addBodyParameters(PostMethod postMethod, Map<String, String> props) {
        Map<String, String> bodyParameters = outgoing.getRequest().getBodyParameters();
        for (Map.Entry<String, String> entry: bodyParameters.entrySet()) {
            String value = placeHolderOrLiteral(entry.getValue(), props);
            postMethod.setParameter(entry.getKey(), value);
        }
    }

    // return input string replacing [tokens] with their values from props
    private static String placeHolderOrLiteral(String value, Map<String, String> props) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = FIND_TOKEN_PATTERN.matcher(value);

        while (matcher.find()) {
            String repString = props.get(matcher.group(1));
            if (repString != null) {
                matcher.appendReplacement(sb, repString);
            } else {
                throw new IllegalStateException(String.format("Template error! Unable to find value for [%s]",
                        matcher.group(1)));
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    @Override
    public String toString() {
        return "Template{" +
                "outgoing=" + outgoing +
                ", status=" + status +
                ", incoming=" + incoming +
                ", name='" + name + '\'' +
                ", configurables=" + configurables +
                '}';
    }
}
