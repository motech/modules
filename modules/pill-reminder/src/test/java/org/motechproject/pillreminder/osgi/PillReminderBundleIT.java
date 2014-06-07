package org.motechproject.pillreminder.osgi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.pillreminder.contract.DailyPillRegimenRequest;
import org.motechproject.pillreminder.contract.DosageRequest;
import org.motechproject.pillreminder.contract.PillRegimenResponse;
import org.motechproject.pillreminder.service.PillReminderService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class PillReminderBundleIT extends BasePaxIT {

    @Inject
    private PillReminderService pillReminderService;

    @Test
    public void testPillreminderService() {
        final String externalId = "PillReminderBundleIT-" + UUID.randomUUID();
        try {
            pillReminderService.createNew(new DailyPillRegimenRequest(externalId, 2, 15, 5, new ArrayList<DosageRequest>()));
            PillRegimenResponse response =  pillReminderService.getPillRegimen(externalId);
            assertNotNull(response);
            assertEquals(externalId, response.getExternalId());
        } finally {
            pillReminderService.remove(externalId);
        }
    }
}
