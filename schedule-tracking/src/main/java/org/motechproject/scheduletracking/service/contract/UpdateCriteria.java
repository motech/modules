package org.motechproject.scheduletracking.service.contract;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the criteria builder which is used to form an enrollment update criteria
 */
public class UpdateCriteria {

    private Map<UpdateCriterion, Object> allCriteria = new HashMap<UpdateCriterion, Object>();

    /**
     * This gives the list of all the criterion specified to update an enrollment
     * @return the criterion map specified to update an enrollment
     */
    public Map<UpdateCriterion, Object> getAll() {
        return allCriteria;
    }

    /** Adds metadata criteria to the update criteria list
     *
     * @param metadata value to be updated in the enrollment
     * @return returns the instance with metadata criteria added to the criteria list
     */
    public UpdateCriteria metadata(Map<String, String> metadata) {
        allCriteria.put(UpdateCriterion.Metadata, metadata);
        return this;
    }

}
