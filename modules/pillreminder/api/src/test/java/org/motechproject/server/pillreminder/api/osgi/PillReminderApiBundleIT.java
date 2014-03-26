package org.motechproject.server.pillreminder.api.osgi;

import org.motechproject.server.pillreminder.api.contract.DailyPillRegimenRequest;
import org.motechproject.server.pillreminder.api.contract.DosageRequest;
import org.motechproject.server.pillreminder.api.contract.PillRegimenResponse;
import org.motechproject.server.pillreminder.api.service.PillReminderService;
import org.motechproject.testing.osgi.BaseOsgiIT;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;

public class PillReminderApiBundleIT extends BaseOsgiIT {

    public void testPillreminderService() {
        getService(PillReminderService.class);

        PillReminderService pillReminderService = (PillReminderService) getApplicationContext().getBean("pillreminderServiceRef");
        assertNotNull(pillReminderService);

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

    @Override
    protected String[] getConfigLocations() {
        return new String[]{"/META-INF/spring/testPillreminderApiBundleContext.xml"};
    }

    @Override
    protected List<String> getImports() {
        return asList(
                "org.motechproject.server.pillreminder.api.service","org.motechproject.osgi.web"
        );
    }
}
