package org.motechproject.messagecampaign.scheduler;

import org.motechproject.messagecampaign.exception.CampaignNotFoundException;
import org.motechproject.messagecampaign.domain.campaign.AbsoluteCampaign;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaign;
import org.motechproject.messagecampaign.domain.campaign.DayOfWeekCampaign;
import org.motechproject.messagecampaign.domain.campaign.OffsetCampaign;
import org.motechproject.messagecampaign.domain.campaign.RepeatIntervalCampaign;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * Holds campaign schedulers and allows retrieval of the necessary scheduler, based
 * on the campaign type.
 */
@Component
public class CampaignSchedulerFactory {
    private static final int CAMPAIGN_SCHEDULER_SERVICE_COUNT = 5;

    /**
     * A map holding scheduler services, indexed by campaign classes they serve.
     */
    private Map<Class, CampaignSchedulerService> campaignSchedulerServices = new HashMap<>(CAMPAIGN_SCHEDULER_SERVICE_COUNT);

    @Autowired
    private AbsoluteCampaignSchedulerService absoluteCampaignSchedulerService;

    @Autowired
    private OffsetCampaignSchedulerService offsetCampaignSchedulerService;

    @Autowired
    private CronBasedCampaignSchedulerService cronBasedCampaignSchedulerService;

    @Autowired
    private RepeatIntervalCampaignSchedulerService repeatIntervalCampaignSchedulerService;

    @Autowired
    private DayOfWeekCampaignSchedulerService dayOfWeekCampaignSchedulerService;

    @Autowired
    private CampaignRecordService campaignRecordService;

    /**
     * Initializes a map of available scheduler services.
     * Invoked by Spring, after initialization of this bean finishes.
     */
    @PostConstruct
    public void init() {
        campaignSchedulerServices.put(AbsoluteCampaign.class, absoluteCampaignSchedulerService);
        campaignSchedulerServices.put(OffsetCampaign.class, offsetCampaignSchedulerService);
        campaignSchedulerServices.put(CronBasedCampaign.class, cronBasedCampaignSchedulerService);
        campaignSchedulerServices.put(RepeatIntervalCampaign.class, repeatIntervalCampaignSchedulerService);
        campaignSchedulerServices.put(DayOfWeekCampaign.class, dayOfWeekCampaignSchedulerService);
    }

    /**
     * Retrieves campaign scheduler for the given campaign name, based on this campaign type.
     *
     * @param campaignName the campaign name to retrieve the scheduler for
     * @return scheduler service for the corresponding campaign type
     * @throws CampaignNotFoundException if campaign of such name des not exist or scheduler service for
     *                                   that campaign type cannot be found
     */
    public CampaignSchedulerService getCampaignScheduler(final String campaignName) {
        CampaignRecord campaign = campaignRecordService.findByName(campaignName);

        if (campaign == null) {
            throw new CampaignNotFoundException(format("Campaign (%s) not found.", campaignName));
        }

        CampaignSchedulerService schedulerService = campaignSchedulerServices.get(campaign.toCampaign().getClass());

        if (schedulerService == null) {
            throw new CampaignNotFoundException(format("Scheduler service for campaign class %s cannot be found.", campaign.toCampaign().getClass().getName()));
        }

        return schedulerService;
    }
}
