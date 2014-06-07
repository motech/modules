package org.motechproject.pillreminder.service;

import org.joda.time.LocalDate;
import org.motechproject.pillreminder.builder.PillRegimenBuilder;
import org.motechproject.pillreminder.builder.PillRegimenResponseBuilder;
import org.motechproject.pillreminder.contract.DailyPillRegimenRequest;
import org.motechproject.pillreminder.contract.PillRegimenResponse;
import org.motechproject.pillreminder.dao.AllPillRegimens;
import org.motechproject.pillreminder.domain.PillRegimen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PillReminderServiceImpl implements PillReminderService {
    private AllPillRegimens allPillRegimens;
    private PillRegimenJobScheduler pillRegimenJobScheduler;

    @Autowired
    public PillReminderServiceImpl(AllPillRegimens allPillRegimens, PillRegimenJobScheduler pillRegimenJobScheduler) {
        this.allPillRegimens = allPillRegimens;
        this.pillRegimenJobScheduler = pillRegimenJobScheduler;
    }

    @Override
    public void createNew(DailyPillRegimenRequest dailyPillRegimenRequest) {
        PillRegimen pillRegimen = new PillRegimenBuilder().createDailyPillRegimenFrom(dailyPillRegimenRequest);
        pillRegimen.validate();
        allPillRegimens.addOrReplace(pillRegimen);
        pillRegimenJobScheduler.scheduleDailyJob(pillRegimen);
    }

    @Override
    public void renew(DailyPillRegimenRequest dailyPillRegimenRequest) {
        remove(dailyPillRegimenRequest.getExternalId());
        createNew(dailyPillRegimenRequest);
    }

    @Override
    public void dosageStatusKnown(String pillRegimenId, String dosageId, LocalDate lastCapturedDate) {
        allPillRegimens.updateLastCapturedDate(pillRegimenId, dosageId, lastCapturedDate);
    }

    @Override
    public PillRegimenResponse getPillRegimen(String externalId) {
        PillRegimen pillRegimen = allPillRegimens.findByExternalId(externalId);
        return pillRegimen == null ? null : new PillRegimenResponseBuilder().createFrom(pillRegimen);
    }

    @Override
    public void remove(String externalID) {
        PillRegimen regimen = allPillRegimens.findByExternalId(externalID);
        pillRegimenJobScheduler.unscheduleJobs(regimen);
        allPillRegimens.safeRemove(regimen);
    }
}
