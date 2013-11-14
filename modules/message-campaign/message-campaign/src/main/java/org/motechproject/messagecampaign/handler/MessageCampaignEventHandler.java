package org.motechproject.messagecampaign.handler;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.messagecampaign.EventKeys.CAMPAIGN_NAME_KEY;
import static org.motechproject.messagecampaign.EventKeys.ENROLL_USER_SUBJECT;
import static org.motechproject.messagecampaign.EventKeys.EXTERNAL_ID_KEY;
import static org.motechproject.messagecampaign.EventKeys.REFERENCE_DATE;
import static org.motechproject.messagecampaign.EventKeys.START_TIME;
import static org.motechproject.messagecampaign.EventKeys.UNENROLL_USER_SUBJECT;

/**
 * Handler for {@link org.motechproject.messagecampaign.EventKeys#ENROLL_USER_SUBJECT} and
 * {@link org.motechproject.messagecampaign.EventKeys#UNENROLL_USER_SUBJECT} events.
 */
@Component
public class MessageCampaignEventHandler {

    @Autowired
    private MessageCampaignService messageCampaignService;

    @MotechListener(subjects = {ENROLL_USER_SUBJECT, UNENROLL_USER_SUBJECT})
    public void enrollOrUnenroll(MotechEvent event) {
        CampaignRequest request = new CampaignRequest(
                getString(event, EXTERNAL_ID_KEY),
                getString(event, CAMPAIGN_NAME_KEY),
                getLocalDate(event, REFERENCE_DATE),
                getTime(event, START_TIME)
        );

        switch (event.getSubject()) {
            case ENROLL_USER_SUBJECT:
                messageCampaignService.startFor(request);
                break;
            case UNENROLL_USER_SUBJECT:
                messageCampaignService.stopAll(request);
                break;
            default:
        }
    }

    private String getString(MotechEvent event, String key) {
        return event.getParameters().get(key).toString();
    }

    private LocalDate getLocalDate(MotechEvent event, String key) {
        Object param = event.getParameters().get(key);
        LocalDate referenceDate;

        if (param instanceof LocalDate) {
            referenceDate = (LocalDate) param;
        } else if (param instanceof DateTime) {
            referenceDate = ((DateTime) param).toLocalDate();
        } else if (param != null) {
            referenceDate = LocalDate.parse(param.toString());
        } else {
            referenceDate = null;
        }

        return referenceDate;
    }

    private Time getTime(MotechEvent event, String key) {
        Object param = event.getParameters().get(key);
        Time time;

        if (param instanceof DateTime) {
            DateTime dateTime = (DateTime) param;
            time = new Time(dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
        } else if (param != null) {
            time = new Time(param.toString());
        } else {
            time = null;
        }

        return time;
    }
}
