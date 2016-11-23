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
import org.motechproject.commcare.pull.CommcareFormImporter;
import org.motechproject.commcare.pull.CommcareFormImporterFactory;
import org.motechproject.commcare.pull.FormImportStatus;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class FormImportControllerTest {

    @InjectMocks
    private FormImportController formImportController = new FormImportController();

    @Mock
    private CommcareFormImporterFactory importerFactory;

    @Mock
    private CommcareFormImporter formImporter;

    private MockMvc controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        when(importerFactory.getImporter(any(HttpSession.class))).thenReturn(formImporter);
        controller = MockMvcBuilders.standaloneSetup(formImportController).build();
    }

    @Test
    public void shouldAllowCheckingImportStatus() throws Exception {
        FormImportStatus status = new FormImportStatus();
        status.setImportInProgress(true);
        status.setLastImportDate(CommcareParamHelper.printDateTime(DateUtil.now()));
        status.setFormsImported(500);
        status.setTotalForms(1600);
        status.setError(false);
        status.setErrorMsg(null);

        when(formImporter.importStatus()).thenReturn(status);

        controller.perform(get("/form-import/status"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().type("application/json;charset=UTF-8"))
                .andExpect(content().string(objectMapper.writeValueAsString(status)));
    }

    @Test
    public void shouldAllowImportInit() throws Exception {
        when(formImporter.countForImport(any(Range.class), eq("configName"))).thenReturn(1672);

        controller.perform(post("/form-import/init").body(requestJson().getBytes(Charset.forName("UTF-8")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().type("application/json;charset=UTF-8"))
                .andExpect(content().string("1672"));

        ArgumentCaptor<Range> captor = ArgumentCaptor.forClass(Range.class);
        verify(formImporter).countForImport(captor.capture(), eq("configName"));
        verifyDateRange(captor.getValue());
    }

    @Test
    public void shouldAllowImportInitByFormId() throws Exception {
        when(formImporter.checkFormIdForImport(any(String.class), eq("configName"))).thenReturn(true);

        controller.perform(post("/form-import/init").body(requestJsonForFormId().getBytes(Charset.forName("UTF-8")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().type("application/json;charset=UTF-8"))
                .andExpect(content().string("1"));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(formImporter).checkFormIdForImport(captor.capture(), eq("configName"));
    }

    @Test
    public void shouldStartImport() throws Exception {
        controller.perform(post("/form-import/start").body(requestJson().getBytes(Charset.forName("UTF-8")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<Range> captor = ArgumentCaptor.forClass(Range.class);
        verify(formImporter).startImport(captor.capture(), eq("configName"));
        verifyDateRange(captor.getValue());
    }

    @Test
    public void shouldStartImportByFormId() throws Exception {
        controller.perform(post("/form-import/start").body(requestJsonForFormId().getBytes(Charset.forName("UTF-8")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(formImporter).startImportById(captor.capture(), eq("configName"));
    }

    private void verifyDateRange(Range<DateTime> dtRange) {
        assertNotNull(dtRange);
        assertEquals(new DateTime(2012, 9, 29, 17, 24, 52, 0, DateTimeZone.UTC), dtRange.getMin());
        assertEquals(new DateTime(2013, 10, 29, 8, 24, 52, 0, DateTimeZone.UTC), dtRange.getMax());
    }

    private String requestJson() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/web/import-request.json")) {
            return IOUtils.toString(in);
        }
    }

    private String requestJsonForFormId() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/web/import-request-form-id.json")) {
            return IOUtils.toString(in);
        }
    }
}
