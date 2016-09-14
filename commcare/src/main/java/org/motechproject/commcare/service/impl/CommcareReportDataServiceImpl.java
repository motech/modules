package org.motechproject.commcare.service.impl;

import com.google.gson.reflect.TypeToken;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.domain.report.ReportDataContainerJson;
import org.motechproject.commcare.domain.report.ReportDataInfo;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareReportDataService;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;


@Service
public class CommcareReportDataServiceImpl implements CommcareReportDataService {

    private CommCareAPIHttpClient commcareHttpClient;
    private CommcareConfigService configService;
    private MotechJsonReader motechJsonReader;

    @Autowired
    public CommcareReportDataServiceImpl(CommCareAPIHttpClient commcareHttpClient, CommcareConfigService configService){
        this.commcareHttpClient = commcareHttpClient;
        this.configService = configService;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public ReportDataInfo getReportByReportId(String reportId, String configName) {
        String response = commcareHttpClient.singleReportDataRequest(configService.getByName(configName).getAccountConfig(),
                reportId);

        ReportDataContainerJson reportResponse = parseSingleReportFromResponse(response);

        return generateReportFromReportResponse(reportResponse);
    }

    @Override
    public ReportDataInfo getReportByReportId(String reportId) {
        return getReportByReportId(reportId, null);
    }

    private ReportDataContainerJson parseSingleReportFromResponse(String response) {
        Type reportResponseType = new TypeToken<ReportDataContainerJson>() { } .getType();
        return (ReportDataContainerJson) motechJsonReader.readFromString(response, reportResponseType);
    }

    private ReportDataInfo generateReportFromReportResponse(ReportDataContainerJson reportResponse) {
        return populateReportDataInfo(reportResponse);
    }

    private ReportDataInfo populateReportDataInfo(ReportDataContainerJson reportResponse) {
        if(reportResponse == null){
            return null;
        }

        ReportDataInfo reportDataInfo = new ReportDataInfo();

        reportDataInfo.setColumns(reportResponse.getColumns());
        reportDataInfo.setData(reportResponse.getData());
        reportDataInfo.setNextPage(reportResponse.getNextPage());
        reportDataInfo.setTotalRecords(reportResponse.getTotalRecords());

        return  reportDataInfo;
    }

}
