package org.motechproject.reports.builder;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.reports.builder.excel.Workbook;

import java.util.List;

public abstract class ReportBuilder<T> {

    private Workbook workbook;

    protected ReportBuilder(String title, List<String> columnHeaders) {
        workbook = new Workbook(title, columnHeaders);
    }

    public HSSFWorkbook build() {
        List<T> data = data();
        if (data != null) {
            for (T datum : data) {
                workbook.addRow(createRowData(datum));
            }
        }
        return workbook.book();
    }

    protected abstract List<String> createRowData(T modal);

    protected abstract List<T> data();
}
