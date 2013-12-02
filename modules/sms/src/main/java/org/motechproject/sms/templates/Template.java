package org.motechproject.sms.templates;

import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Models how we can talk to a specific SMS provider
 */
public class Template {

    public static final String MESSAGE_PLACEHOLDER = "$message";
    public static final String RECIPIENTS_PLACEHOLDER = "$recipients";
    public static final Pattern FIND_TOKEN_PATTERN = Pattern.compile("\\[(\\w*)\\]");

    private Outgoing outgoing;
    private Status status;
    private Incoming incoming;
    private String name;
    private List<String> configurables;

    public HttpMethod generateRequestFor(Map<String, String> props) {
        HttpMethod httpMethod;
        if (HttpMethodType.POST.equals(outgoing.getRequest().getType())) {
            httpMethod = new PostMethod(outgoing.getRequest().getUrlPath(props));
            if (outgoing.getRequest().getJsonContentType()) {
                Map<String, String> jsonParams = getJsonParameters(outgoing.getRequest().getBodyParameters(), props);
                Gson gson = new Gson();
                String json = gson.toJson(jsonParams);
                StringRequestEntity requestEntity = null;
                try {
                    requestEntity = new StringRequestEntity(json, MediaType.APPLICATION_JSON_VALUE, "UTF-8");
                } catch  (UnsupportedEncodingException e) {
                    throw new IllegalStateException(String.format("Template error: {}: invalid json: {}", name,
                            e.toString()));
                }
                if (requestEntity != null) {
                    ((PostMethod) httpMethod).setRequestEntity(requestEntity);
                } else {
                    throw new IllegalStateException(String.format("Template error: {}: null request entity", name));
                }
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

    private NameValuePair[] addQueryParameters(Map<String, String> props) {
        List<NameValuePair> queryStringValues = new ArrayList<NameValuePair>();
        Map<String, String> queryParameters = outgoing.getRequest().getQueryParameters();
        for (Map.Entry< String, String > entry : queryParameters.entrySet()) {
            String value = placeHolderOrLiteral(entry.getValue(), props);
            queryStringValues.add(new NameValuePair(entry.getKey(), value));
        }
        return queryStringValues.toArray(new NameValuePair[queryStringValues.size()]);
    }

    private Map<String, String> getJsonParameters(Map<String, String> bodyParameters, Map<String, String> props) {
        Map<String, String> ret = new HashMap<String, String>();
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

    public String recipientsAsString(List<String> recipients) {
        return StringUtils.join(recipients.iterator(), outgoing.getRequest().getRecipientsSeparator());
    }

    public Outgoing getOutgoing() {
        return outgoing;
    }

    public Incoming getIncoming() {
        return incoming;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getConfigurables() {
        return configurables;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void readDefaults(SettingsFacade settingsFacade) {
        outgoing.readDefaults(settingsFacade);
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
