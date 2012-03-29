package org.motechproject.aggregator.aggregation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobDetail;
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

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);
        when(reaperJob.getName()).thenReturn(JOB_NAME);
        when(reaperJob.getGroup()).thenReturn(JOB_GROUP);
    }

    @Test
    public void shouldRemoveOldScheduleJobWhenItExists() throws Exception {
        when(scheduler.getJobDetail(JOB_NAME, JOB_GROUP)).thenReturn(reaperJob);

        new AggregationScheduler(schedulerFactoryBean, trigger, reaperJob);

        verify(scheduler).deleteJob(JOB_NAME, JOB_GROUP);
    }

    @Test
    public void shouldNotTryAndRemoveOldScheduleJobWhenItDoesNotExist() throws Exception {
        when(scheduler.getJobDetail(JOB_NAME, JOB_GROUP)).thenReturn(null);

        new AggregationScheduler(schedulerFactoryBean, trigger, reaperJob);

        verify(scheduler, times(0)).deleteJob(JOB_NAME, JOB_GROUP);
    }

    @Test
    public void shouldScheduleANewRepeatingJob() throws Exception {
        when(scheduler.getJobDetail(JOB_NAME, JOB_GROUP)).thenReturn(reaperJob);

        new AggregationScheduler(schedulerFactoryBean, trigger, reaperJob);

        verify(scheduler).deleteJob(JOB_NAME, JOB_GROUP);
        verify(scheduler).scheduleJob(reaperJob, trigger);
    }
}
