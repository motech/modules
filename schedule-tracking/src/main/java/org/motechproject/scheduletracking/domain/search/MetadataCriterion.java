package org.motechproject.scheduletracking.domain.search;

import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.ArrayList;
import java.util.List;

public class MetadataCriterion implements Criterion {
    private String key;
    private String value;

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
