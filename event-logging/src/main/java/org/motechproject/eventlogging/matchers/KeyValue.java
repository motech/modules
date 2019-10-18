package org.motechproject.eventlogging.matchers;

import java.util.Map;

/**
 * Class representing mapping from one parameter key-value pair to another.
 * Used in {@link org.motechproject.eventlogging.converter.impl.DefaultDbToLogConverter}
 * to replace those pairs before persisting logs in the database.
 */
public class KeyValue {

    private String startKey;
    private Object startValue;
    private String endKey;
    private Object endValue;
    private boolean isOptional;

    /**
     * Creates an instance of KeyValue from passed parameters. If an event contains key specified
     * in startKey and value of this key equals startValue, then that key will be replaced by endKey
     * and that value by endValue.
     *
     * @param startKey key to replace
     * @param startValue value of replacing key
     * @param endKey new key
     * @param endValue new value for new key
     * @param isOptional have no use at the moment
     */
    public KeyValue(String startKey, Object startValue, String endKey, Object endValue, boolean isOptional) {
        this.startKey = startKey;
        this.startValue = startValue;
        this.endKey = endKey;
        this.endValue = endValue;
        this.isOptional = isOptional;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public String getStartKey() {
        return startKey;
    }

    public void setStartKey(String startKey) {
        this.startKey = startKey;
    }

    public Object getStartValue() {
        return startValue;
    }

    public void setStartValue(Object startValue) {
        this.startValue = startValue;
    }

    public String getEndKey() {
        return endKey;
    }

    public void setEndKey(String endKey) {
        this.endKey = endKey;
    }

    public Object getEndValue() {
        return endValue;
    }

    public void setEndValue(Object endValue) {
        this.endValue = endValue;
    }

    public static KeyValue buildFromMap(Map<String, String> map) {
        String startKey = null;
        String startValue = null;
        String endKey = null;
        String endValue = null;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (startKey == null) {
                startKey = entry.getKey();
                startValue = entry.getValue();
            } else {
                endKey = entry.getKey();
                endValue = entry.getValue();
            }
        }

        return new KeyValue(startKey, startValue, endKey, endValue, true);
    }
}
