package org.motechproject.reports.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.reports.builder.excel.model.Workbook;
import org.motechproject.reports.model.ReportDataSource;

import java.util.List;

public class PagedReportBuilder {

    private Workbook workbook;
    private ReportDataSource reportDataSource;

    public PagedReportBuilder(ReportDataSource reportDataSource) {
        this.reportDataSource = reportDataSource;
        workbook = new Workbook(reportDataSource.title(), reportDataSource.columnHeaders());
    }

    public HSSFWorkbook build() {
        boolean doneBuilding = false;
        int pageNumber = 1;
        while (!doneBuilding) {
            List<Object> data = reportDataSource.data(pageNumber);
            if (data != null && !data.isEmpty()) {
                for (Object datum : data) {
                    workbook.addRow(createRowData(datum));
                }
                pageNumber++;
            } else {
                doneBuilding = true;
            }
        }
        return workbook.book();
    }

    private List<String> createRowData(Object model) {
        return reportDataSource.createRowData(model);
    }
}
