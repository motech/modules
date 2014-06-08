package org.motechproject.scheduletracking.service;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.scheduletracking.domain.Enrollment;

import java.util.Map;

public interface EnrollmentService {
    String enroll(String externalId, String scheduleName, String startingMilestoneName, DateTime referenceDateTime, DateTime enrollmentDateTime, Time preferredAlertTime, Map<String, String> metadata);

    void fulfillCurrentMilestone(Enrollment enrollment, DateTime fulfillmentDateTime);

    void unenroll(Enrollment enrollment);

    MilestoneAlerts getAlertTimings(String externalId, String scheduleName, String milestoneName, DateTime referenceDateTime, DateTime enrollmentDateTime, Time preferredAlertTime);
}
