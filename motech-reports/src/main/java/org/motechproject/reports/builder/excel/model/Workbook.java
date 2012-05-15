package org.motechproject.reports.builder.excel.model;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public class Workbook {

    private final HSSFWorkbook workbook;
    private Worksheet worksheet;
    private String title;
    private List<String> columnHeaders;
    private int worksheetNumber;

    public Workbook(String title, List<String> columnHeaders) {
        this.title = title;
        this.columnHeaders = columnHeaders;

        workbook = new HSSFWorkbook();

        worksheetNumber = 0;
        worksheet = newWorksheet();
    }

    public void addRow(List<String> rowData) {
        if (!worksheet.addRow(rowData)) {
            worksheet = newWorksheet();
            worksheet.addRow(rowData);
        }
    }

    public HSSFWorkbook book() {
        return workbook;
    }

    private Worksheet newWorksheet() {
        return new Worksheet(workbook, "Worksheet" + (worksheetNumber++), title, columnHeaders);
    }
}
