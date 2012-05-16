package org.motechproject.reports.controller;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.reports.controller.sample.SampleReportController;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@ContextConfiguration(locations = "classpath*:/applicationReportsContext.xml")
public class ReportControllerTest extends SpringIntegrationTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletOutputStream outputStream;
    @Autowired
    ReportController reportController;
    @Autowired
    SampleReportController sampleReportController;

    @Before
    public void setup() throws IOException {
        initMocks(this);
        when(response.getOutputStream()).thenReturn(outputStream);
        sampleReportController.isCalled = false;
    }

    @Test
    public void shouldIdentifyAppropriateExcelController() {
        assertFalse(sampleReportController.isCalled);
        reportController.createReport("sampleReports", "sampleReport", response);
        assertTrue(sampleReportController.isCalled);
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }

}
