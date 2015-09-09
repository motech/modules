package org.motechproject.pillreminder.builder;

import org.junit.Test;
import org.motechproject.pillreminder.event.EventKeys;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SchedulerPayloadBuilderTest {

    @Test
    public void shouldBuildASchedulerPayload() {
        Map payload = new SchedulerPayloadBuilder()
                .withDosageId(10L)
                .payload();
        assertEquals(payload.get(EventKeys.DOSAGE_ID_KEY), 10L);
    }
}
