package org.motechproject.eventlogging.matchers;

import org.motechproject.event.MotechEvent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Version of {@link LoggableEvent} class used when working with mappings. Additionaly to LoggableEvent it
 * stores {@link LogMappings} used for manipulating event parameters before persisting log.
 */
public class MappedLoggableEvent extends LoggableEvent {

    private LogMappings mappings;

    /**
     * Creates an instance of MappedLoggableEvent using passed parameters
     *
     * @param eventSubjects list of event subjects to log
     * @param flags event flags used for filtering events by parameters
     * @param mappings {@link LogMappings} object used for replacing, including and excluding parameters before logging to the database
     */
    public MappedLoggableEvent(List<String> eventSubjects, List<? extends EventFlag> flags, LogMappings mappings) {
        super(eventSubjects, flags);
        this.mappings = mappings;
    }

    public LogMappings getMappings() {
        return mappings;
    }

    public void setMappings(LogMappings mappings) {
        this.mappings = mappings;
    }

    public Map<String, Object> filterParams(MotechEvent event) {
        Map<String, Object> initialParameters = event.getParameters();

        LogMappings logMappings = getMappings();
        List<KeyValue> keyValueList = logMappings.getMappings();
        List<String> exclusions = logMappings.getExclusions();
        List<String> inclusions = logMappings.getInclusions();
        Map<String, Object> finalParameters = new LinkedHashMap<String, Object>(initialParameters);

        for (KeyValue keyValue : keyValueList) {
            if (initialParameters.containsKey(keyValue.getStartKey())) {
                if (keyValue.getStartValue().equals(initialParameters.get(keyValue.getStartKey()))) {
                    finalParameters.put(keyValue.getEndKey(), keyValue.getEndValue());
                    exclusions.add(keyValue.getStartKey());
                }
            }
        }

        for (String excludeParameter : exclusions) {
            if (initialParameters.containsKey(excludeParameter)) {
                finalParameters.remove(excludeParameter);
            }
        }

        for (String includeParameter : inclusions) {
            if (initialParameters.containsKey(includeParameter)) {
                finalParameters.put(includeParameter, initialParameters.get(includeParameter));
            }
        }

        return finalParameters;
    }

}
