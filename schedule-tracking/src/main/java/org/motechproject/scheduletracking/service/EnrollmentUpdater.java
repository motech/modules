package org.motechproject.scheduletracking.service;

import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.service.contract.UpdateCriterion;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum used to perform updates on given enrollments.
 */
public enum EnrollmentUpdater {

    /**
     * Enrollment metadata updater.
     */
    metadataUpdater {

        /**
         * Updates the metadata in the given enrollment.
         *
         * @param enrollment the enrollment to update
         * @param newValue the metadata to add or update
         * @return the updated enrollment
         */
        @Override
        public Enrollment update(Enrollment enrollment, Object newValue) {
            Map<String, String> metadata = new HashMap<String, String>(enrollment.getMetadata());
            Map<String, String> tobeUpdatedMetadata = (Map<String, String>) newValue;
            
            for (Map.Entry<String, String> entry : tobeUpdatedMetadata.entrySet()) {
                metadata.put(entry.getKey(), entry.getValue());
            }

            enrollment.setMetadata(metadata);
            return enrollment;
        }
    };

    /**
     * Updates the given enrolment.
     *
     * @param enrollment the enrollment to update
     * @param newValue  the value to update
     * @return the updated enrollment
     */
    public abstract Enrollment update(Enrollment enrollment, Object newValue);

    private static Map<UpdateCriterion, EnrollmentUpdater> updateCriterionMap = new HashMap<UpdateCriterion, EnrollmentUpdater>();

    static {
        updateCriterionMap.put(UpdateCriterion.Metadata, EnrollmentUpdater.metadataUpdater);
    }

    /**
     * Returns the enrollment updater for the given update criteria.
     *
     * @param updateCriterion the update criteria
     * @return the enrollment updater
     */
    public static EnrollmentUpdater get(UpdateCriterion updateCriterion) {
        return updateCriterionMap.get(updateCriterion);
    }
}
