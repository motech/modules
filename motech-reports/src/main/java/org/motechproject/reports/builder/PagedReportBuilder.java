package org.motechproject.reports.builder;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.reports.builder.excel.Workbook;

import java.util.List;

public abstract class PagedReportBuilder<T> {

    private Workbook workbook;

    protected PagedReportBuilder(String title, List<String> columnHeaders) {
        workbook = new Workbook(title, columnHeaders);
    }

    public HSSFWorkbook build() {
        boolean doneBuilding = false;
        int pageNumber = 1;
        while (!doneBuilding) {
            List<T> data = data(pageNumber);
            if (data != null && !data.isEmpty()) {
                for (T datum : data) {
                    workbook.addRow(createRowData(datum));
                }
                pageNumber++;
            } else {
                doneBuilding = true;
            }
        }
        return workbook.book();
    }

    protected abstract List<String> createRowData(T modal);

    protected abstract List<T> data(long pageNumber);
}
