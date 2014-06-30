package org.motechproject.messagecampaign.domain.campaign;

public enum CampaignType {

    ABSOLUTE, OFFSET, REPEAT_INTERVAL, DAY_OF_WEEK, CRON;

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
