package org.motechproject.ivr.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Entity
public class CallDetailRecord {

    private static final int MAX_ENTITY_STRING_LENGTH = 255;
    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSS");
    private static final Logger LOGGER = LoggerFactory.getLogger(CallDetailRecord.class);

    @Field(required = true)
    private String motechTimestamp;

    @Field
    private String providerTimestamp;

    @Field
    private String configName;

    @Field
    private String from;

    @Field
    private String to;

    @Field
    private CallDirection callDirection;

    @Field
    private CallStatus callStatus;

    @Field
    private String motechCallId;

    @Field
    private String providerCallId;

    @Field
    private Map<String, String> providerExtraData;

    public CallDetailRecord() {
        providerExtraData = new HashMap<>();
        callStatus = CallStatus.UNKNOWN;
        this.motechTimestamp = DT_FORMATTER.print(DateTime.now());
    }

    public CallDetailRecord(String configName,  //NO CHECKSTYLE ParameterNumber
                            String providerTimestamp, String from, String to, CallDirection callDirection,
                            CallStatus callStatus, String motechCallId, String providerCallId,
                            Map<String, String> providerExtraData) {
        this();
        this.configName = configName;
        this.providerTimestamp = providerTimestamp;
        this.from = from;
        this.to = to;
        this.callDirection = callDirection;
        this.callStatus = callStatus;
        this.motechCallId = motechCallId;
        this.providerCallId = providerCallId;
        if (providerExtraData != null) {
            this.providerExtraData = providerExtraData;
        }
    }

    public static String getCurrentTimestamp() {
        return DT_FORMATTER.print(DateTime.now());
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getMotechTimestamp() {
        return motechTimestamp;
    }

    public void setMotechTimestamp(String motechTimestamp) {
        this.motechTimestamp = motechTimestamp;
    }

    public String getProviderTimestamp() {
        return providerTimestamp;
    }

    public void setProviderTimestamp(String providerTimestamp) {
        this.providerTimestamp = providerTimestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public CallDirection getCallDirection() {
        return callDirection;
    }

    public CallStatus getCallStatus() {
        return callStatus;
    }

    public String getMotechCallId() {
        return motechCallId;
    }

    public String getProviderCallId() {
        return providerCallId;
    }

    public Map<String, String> getProviderExtraData() {
        return providerExtraData;
    }

    /**
     * When receiving call detail information from an IVR provider the specific call details must be mapped from
     * what the provider sends back to MOTECH and a CallDetailRecord object. This method will find which field on the
     * given callDetailRecord matches the given key and set service to the given value. If there is no matching
     * CallDetailRecord field, then the key/value pair is added to the providerExtraData map field. Also, if a
     * callStatus value does not map to an existing CallStatus, then the value of the callStatus field is set to
     * CallStatus.UNKNOWN and the string value (with 'callStatus' key) is added to the providerExtraData
     * CallDetailRecord map field.
     *
     * @param key
     * @param val
     */
    public void setField(String key, String val) {
        String value;

        if (val.length() > MAX_ENTITY_STRING_LENGTH) {
            LOGGER.warn("The value for {} exceeds {} characters and will be truncated.", key, MAX_ENTITY_STRING_LENGTH);
            LOGGER.warn("The complete value for {} is {}", key, val);
            value = val.substring(0, MAX_ENTITY_STRING_LENGTH);
        }  else {
            value = val;
        }

        try {
            java.lang.reflect.Field field = this.getClass().getDeclaredField(key);
            Object object;
            try {
                switch (key) {
                    case "callStatus":
                        try {
                            object = CallStatus.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            // Always add unknown call status to the provider extra data, for inspection
                            LOGGER.warn("Unknown callStatus: {}", value);
                            providerExtraData.put(key, value);
                            object = CallStatus.UNKNOWN;
                        }
                        break;
                    case  "callDirection":
                        try {
                            object = CallDirection.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            // Always add unknown call directions to the provider extra data, for inspection
                            LOGGER.warn("Unknown callDirection: {}", value);
                            providerExtraData.put(key, value);
                            object = CallDirection.UNKNOWN;
                        }
                        break;
                    default:
                        object = value;
                        break;
                }
                field.set(this, object);
            } catch (IllegalAccessException e) {
                // This should never happen as all CallDetailRecord fields should be accessible, but if somehow there
                // happens to be a 'final' public field with the same name as a call detail key, then this will throw
                throw new IllegalStateException(String.format(
                        "Unable to set call detail record field '%s' to value '%s':\n%s", key, value, e));
            }
        } catch (NoSuchFieldException e) {
            LOGGER.info("Extra data from provider: '{}': '{}'", key, value);
            providerExtraData.put(key, value);
        }
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CallDetailRecord)) {
            return false;
        }

        CallDetailRecord that = (CallDetailRecord) o;

        return !((callDirection != that.callDirection) ||
                (callStatus != that.callStatus) ||
                (!configName.equals(that.configName)) ||
                (from != null ? !from.equals(that.from) : that.from != null) ||
                (motechCallId != null ? !motechCallId.equals(that.motechCallId) : that.motechCallId != null) ||
                (providerCallId != null ? !providerCallId.equals(that.providerCallId) : that.providerCallId != null) ||
                (providerExtraData != null ?
                        !providerExtraData.equals(that.providerExtraData) : that.providerExtraData != null) ||
                (!motechTimestamp.equals(that.motechTimestamp)) ||
                (!providerTimestamp.equals(that.providerTimestamp)) ||
                (to != null ? !to.equals(that.to) : that.to != null));

    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public int hashCode() {
        int result = motechTimestamp != null ? motechTimestamp.hashCode() : 0;
        result = 31 * result + (providerTimestamp != null ? providerTimestamp.hashCode() : 0);
        result = 31 * result + (configName != null ? configName.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (callDirection != null ? callDirection.hashCode() : 0);
        result = 31 * result + (callStatus != null ? callStatus.hashCode() : 0);
        result = 31 * result + (motechCallId != null ? motechCallId.hashCode() : 0);
        result = 31 * result + (providerCallId != null ? providerCallId.hashCode() : 0);
        result = 31 * result + (providerExtraData != null ? providerExtraData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CallDetailRecord{" +
                "motechTimestamp='" + motechTimestamp + '\'' +
                ", providerTimestamp='" + providerTimestamp + '\'' +
                ", configName='" + configName + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", callDirection=" + callDirection +
                ", callStatus=" + callStatus +
                ", motechCallId='" + motechCallId + '\'' +
                ", providerCallId='" + providerCallId + '\'' +
                ", providerExtraData=" + providerExtraData +
                '}';
    }
}
