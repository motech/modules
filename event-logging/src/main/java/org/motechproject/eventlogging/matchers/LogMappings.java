package org.motechproject.eventlogging.matchers;

import java.util.Collections;
import java.util.List;

/**
 * Class used to store data about manipulations on event parameters before persisting
 * logs in the database. LogMapping allows to include, exclude and replace parameters before
 * persisting them in the log.
 */
public class LogMappings {

    private List<KeyValue> mappings;
    private List<String> exclusions;
    private List<String> inclusions;

    /**
     * Create an instance of LogMappings using passed parameters
     *
     * @param mappings list of KeyValue mappings
     * @param exclusions list of parameters to exclude from the log
     * @param inclusions list of parameters to include in the log
     */
    public LogMappings(List<KeyValue> mappings, List<String> exclusions, List<String> inclusions) {
        if (mappings == null) {
            this.mappings = Collections.<KeyValue> emptyList();
        } else {
            this.mappings = mappings;
        }
        if (exclusions == null) {
            this.exclusions = Collections.<String> emptyList();
        } else {
            this.exclusions = exclusions;
        }
        if (inclusions == null) {
            this.inclusions = Collections.<String> emptyList();
        } else {
            this.inclusions = inclusions;
        }
    }

    public List<String> getInclusions() {
        return inclusions;
    }

    /**
     * Sets inclusions to log - if an event contains a parameter key that is included in the inclusions list,
     * that parameter will be persisted in the database log. By default all parameters are included.
     *
     * @param inclusions list of parameters to include in log
     */
    public void setInclusions(List<String> inclusions) {
        this.inclusions = inclusions;
    }

    public List<KeyValue> getMappings() {
        return mappings;
    }

    /**
     * Sets replacement map for event parameters in the form of a KeyValue list.
     * See {@link KeyValue} for more information.
     *
     * @param mappings list of KeyValue mappings
     */
    public void setMappings(List<KeyValue> mappings) {
        this.mappings = mappings;
    }

    public List<String> getExclusions() {
        return exclusions;
    }

    /**
     * Sets exclusions from log - if an event contains a parameter key that is included in the exclusions list,
     * that parameter will not be persisted in the database log.
     *
     * @param exclusions list of parameters to exclude from log
     */
    public void setExclusions(List<String> exclusions) {
        this.exclusions = exclusions;
    }
}
