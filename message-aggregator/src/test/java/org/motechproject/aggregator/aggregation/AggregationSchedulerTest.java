package org.motechproject.aggregator.aggregation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AggregationSchedulerTest {
    public static final String JOB_NAME = "REAPER";
    public static final String JOB_GROUP = "REAPER-GROUP";
    @Mock
    private SchedulerFactoryBean schedulerFactoryBean;
    @Mock
    private Trigger trigger;
    @Mock
    private JobDetail reaperJob;
    @Mock
    private Scheduler scheduler;
    private JobKey jobKey;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        jobKey = new JobKey(JOB_NAME, JOB_GROUP);

        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);
        when(reaperJob.getKey()).thenReturn(jobKey);
    }

    @Test
    public void shouldRemoveOldScheduleJobWhenItExists() throws Exception {
        when(scheduler.getJobDetail(jobKey)).thenReturn(reaperJob);

        new AggregationScheduler(schedulerFactoryBean, trigger, reaperJob);

        verify(scheduler).deleteJob(jobKey);
    }

    @Test
    public void shouldNotTryAndRemoveOldScheduleJobWhenItDoesNotExist() throws Exception {
        when(scheduler.getJobDetail(jobKey)).thenReturn(null);

        new AggregationScheduler(schedulerFactoryBean, trigger, reaperJob);

        verify(scheduler, times(0)).deleteJob(jobKey);
    }

    @Test
    public void shouldScheduleANewRepeatingJob() throws Exception {
        when(scheduler.getJobDetail(jobKey)).thenReturn(reaperJob);

        new AggregationScheduler(schedulerFactoryBean, trigger, reaperJob);

        verify(scheduler).deleteJob(jobKey);
        verify(scheduler).scheduleJob(reaperJob, trigger);
    }
}
