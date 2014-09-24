package org.motechproject.ivr.domain;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IVR provider configuration, represents how the IVR module interacts with an IVR provider
 */
public class Config {

    /**
     * How a config is identified
     */
    private String name;

    /**
     * List of fields the IVR provider sends to the status controller which shouldn't be included (ie: ignored) in
     * the CDR data
     */
    private List<String> ignoredStatusFields;

    /**
     * Template string used to issue an HTTP call to the IVR provider in order to initiate an outgoing (MT) call.
     * [xxx] placeholders are replaced with the values provided in the initiateCall() method.
     */
    private String outgoingCallUriTemplate;

    /**
     * Which HTTP method should be used to trigger an outgoing call from the IVR provider
     */
    private HttpMethod outgoingCallMethod;

    /**
     * A map of parameters to be substituted in the outgoing URI template
     */
    @JsonIgnore
    private Map<String, String> statusFieldMap = new HashMap<>();
    /**
     * This field is used to pass the status field back & forth to the UI
     */
    private String statusFieldMapString;

    public Config(String name, List<String> ignoredStatusFields, String statusFieldMapString,
                  HttpMethod outgoingCallMethod, String outgoingCallUriTemplate) {
        this.name = name;
        this.ignoredStatusFields = ignoredStatusFields;
        this.outgoingCallUriTemplate = outgoingCallUriTemplate;
        this.outgoingCallMethod = outgoingCallMethod;
        this.statusFieldMapString = statusFieldMapString;
        this.statusFieldMap = parseStatusMapString(statusFieldMapString);
    }

    private Map<String, String> parseStatusMapString(String string) {
        //todo: replace that with guava Splitter when guava 18.0 is available in external-osgi-bundles
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(string)) {
            return map;
        }
        String[] strings = string.split("\\s*,\\s*");
        for (String s : strings) {
            String[] kv = s.split("\\s*:\\s*");
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            } else {
                throw new IllegalArgumentException(String.format("%s is an invalid map", string));
            }
        }
        return map;
    }

    public String getName() {
        return name;
    }

    /**
     * Quick way of knowing if a field supplied by the IVR provider in the status callback should be ignored
     *
     * @return true if the given field should be ignored, false otherwise
     */
    public boolean shouldIgnoreField(String fieldName) {
        return (null != ignoredStatusFields && ignoredStatusFields.contains(fieldName));
    }

    public void setStatusFieldMapString(String statusFieldMapString) {
        this.statusFieldMapString = statusFieldMapString;
        this.statusFieldMap = parseStatusMapString(statusFieldMapString);
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIgnoredStatusFields() {
        return ignoredStatusFields;
    }

    public void setIgnoredStatusFields(List<String> ignoredStatusFields) {
        this.ignoredStatusFields = ignoredStatusFields;
    }

    public String getOutgoingCallUriTemplate() {
        return outgoingCallUriTemplate;
    }

    public void setOutgoingCallUriTemplate(String outgoingCallUriTemplate) {
        this.outgoingCallUriTemplate = outgoingCallUriTemplate;
    }

    public HttpMethod getOutgoingCallMethod() {
        return outgoingCallMethod;
    }

    public void setOutgoingCallMethod(HttpMethod outgoingCallMethod) {
        this.outgoingCallMethod = outgoingCallMethod;
    }

    public String getStatusFieldMapString() {
        return statusFieldMapString;
    }

    /**
     * When pinging Motech back to provide call status, IVR providers sometimes send fields with different names than
     * those that are used by the system. For example the originating number is sometimes provided as 'callerid' whereas
     * Motech uses the name 'from'. The statusFieldMap config field contains such a mapping of field names. And
     * mapStatusField() returns that mapping or the original field name if no mapping exists.
     *
     * @param fieldName
     * @return
     */
    public String mapStatusField(String fieldName) {
        // When a Config is deserialized from json, the statusFieldMapString may be set, but no constructor or setter is
        // called, so it's possible that the statusFieldMap is null when statusFieldMapString isn't blank, if that's the
        // case then just parse statusFieldMapString when it's needed the first time.
        if (null == statusFieldMap && !StringUtils.isBlank(statusFieldMapString)) {
            statusFieldMap = parseStatusMapString(statusFieldMapString);
        }
        if (null != statusFieldMap && statusFieldMap.containsKey(fieldName)) {
            return statusFieldMap.get(fieldName);
        }
        return fieldName;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Config)) {
            return false;
        }

        Config config = (Config) o;

        if (ignoredStatusFields != null ? !ignoredStatusFields.equals(config.ignoredStatusFields) : config
                .ignoredStatusFields != null) {
            return false;
        }
        if (!name.equals(config.name)) {
            return false;
        }
        if (outgoingCallMethod != config.outgoingCallMethod) {
            return false;
        }
        if (outgoingCallUriTemplate != null ? !outgoingCallUriTemplate.equals(config.outgoingCallUriTemplate) :
                config.outgoingCallUriTemplate != null) {
            return false;
        }
        if (statusFieldMap != null ? !statusFieldMap.equals(config.statusFieldMap) : config.statusFieldMap != null) {
            return false;
        }
        if (statusFieldMapString != null ? !statusFieldMapString.equals(config.statusFieldMapString) :
                config.statusFieldMapString != null) {
            return false;
        }

        return true;
    }
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (ignoredStatusFields != null ? ignoredStatusFields.hashCode() : 0);
        result = 31 * result + (outgoingCallUriTemplate != null ? outgoingCallUriTemplate.hashCode() : 0);
        result = 31 * result + (outgoingCallMethod != null ? outgoingCallMethod.hashCode() : 0);
        result = 31 * result + (statusFieldMap != null ? statusFieldMap.hashCode() : 0);
        result = 31 * result + (statusFieldMapString != null ? statusFieldMapString.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Config{" +
                "name='" + name + '\'' +
                ", ignoredStatusFields=" + ignoredStatusFields +
                ", outgoingCallUriTemplate='" + outgoingCallUriTemplate + '\'' +
                ", outgoingCallMethod=" + outgoingCallMethod +
                ", statusFieldMap=" + statusFieldMap +
                ", statusFieldMapString='" + statusFieldMapString + '\'' +
                '}';
    }
}
