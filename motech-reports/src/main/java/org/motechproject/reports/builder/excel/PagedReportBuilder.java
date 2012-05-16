package org.motechproject.reports.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.reports.builder.excel.model.Workbook;
import org.motechproject.reports.model.ReportDataSource;

import java.util.List;

public class PagedReportBuilder {

    private Workbook workbook;

    private ReportDataSource reportDataSource;
    private String reportName;

    public PagedReportBuilder(ReportDataSource reportDataSource, String reportName) {
        this.reportDataSource = reportDataSource;
        this.reportName = reportName;
        workbook = new Workbook(reportDataSource.title(), reportDataSource.columnHeaders(reportName));
    }

    public HSSFWorkbook build() {
        boolean doneBuilding = false;
        int pageNumber = 1;
        while (!doneBuilding) {
            List<Object> data = reportDataSource.data(reportName, pageNumber);
            if (data != null && !data.isEmpty()) {
                for (Object datum : data) {
                    workbook.addRow(reportDataSource.rowData(reportName, datum));
                }
                pageNumber++;
            } else {
                doneBuilding = true;
            }
        }
        return workbook.book();
    }

}
