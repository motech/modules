package org.motechproject.commcare.web;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.pull.CaseImportStatus;
import org.motechproject.commcare.pull.CommcareCaseImporter;
import org.motechproject.commcare.pull.CommcareCaseImporterFactory;
import org.motechproject.commcare.util.CommcareParamHelper;
import org.motechproject.commons.api.Range;
import org.motechproject.commons.date.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CaseImportControllerTest {

    @InjectMocks
    private CaseImportController caseImportController = new CaseImportController();

    @Mock
    private CommcareCaseImporterFactory importerFactory;

    @Mock
    private CommcareCaseImporter caseImporter;

    private MockMvc controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        when(importerFactory.getImporter(any(HttpSession.class))).thenReturn(caseImporter);
        controller = MockMvcBuilders.standaloneSetup(caseImportController).build();
    }

    @Test
    public void shouldAllowCheckingImportStatus() throws Exception {
        CaseImportStatus status = new CaseImportStatus();
        status.setImportInProgress(true);
        status.setCasesImported(200);
        status.setTotalCases(600);
        status.setError(false);
        status.setLastImportDate(CommcareParamHelper.printDateTime(DateUtil.now()));

        when(caseImporter.importStatus()).thenReturn(status);

        controller.perform(get("/case-import/status"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().type("application/json;charset=UTF-8"))
                .andExpect(content().string(objectMapper.writeValueAsString(status)));
    }

    @Test
    public void shouldAllowImportInit() throws Exception {
        when(caseImporter.countForImport(any(Range.class), eq("configName"))).thenReturn(1672);

        controller.perform(post("/case-import/init").body(requestJson().getBytes(Charset.forName("UTF-8")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().type("application/json;charset=UTF-8"))
                .andExpect(content().string("1672"));

        ArgumentCaptor<Range> captor = ArgumentCaptor.forClass(Range.class);
        verify(caseImporter).countForImport(captor.capture(), eq("configName"));
        verifyDateRange(captor.getValue());
    }

    @Test
    public void shouldStartImport() throws Exception {
        controller.perform(post("/case-import/start").body(requestJson().getBytes(Charset.forName("UTF-8")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<Range> captor = ArgumentCaptor.forClass(Range.class);
        verify(caseImporter).startImport(captor.capture(), eq("configName"));
        verifyDateRange(captor.getValue());
    }

    @Test
    public void shouldStartImportByCaseId() throws Exception {
        controller.perform(post("/case-import/import-by-id").body(requestJsonWithCaseId().getBytes(Charset.forName("UTF-8")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(caseImporter).importSingleCase(captor.capture(), eq("configName"));
    }

    private String requestJson() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/web/case-import-request.json")) {
            return IOUtils.toString(in);
        }
    }

    private String requestJsonWithCaseId() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/web/case-import-request-caseid.json")) {
            return IOUtils.toString(in);
        }
    }

    private void verifyDateRange(Range<DateTime> dtRange) {
        assertNotNull(dtRange);
        assertEquals(new DateTime(2015, 3, 22, 17, 45, 35, 0, DateTimeZone.UTC), dtRange.getMin());
        assertEquals(new DateTime(2015, 11, 10, 8, 16, 21, 0, DateTimeZone.UTC), dtRange.getMax());
    }
}
