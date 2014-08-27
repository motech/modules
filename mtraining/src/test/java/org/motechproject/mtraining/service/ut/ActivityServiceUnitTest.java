package org.motechproject.mtraining.service.ut;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.mtraining.domain.ActivityRecord;
import org.motechproject.mtraining.domain.ActivityState;
import org.motechproject.mtraining.repository.ActivityDataService;
import org.motechproject.mtraining.service.ActivityService;
import org.motechproject.mtraining.service.impl.ActivityServiceImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit tests for Activity Service
 */
public class ActivityServiceUnitTest {
    @Mock
    private ActivityService activityService;

    @Mock
    private ActivityDataService activityDataService;

    @Before
    public void setup() {
        initMocks(this);
        activityService = new ActivityServiceImpl(activityDataService);
    }

    @Test
    public void getActivityForUser() {
        List<ActivityRecord> activityRecords = (List<ActivityRecord>) mock(List.class);
        when(activityDataService.findRecordsForUser("1414")).thenReturn(activityRecords);
        assertEquals(activityRecords, activityService.getAllActivityForUser("1414"));
    }

    @Test
    public void getEmptyActivityForUser() {
        List<ActivityRecord> activityRecords = (List<ActivityRecord>) mock(List.class);
        when(activityDataService.findRecordsForUser("1414")).thenReturn(activityRecords);
        assertEquals(0, activityService.getAllActivityForUser("1515").size());
    }

    @Test
    public void getCompletedActivityForUser() {
        List<ActivityRecord> activityRecords = (List<ActivityRecord>) mock(List.class);
        when(activityDataService.findRecordsForUserByState(anyString(), (ActivityState) anyObject())).thenReturn(activityRecords);
        assertEquals(activityRecords, activityService.getCompletedActivityForUser("1212"));
    }
    @Test
    public void getActivityById() {
        ActivityRecord activityRecord = new ActivityRecord();
        when(activityDataService.findRecordById(new Long(12))).thenReturn(activityRecord);
        assertEquals(activityRecord, activityService.getActivityById(12));
    }
}
