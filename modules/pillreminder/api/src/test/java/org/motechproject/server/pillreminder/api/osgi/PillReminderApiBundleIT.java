package org.motechproject.server.pillreminder.api.osgi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.server.pillreminder.api.contract.DailyPillRegimenRequest;
import org.motechproject.server.pillreminder.api.contract.DosageRequest;
import org.motechproject.server.pillreminder.api.contract.PillRegimenResponse;
import org.motechproject.server.pillreminder.api.service.PillReminderService;
import org.motechproject.testing.osgi.BasePaxIT;
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
public class PillReminderApiBundleIT extends BasePaxIT {

    @Inject
    private PillReminderService pillReminderService;

    @Test
    public void testPillreminderService() {
        final String externalId = "PillReminderApiBundleIT-" + UUID.randomUUID();
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
