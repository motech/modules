package org.motechproject.commcare.service.impl;

import com.google.common.reflect.TypeToken;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.domain.CommcareMetadataInfo;
import org.motechproject.commcare.domain.CommcareMetadataJson;
import org.motechproject.commcare.domain.report.ReportInfo;
import org.motechproject.commcare.domain.report.ReportsInfo;
import org.motechproject.commcare.domain.report.ReportJson;
import org.motechproject.commcare.domain.report.ReportsResponseJson;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareReportService;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link CommcareReportService} that is responsible for interacting with CommCareHQ's Report Metadata and Data API.
 */
@Service
public class CommcareReportServiceImpl implements CommcareReportService {

    private CommCareAPIHttpClient commCareHttpClient;
    private CommcareConfigService configService;
    private MotechJsonReader motechJsonReader;

    @Autowired
    public CommcareReportServiceImpl(CommCareAPIHttpClient commCareHttpClient, CommcareConfigService configService) {
        this.commCareHttpClient = commCareHttpClient;
        this.configService = configService;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public ReportsInfo getReportsList(String configName) {
        String response = commCareHttpClient.reportsListRequest(configService.getByName(configName).getAccountConfig());

        ReportsResponseJson reportsResponseJson = parseReportsFromResponse(response);

        return new ReportsInfo(generateReportsFromReportsResponse(reportsResponseJson.getReports()), populateReportsMetadata(reportsResponseJson.getMetadata()));
    }

    @Override
    public ReportsInfo getReportsList() {
        return getReportsList(null);
    }

    private ReportsResponseJson parseReportsFromResponse(String response) {
        Type reportsResponseType = new TypeToken<ReportsResponseJson>() { } .getType();
        return (ReportsResponseJson) motechJsonReader.readFromString(response, reportsResponseType);
    }

    private List<ReportInfo> generateReportsFromReportsResponse(List<ReportJson> reportResponses) {
        if (reportResponses == null) {
            return Collections.emptyList();
        }

        List<ReportInfo> reportsInfoList = new ArrayList<>();

        for (ReportJson reportResponse : reportResponses) {
            reportsInfoList.add(populateReportInfo(reportResponse));
        }

        return reportsInfoList;
    }

    private ReportInfo populateReportInfo(ReportJson reportResponse) {
        if (reportResponse == null) {
            return null;
        }

        ReportInfo reportInfo = new ReportInfo();

        reportInfo.setId(reportResponse.getId());
        reportInfo.setTitle(reportResponse.getTitle());
        reportInfo.setColumns(reportResponse.getColumns());
        reportInfo.setFilters(reportResponse.getFilters());

        return reportInfo;
    }

    private CommcareMetadataInfo populateReportsMetadata(CommcareMetadataJson metadataJson) {
        CommcareMetadataInfo metadataInfo = new CommcareMetadataInfo();

        metadataInfo.setLimit(metadataJson.getLimit());
        metadataInfo.setNextPageQueryString(metadataJson.getNextPageQueryString());
        metadataInfo.setOffset(metadataJson.getOffset());
        metadataInfo.setPreviousPageQueryString(metadataJson.getPreviousPageQueryString());
        metadataInfo.setTotalCount(metadataJson.getTotalCount());

        return metadataInfo;
    }
}