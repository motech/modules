package org.motechproject.scheduletracking.domain.search;

import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.ArrayList;
import java.util.List;

/**
 * Criterion used to filter enrollments by metadata. If the enrollment have got a given key-value entry
 * in their metadata then this criterion is met.
 */
public class MetadataCriterion implements Criterion {

    private String key;

    private String value;

    /**
     * Creates a MetadataCriterion with the key attribute set to {@code key}, the value attribute to {@code value}.
     *
     * @param key the metadata key
     * @param value the metadata value
     */
    public MetadataCriterion(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return allEnrollments.findByMetadataProperty(key, value);
    }

    @Override
    public List<Enrollment> filter(List<Enrollment> enrollments) {
        List<Enrollment> filteredEnrollments = new ArrayList<Enrollment>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getMetadata() != null && value.equals(enrollment.getMetadata().get(key))) {
                filteredEnrollments.add(enrollment);
            }
        }
        return filteredEnrollments;
    }


}
