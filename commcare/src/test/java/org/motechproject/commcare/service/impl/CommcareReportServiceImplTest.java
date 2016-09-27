package org.motechproject.commcare.service.impl;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.domain.report.constants.ColumnType;
import org.motechproject.commcare.domain.report.constants.FilterDataType;
import org.motechproject.commcare.domain.report.constants.FilterType;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommcareReportServiceImplTest {

    private static final String REPORTS_LIST_METADATA_RESPONSE = "json/service/reportsListMetadataResponse.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareCaseServiceImplTest.class);
    private CommcareReportServiceImpl reportService;

    @Mock
    private CommCareAPIHttpClient commcareHttpClient;

    @Mock
    private CommcareConfigService configService;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);

        config = ConfigsUtils.prepareConfigOne();

        when(configService.getByName(null)).thenReturn(config);

        reportService = new CommcareReportServiceImpl(commcareHttpClient, configService);
    }

    @Test
    public void shouldGetReports() {
        when(commcareHttpClient.reportsListMetadataRequest(configService.getByName(null).getAccountConfig())).thenReturn(getResponseForReportsListMetadata());

        ReportsMetadataInfo reportsMetadataInfo = reportService.getReportsList();

        assertThat(reportsMetadataInfo.getMetadataInfo().getTotalCount(), equalTo(2));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().size(), equalTo(2));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getColumns().size(), equalTo(3));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getFilters().size(), equalTo(2));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getId(), equalTo("9aab0eeb88555a7b3bc8676883e7379a"));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getTitle(), equalTo("Test Report 1"));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getColumns().get(0).getId(), equalTo("name"));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getColumns().get(0).getDisplay(), equalTo("Name"));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getColumns().get(0).getType(), equalTo(ColumnType.FIELD));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getFilters().get(0).getType(), equalTo(FilterType.CHOICE_LIST));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getFilters().get(0).getDatatype(), equalTo(FilterDataType.STRING));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getFilters().get(0).getSlug(), equalTo("closed"));
    }

    private String getResponseForReportsListMetadata() {
        return getRawJson(REPORTS_LIST_METADATA_RESPONSE);
    }

    private String getRawJson(String path) {
        try {
            URL url = this.getClass().getClassLoader().getResource(path);
            return FileUtils.readFileToString(new File(url.getFile()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}