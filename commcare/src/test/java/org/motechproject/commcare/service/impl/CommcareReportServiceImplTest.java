package org.motechproject.commcare.service.impl;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareMetadataInfo;
import org.motechproject.commcare.domain.report.ReportMetadataColumn;
import org.motechproject.commcare.domain.report.ReportMetadataFilter;
import org.motechproject.commcare.domain.report.ReportMetadataInfo;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.domain.report.ReportDataInfo;
import org.motechproject.commcare.domain.report.ReportDataColumn;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommcareReportServiceImplTest {

    private static final String REPORTS_LIST_METADATA_RESPONSE = "json/service/reportsListMetadataResponse.json";
    private static final String REPORT_DATA_RESPONSE = "json/service/reportDataResponse.json";
    private static final String REPORT_ID = "reportId";
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
        ReportsMetadataInfo reportsMetadataInfo = prepareReportsMetadataInfo();

        when(commcareHttpClient.reportsListMetadataRequest(configService.getByName(null).getAccountConfig())).thenReturn(getResponseForReportsListMetadata());

        ReportsMetadataInfo fetched = reportService.getReportsList();

        assertEquals(reportsMetadataInfo, fetched);
    }

    @Test
    public void shouldGetReportById() {
        ReportDataInfo reportDataInfo = prepareReportDataInfo();

        when(commcareHttpClient.singleReportDataRequest(config.getAccountConfig(), REPORT_ID)).thenReturn(getResponseForReportData());

        ReportDataInfo fetched = reportService.getReportById(REPORT_ID);

        assertEquals(reportDataInfo, fetched);

    }

    private String getResponseForReportsListMetadata() {
        return getRawJson(REPORTS_LIST_METADATA_RESPONSE);
    }

    private String getResponseForReportData() {
        return getRawJson(REPORT_DATA_RESPONSE);
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

    private ReportsMetadataInfo prepareReportsMetadataInfo() {
        List<ReportMetadataInfo> reportMetadataInfoList = new ArrayList<>();

        List<ReportMetadataColumn> columns = new ArrayList<>();
        columns.add(new ReportMetadataColumn("name", "Name", ColumnType.FIELD));
        columns.add(new ReportMetadataColumn("gender", "Gender", ColumnType.EXPANDED));
        columns.add(new ReportMetadataColumn("address", "Person Address", ColumnType.FIELD));

        List<ReportMetadataFilter> filters = new ArrayList<>();
        filters.add(new ReportMetadataFilter(FilterDataType.STRING, "closed", FilterType.CHOICE_LIST));
        filters.add(new ReportMetadataFilter(FilterDataType.STRING, "owner_name", FilterType.CHOICE_LIST));

        reportMetadataInfoList.add(new ReportMetadataInfo("9aab0eeb88555a7b3bc8676883e7379a", "Test Report 1", columns, filters));

        columns = new ArrayList<>();
        columns.add(new ReportMetadataColumn("district", "District", ColumnType.FIELD));
        columns.add(new ReportMetadataColumn("number_of_children_visited", "Num Children Visited", ColumnType.FIELD));
        columns.add(new ReportMetadataColumn("number_of_children_underweight", "Underweight", ColumnType.FIELD));

        filters = new ArrayList<>();
        filters.add(new ReportMetadataFilter(FilterDataType.STRING, "closed", FilterType.CHOICE_LIST));
        filters.add(new ReportMetadataFilter(FilterDataType.STRING, "owner_name", FilterType.CHOICE_LIST));
        filters.add(new ReportMetadataFilter(FilterDataType.INTEGER, "child_age", FilterType.DYNAMIC_CHOICE_LIST));
        filters.add(new ReportMetadataFilter(FilterDataType.STRING, "form_date", FilterType.DATE));

        reportMetadataInfoList.add(new ReportMetadataInfo("9aab0eeb88555a7b4568676883e7379a", "Test Report 2", columns, filters));

        CommcareMetadataInfo metadataInfo = new CommcareMetadataInfo(0, null, 0, null, 2);

        return new ReportsMetadataInfo(reportMetadataInfoList, metadataInfo);
    }

    private ReportDataInfo prepareReportDataInfo() {
        List<ReportDataColumn> columns = new ArrayList<>();
        columns.add(new ReportDataColumn("District", "district", null));
        columns.add(new ReportDataColumn("Num Children Visited", "number_of_children_visited", null));
        columns.add(new ReportDataColumn("Gender-male", "gender-male", "male"));
        columns.add(new ReportDataColumn("Gender-female", "gender-female", "female"));

        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> singleData = new HashMap<>();
        singleData.put("district", "Middlesex");
        singleData.put("number_of_children_visited", "46");
        singleData.put("gender-male", "10");
        singleData.put("gender-female", "35");
        data.add(singleData);

        singleData = new HashMap<>();
        singleData.put("district", "Suffolk");
        singleData.put("number_of_children_visited", "85");
        singleData.put("gender-male", "81");
        singleData.put("gender-female", "4");
        data.add(singleData);

        return new ReportDataInfo(columns, data, "/a/domainOne/api/v0.5/configurablereportdata/9aab0eeb88555a7b4568676883e7379a/?offset=3&limit=3&state=vermont", 30);
    }
}
