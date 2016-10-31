package org.motechproject.commcare.tasks.builder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commcare.util.DummyCommcareSchema;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commcare.util.DummyCommcareSchema.REPORT_COLUMN1_VALUE;
import static org.motechproject.commcare.util.DummyCommcareSchema.REPORT_COLUMN4_VALUE;
import static org.motechproject.commcare.util.DummyCommcareSchema.REPORT_COLUMN5_VALUE;

public class ReportTriggerBuilderTest {

    @Mock
    private CommcareSchemaService schemaService;

    @Mock
    private CommcareConfigService configService;

    private Configs configs = ConfigsUtils.prepareConfigsWithThreeConfigs();

    private ReportTriggerBuilder reportTriggerBuilder;

    private static final String FIRST_REPORT_NAME = "reportOne";
    private static final String SECOND_REPORT_NAME = "reportTwo";

    private static final String BASE_SUBJECT_ONE = EventSubjects.RECEIVED_REPORT + ".ConfigOne.";
    private static final String BASE_SUBJECT_TWO = EventSubjects.RECEIVED_REPORT + ".ConfigTwo.";

    @Before
    public void setUp() {
        initMocks(this);
        when(configService.getConfigs()).thenReturn(configs);
        when(schemaService.getReportsMetadata("ConfigOne")).thenReturn(DummyCommcareSchema.getReportsMetadataForConfigOne());
        when(schemaService.getReportsMetadata("ConfigTwo")).thenReturn(DummyCommcareSchema.getReportsMetadataForConfigTwo());

        reportTriggerBuilder = new ReportTriggerBuilder(schemaService, configService);
    }

    @Test
    public void shouldBuildProperTriggerRequestForReports() {
        List<TriggerEventRequest> triggers = reportTriggerBuilder.buildTriggers();

        assertFalse(triggers.isEmpty());

        int counter = 0;
        for (ReportsMetadataInfo reports : DummyCommcareSchema.getReportsMetadataForConfigOne()) {
            counter += reports.getReportMetadataInfoList().size();
        }

        for (ReportsMetadataInfo reports : DummyCommcareSchema.getReportsMetadataForConfigTwo()) {
            counter += reports.getReportMetadataInfoList().size();
        }

        assertEquals(counter, triggers.size());

        for (TriggerEventRequest request : triggers) {
            String subject = request.getSubject();

            switch (subject) {
                case BASE_SUBJECT_ONE + DummyCommcareSchema.REPORT_ID1:
                    assertEquals("Received Report: Test Report 1 [ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), REPORT_COLUMN1_VALUE));
                    break;
                case BASE_SUBJECT_ONE + DummyCommcareSchema.REPORT_ID2:
                    assertEquals("Received Report: Test Report 2 [ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), REPORT_COLUMN4_VALUE));
                    break;
                case BASE_SUBJECT_TWO + DummyCommcareSchema.REPORT_ID1:
                    assertEquals("Received Report: Test Report 1 [ConfigTwo]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), REPORT_COLUMN5_VALUE));
                    break;
                default:
                    fail("Found trigger with incorrect subject: " + subject);
            }
        }
    }

    private boolean hasEventKey(List<EventParameterRequest> eventParameters, String key) {
        for (EventParameterRequest parameter : eventParameters) {
            if (parameter.getEventKey().equals(key)) {
                return true;
            }
        }

        return false;
    }
}
