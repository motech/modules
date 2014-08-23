package org.motechproject.ivr.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Unique;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IVR provider configuration, represents how the IVR module interacts with an IVR provider
 */
@Entity
public class Config {

    // See http://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
    private static final int MAX_URL_SIZE = 2000;

    /**
     * How a config is identified
     */
    @Field
    @Unique
    private String name;

    /**
     * List of fields the IVR provider sends to the status controller which shouldn't be included (ie: ignored) in
     * the CDR data
     */
    @Field
    private List<String> ignoredStatusFields;

    /**
     * Template string used to issue an HTTP GET call to the IVR provider in order to initiate an outgoing (MT) call.
     * [xxx] placeholders are replaced with the values provided in the initiateCall() method.
     */
    @Field
    @Column(length = MAX_URL_SIZE)
    private String outgoingCallUriTemplate;

    /**
     * Which HTTP method should be used to trigger an outgoing call from the IVR provider
     */
    @Field
    private HttpMethod outgoingCallMethod;

    /**
     * A map of parameters to be substituted in the outgoing URI template
     */
    @Field
    private Map<String, String> statusFieldMap = new HashMap<>();

    public Config(String name, List<String> ignoredStatusFields, Map<String, String> statusFieldMap,
                  HttpMethod outgoingCallMethod, String outgoingCallUriTemplate) {
        this.name = name;
        this.ignoredStatusFields = ignoredStatusFields;
        this.outgoingCallUriTemplate = outgoingCallUriTemplate;
        this.outgoingCallMethod = outgoingCallMethod;
        this.statusFieldMap = statusFieldMap;
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

    public List<String> getIgnoredStatusFields() {
        return ignoredStatusFields;
    }

    public String getOutgoingCallUriTemplate() {
        return outgoingCallUriTemplate;
    }

    public HttpMethod getOutgoingCallMethod() {
        return outgoingCallMethod;
    }

    public Map<String, String> getStatusFieldMap() {
        return statusFieldMap;
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
        if (null != statusFieldMap && statusFieldMap.containsKey(fieldName)) {
            return statusFieldMap.get(fieldName);
        }
        return fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Config)) {
            return false;
        }

        //todo: I'm using a string compare because comparing the statusFieldMap field (of type Map<String, CallStatus>) fails
        //todo: change to a proper full fledged equals when https://applab.atlassian.net/browse/MOTECH-1186 is fixed
        return this.toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (ignoredStatusFields != null ? ignoredStatusFields.hashCode() : 0);
        result = 31 * result + (outgoingCallUriTemplate != null ? outgoingCallUriTemplate.hashCode() : 0);
        result = 31 * result + (outgoingCallMethod != null ? outgoingCallMethod.hashCode() : 0);
        result = 31 * result + (statusFieldMap != null ? statusFieldMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Config{" +
                "name='" + name + '\'' +
                ", ignoredStatusFields='" + ignoredStatusFields + '\'' +
                ", outgoingCallUriTemplate='" + outgoingCallUriTemplate + '\'' +
                ", outgoingCallMethod='" + outgoingCallMethod + '\'' +
                ", statusFieldMap='" + statusFieldMap + '\'' +
                '}';
    }
}
