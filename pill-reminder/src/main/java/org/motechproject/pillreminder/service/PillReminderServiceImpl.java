package org.motechproject.pillreminder.service;

import org.joda.time.LocalDate;
import org.motechproject.pillreminder.builder.PillRegimenBuilder;
import org.motechproject.pillreminder.builder.PillRegimenResponseBuilder;
import org.motechproject.pillreminder.contract.DailyPillRegimenRequest;
import org.motechproject.pillreminder.contract.PillRegimenResponse;
import org.motechproject.pillreminder.dao.PillRegimenDataService;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.PillRegimen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link PillReminderService}. Uses {@link PillRegimenJobScheduler} for scheduling
 * jobs and stores information in MDS using {@link PillRegimenDataService}.
 */
@Component
public class PillReminderServiceImpl implements PillReminderService {
    private PillRegimenDataService pillRegimenDataService;
    private PillRegimenJobScheduler pillRegimenJobScheduler;

    @Override
    @Transactional
    public void createNew(DailyPillRegimenRequest dailyPillRegimenRequest) {
        PillRegimen pillRegimen = new PillRegimenBuilder().createDailyPillRegimenFrom(dailyPillRegimenRequest);
        pillRegimen.validate();

        pillRegimen = pillRegimenDataService.create(pillRegimen);
        pillRegimenJobScheduler.scheduleDailyJob(pillRegimen);
    }

    @Override
    @Transactional
    public void renew(DailyPillRegimenRequest dailyPillRegimenRequest) {
        remove(dailyPillRegimenRequest.getExternalId());
        createNew(dailyPillRegimenRequest);
    }

    @Override
    @Transactional
    public void dosageStatusKnown(Long pillRegimenId, Long dosageId, LocalDate lastCapturedDate) {
        PillRegimen regimen = pillRegimenDataService.findById(pillRegimenId);
        if (regimen == null) {
            throw new IllegalArgumentException("Pill Regimen with id " + pillRegimenId + " not found");
        }

        Dosage dosage = regimen.getDosage(dosageId);
        if (dosage == null) {
            throw new IllegalArgumentException(String.format("Dosage with id %d not found in Pill Regimen with id %d",
                    dosageId, pillRegimenId));
        }

        dosage.updateResponseLastCapturedDate(lastCapturedDate);

        pillRegimenDataService.update(regimen);
    }

    @Override
    @Transactional
    public PillRegimenResponse getPillRegimen(String externalId) {
        PillRegimen pillRegimen = pillRegimenDataService.findByExternalId(externalId);
        return pillRegimen == null ? null : new PillRegimenResponseBuilder().createFrom(pillRegimen);
    }

    @Override
    @Transactional
    public void remove(String externalID) {
        PillRegimen regimen = pillRegimenDataService.findByExternalId(externalID);
        pillRegimenJobScheduler.unscheduleJobs(regimen);
        pillRegimenDataService.delete(regimen);
    }

    @Autowired
    public void setPillRegimenDataService(PillRegimenDataService pillRegimenDataService) {
        this.pillRegimenDataService = pillRegimenDataService;
    }

    @Autowired
    public void setPillRegimenJobScheduler(PillRegimenJobScheduler pillRegimenJobScheduler) {
        this.pillRegimenJobScheduler = pillRegimenJobScheduler;
    }
}
