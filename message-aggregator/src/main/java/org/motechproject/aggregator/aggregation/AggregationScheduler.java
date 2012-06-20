package org.motechproject.aggregator.aggregation;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class AggregationScheduler {
    private Logger logger = LoggerFactory.getLogger(AggregationScheduler.class);

    private SchedulerFactoryBean schedulerFactoryBean;
    private Trigger reaperTrigger;
    private JobDetail reaperJob;

    @Autowired
    public AggregationScheduler(SchedulerFactoryBean schedulerFactoryBean,
                                @Qualifier("reaperTrigger") Trigger reaperTrigger, @Qualifier("reaperJob") JobDetail reaperJob) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.reaperTrigger = reaperTrigger;
        this.reaperJob = reaperJob;
        initialize();
    }

    public void initialize() {
        try {
            final JobDetail existingReaperJob = schedulerFactoryBean.getScheduler().getJobDetail(reaperJob.getKey());
            if (existingReaperJob != null) {
                schedulerFactoryBean.getScheduler().deleteJob(reaperJob.getKey());
            }
            schedulerFactoryBean.getScheduler().scheduleJob(reaperJob, reaperTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            logger.error("Encountered exception while scheduling aggregation", e);
        }
    }

}

