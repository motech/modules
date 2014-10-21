package org.motechproject.scheduletracking.service;

import org.joda.time.DateTime;

public interface ScheduletrackingTasksActionFacade {

    void enroll(String externalId, String scheduleName, String preferredAlertTime, // NO CHECKSTYLE ParameterNumber
                DateTime referenceDate, String referenceTime, DateTime enrolmentDate, String enrollmentTime,
                String startingMilestoneName);

    void unenroll(String externalId, String scheduleName);
}
