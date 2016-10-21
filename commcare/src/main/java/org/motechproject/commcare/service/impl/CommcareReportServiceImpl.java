package org.motechproject.commcare.service.impl;

import com.google.common.reflect.TypeToken;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.domain.CommcareMetadataInfo;
import org.motechproject.commcare.domain.CommcareMetadataJson;
import org.motechproject.commcare.domain.report.ReportMetadataInfo;
import org.motechproject.commcare.domain.report.ReportDataInfo;
import org.motechproject.commcare.domain.report.ReportDataResponseJson;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.domain.report.ReportMetadataJson;
import org.motechproject.commcare.domain.report.ReportsMetadataResponseJson;
import org.motechproject.commcare.domain.report.constants.ColumnType;
import org.motechproject.commcare.domain.report.constants.FilterDataType;
import org.motechproject.commcare.domain.report.constants.FilterType;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareReportService;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * A {@link CommcareReportService} that is responsible for interacting with CommCareHQ's Report Metadata and Data API.
 */
@Service
public class CommcareReportServiceImpl implements CommcareReportService {

    private CommCareAPIHttpClient commcareHttpClient;
    private CommcareConfigService configService;
    private MotechJsonReader motechJsonReader;

    @Autowired
    public CommcareReportServiceImpl(CommCareAPIHttpClient commcareHttpClient, CommcareConfigService configService) {
        this.commcareHttpClient = commcareHttpClient;
        this.configService = configService;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public ReportsMetadataInfo getReportsList(String configName) {
        String response = commcareHttpClient.reportsListMetadataRequest(configService.getByName(configName).getAccountConfig());

        ReportsMetadataResponseJson reportsMetadataResponseJson = parseReportsFromResponse(response);

        return new ReportsMetadataInfo(generateReportsFromReportsResponse(reportsMetadataResponseJson.getReports()), populateReportsMetadata(reportsMetadataResponseJson.getMetadata()));
    }

    @Override
    public ReportsMetadataInfo getReportsList() {
        return getReportsList(null);
    }

    @Override
    public ReportDataInfo getReportById(String reportId, String configName) {
        String response = commcareHttpClient.singleReportDataRequest(configService.getByName(configName).getAccountConfig(),
                reportId);

        ReportDataResponseJson reportResponse = parseSingleReportFromResponse(response);

        return generateReportFromReportResponse(reportResponse);
    }

    @Override
    public ReportDataInfo getReportByIdWithFilters (String reportId, String configName, String filters) {
        String response = commcareHttpClient.singleReportDataRequestWithFilters(configService.getByName(configName).getAccountConfig(),
                reportId, filters);
        ReportDataResponseJson reportResponse = parseSingleReportFromResponse(response);

        return generateReportFromReportResponse(reportResponse);
    }

    @Override
    public ReportDataInfo getReportById(String reportId) {
        return getReportById(reportId, null);
    }

    private ReportsMetadataResponseJson parseReportsFromResponse(String response) {
        Type reportsResponseType = new TypeToken<ReportsMetadataResponseJson>() { } .getType();
        Map<Type, Object> adapters = new HashMap<>();
        adapters.put(ColumnType.class, new ColumnType.ColumnTypeDeserializer());
        adapters.put(FilterType.class, new FilterType.FilterTypeDeserializer());
        adapters.put(FilterDataType.class, new FilterDataType.FilterDataTypeDeserializer());
        return (ReportsMetadataResponseJson) motechJsonReader.readFromString(response, reportsResponseType, adapters);
    }

    private ReportDataResponseJson parseSingleReportFromResponse(String response) {
        Type reportResponseType = new TypeToken<ReportDataResponseJson>() { } .getType();
        return (ReportDataResponseJson) motechJsonReader.readFromString(response, reportResponseType);
    }

    private ReportDataInfo generateReportFromReportResponse(ReportDataResponseJson reportResponse) {
        return populateReportDataInfo(reportResponse);
    }

    private List<ReportMetadataInfo> generateReportsFromReportsResponse(List<ReportMetadataJson> reportResponses) {
        List<ReportMetadataInfo> reportsInfoList = Collections.emptyList();

        if (reportResponses != null) {
            reportsInfoList = new ArrayList<>();
            for (ReportMetadataJson reportResponse : reportResponses) {
                reportsInfoList.add(populateReportMetadataInfo(reportResponse));
            }
        }

        return reportsInfoList;
    }

    private ReportMetadataInfo populateReportMetadataInfo(ReportMetadataJson reportResponse) {
        ReportMetadataInfo reportMetadataInfo = null;

        if (reportResponse != null) {
            reportMetadataInfo = new ReportMetadataInfo(reportResponse.getId(), reportResponse.getTitle(),
                    reportResponse.getColumns(), reportResponse.getFilters());
        }

        return reportMetadataInfo;
    }

    private ReportDataInfo populateReportDataInfo(ReportDataResponseJson reportResponse) {
        ReportDataInfo reportDataInfo = null;

        if (reportResponse != null) {
            reportDataInfo = new ReportDataInfo(reportResponse.getColumns(), reportResponse.getData(),
                    reportResponse.getNextPage(), reportResponse.getTotalRecords());
        }

        return reportDataInfo;
    }

    private CommcareMetadataInfo populateReportsMetadata(CommcareMetadataJson metadataJson) {
        return new CommcareMetadataInfo(metadataJson.getLimit(), metadataJson.getNextPageQueryString(),
                metadataJson.getOffset(), metadataJson.getPreviousPageQueryString(), metadataJson.getTotalCount());
    }
}
