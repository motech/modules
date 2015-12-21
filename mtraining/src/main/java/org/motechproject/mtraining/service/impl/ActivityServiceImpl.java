package org.motechproject.mtraining.service.impl;

import org.motechproject.mtraining.domain.ActivityRecord;
import org.motechproject.mtraining.domain.ActivityState;
import org.motechproject.mtraining.repository.ActivityDataService;
import org.motechproject.mtraining.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the activity service. This helps retrieve all the activity for a particular user
 * such as enrollment in a course or score in a quiz, etc
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    private ActivityDataService activityDataService;

    @Autowired
    public ActivityServiceImpl(ActivityDataService activityDataService) {
        this.activityDataService = activityDataService;
    }

    @Override
    @Transactional
    public ActivityRecord createActivity(ActivityRecord activityRecord) {
        return activityDataService.create(activityRecord);
    }

    @Override
    @Transactional
    public ActivityRecord updateActivity(ActivityRecord activityRecord) {
        return activityDataService.update(activityRecord);
    }

    @Override
    @Transactional
    public ActivityRecord getActivityById(long activityId) {
        return activityDataService.findRecordById(activityId);
    }

    @Override
    @Transactional
    public List<ActivityRecord> getAllActivityForUser(String externalId) {
        return activityDataService.findRecordsForUser(externalId);
    }

    @Override
    @Transactional
    public List<ActivityRecord> getCompletedActivityForUser(String externalId) {
        return activityDataService.findRecordsForUserByState(externalId, ActivityState.COMPLETED);
    }

    @Override
    @Transactional
    public void deleteActivity(long activityRecordId) {
        activityDataService.delete("id", activityRecordId);
    }

    @Override
    @Transactional
    public void deleteAllActivityForUser(String externalId) {
        for (ActivityRecord current : getAllActivityForUser(externalId)) {
            activityDataService.delete(current);
        }
    }
}
