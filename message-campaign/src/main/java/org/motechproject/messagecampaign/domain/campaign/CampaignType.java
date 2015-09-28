package org.motechproject.messagecampaign.domain.campaign;

/**
 * Represents the type of the campaign.
 */
public enum CampaignType {

    ABSOLUTE, OFFSET, REPEAT_INTERVAL, DAY_OF_WEEK, CRON;

    /**
     * Creates an instance of the {@link Campaign}, based on the
     * enum expression in used. Each expression is assigned to exactly
     * one campaign representation.
     *
     * @return empty campaign representation
     */
    public Campaign instance() {
        switch (this) {
            case ABSOLUTE:
                return new AbsoluteCampaign();
            case OFFSET:
                return new OffsetCampaign();
            case REPEAT_INTERVAL:
                return new RepeatIntervalCampaign();
            case DAY_OF_WEEK:
                return new DayOfWeekCampaign();
            case CRON:
                return new CronBasedCampaign();
            default:
                return null;
        }
    }

}
