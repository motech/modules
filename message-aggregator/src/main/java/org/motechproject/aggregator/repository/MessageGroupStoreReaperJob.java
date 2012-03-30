package org.motechproject.aggregator.repository;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.store.MessageGroupStoreReaper;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class MessageGroupStoreReaperJob extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(MessageGroupStoreReaperJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final ApplicationContext applicationContext;
        try {
            applicationContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext");
            ((MessageGroupStoreReaper) applicationContext.getBean("reaper")).run();

        } catch (SchedulerException e) {
            logger.error("Encountered exception while executing reaper job", e);
        }
    }

}
