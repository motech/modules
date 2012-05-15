package org.motechproject.reports.builder.excel.model;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.motechproject.reports.builder.excel.model.ExcelColumn;
import org.motechproject.reports.builder.excel.model.MotechCellStyle;

import java.util.ArrayList;
import java.util.List;

public class Worksheet {

    public static final int TITLE_ROW_HEIGHT = 500;
    public static final int MAX_ROW_INDEX = 29999;
    public static final int HEADER_ROW_COUNT = 2;
    public static final int HEADER_ROW_HEIGHT = 500;
    public static final int FIRST_COLUMN = 0;

    private int currentRowIndex = 0;
    private List<String> columnHeaders;
    HSSFSheet sheet;

    public Worksheet(HSSFWorkbook workbook, String sheetName, String title, List<String> columnHeaders) {
        sheet = workbook.createSheet(sheetName);
        initializeLayout(title, columnHeaders);
        sheet.createFreezePane(0, currentRowIndex);
    }

    public boolean addRow(List<String> rowData) {
        if (dataRowIndex() > lastDataRowIndex()) {
            return false;
        } else {
            HSSFRow row = sheet.createRow(currentRowIndex);
            for (int i = 0; i < rowData.size(); i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(rowData.get(i));
            }
            currentRowIndex++;
        }
        return true;
    }

    private int dataRowIndex() {
        return currentRowIndex - HEADER_ROW_COUNT;
    }

    private int lastDataRowIndex() {
        return MAX_ROW_INDEX - HEADER_ROW_COUNT;
    }

    protected void initializeLayout(String title, List<String> columnHeaders) {
        buildTitle(title, columnHeaders.size());
        buildHeader(columnHeaders);
    }

    private void buildTitle(String title, int width) {
        HSSFRow rowTitle = sheet.createRow((short) currentRowIndex);
        rowTitle.setHeight((short) TITLE_ROW_HEIGHT);

        HSSFCell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue(title);
        cellTitle.setCellStyle(new MotechCellStyle(sheet, CellStyle.ALIGN_CENTER).style());

        sheet.addMergedRegion(new CellRangeAddress(currentRowIndex, currentRowIndex, FIRST_COLUMN, width));
        currentRowIndex++;
    }

    private void buildHeader(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
        createHeaderRow();
    }

    private void createHeaderRow() {
        HSSFCellStyle headerCellStyle = new MotechCellStyle(sheet).style();

        HSSFRow headerRow = sheet.createRow((short) currentRowIndex);
        headerRow.setHeight((short) HEADER_ROW_HEIGHT);

        int columnIndex = 0;
        for (ExcelColumn column : columnHeaders()) {
            HSSFCell cell = headerRow.createCell(columnIndex);
            cell.setCellValue(column.getHeader());
            cell.setCellStyle(headerCellStyle);
            columnIndex++;
        }
        currentRowIndex++;
    }


    private List<ExcelColumn> columnHeaders() {
        List<ExcelColumn> columns = new ArrayList<ExcelColumn>();
        for (String header : columnHeaders) {
            columns.add(new ExcelColumn(header, Cell.CELL_TYPE_STRING, 8000));
        }
        setColumnWidths(columns);
        return columns;
    }

    private void setColumnWidths(List<ExcelColumn> columns) {
        int columnIndex = 0;
        for (ExcelColumn column : columns) {
            sheet.setColumnWidth(columnIndex, column.getWidth());
            columnIndex++;
        }
    }
}
