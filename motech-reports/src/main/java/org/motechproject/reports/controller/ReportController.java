package org.motechproject.reports.controller;

import org.motechproject.reports.model.AllReportDataSources;
import org.motechproject.reports.model.ReportDataSource;
import org.motechproject.reports.writer.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@RequestMapping(value = "/reports")
@Controller
public class ReportController {

    private AllReportDataSources allReportDataSources;
    private ExcelWriter excelWriter;

    @Autowired
    public ReportController(AllReportDataSources allReportDataSources, ExcelWriter excelWriter) {
        this.allReportDataSources = allReportDataSources;
        this.excelWriter = excelWriter;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{reportName}.xls")
    public void createReport(@PathVariable("reportName") String reportName, HttpServletResponse response) {
        ReportDataSource reportDataSource = allReportDataSources.get(reportName);
        excelWriter.writeExcelToResponse(response, reportDataSource, reportName + ".xls");
    }
}
