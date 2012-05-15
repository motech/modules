package org.motechproject.reports.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.motechproject.reports.builder.excel.ReportBuilder;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class ReportBuilderTest {

    public static class TestReportBuilder extends ReportBuilder<String> {

        protected TestReportBuilder() {
            super("Test", asList("Column 1", "Column 2"));
        }

        @Override
        protected List<String> createRowData(String modal) {
            return asList(modal, modal);
        }

        @Override
        protected List<String> data() {
            return asList("Row1", "Row2");
        }
    }

    @Test
    public void shouldAddTitle() {
        HSSFWorkbook workbook = new TestReportBuilder().build();
        assertEquals("Test", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
    }

    @Test
    public void shouldAddColumnHeaders() {
        HSSFWorkbook workbook = new TestReportBuilder().build();
        assertEquals("Column 1", workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
        assertEquals("Column 2", workbook.getSheetAt(0).getRow(1).getCell(1).getStringCellValue());
    }

    @Test
    public void shouldAddRowData() {
        HSSFWorkbook workbook = new TestReportBuilder().build();
        assertEquals("Row1", workbook.getSheetAt(0).getRow(2).getCell(0).getStringCellValue());
        assertEquals("Row2", workbook.getSheetAt(0).getRow(3).getCell(0).getStringCellValue());
    }
}
