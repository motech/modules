package org.motechproject.reports.builder;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class PagedReportBuilderTest {

    public static class TestReportBuilder extends PagedReportBuilder<String> {

        protected TestReportBuilder() {
            super("Test", asList("Column 1", "Column 2"));
        }

        @Override
        protected List<String> createRowData(String modal) {
            return asList(modal, modal);
        }

        @Override
        protected List<String> data(int pageNumber) {
            if (pageNumber == 1)
                return asList("Row1", "Row2");
            else if (pageNumber == 2)
                return asList("Row3", "Row4");
            else
                return null;
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
    public void shouldPageRowData() {
        HSSFWorkbook workbook = new TestReportBuilder().build();
        assertDataInFirstPageAdded(workbook);
        assertDataInSecondPageAdded(workbook);
    }

    private void assertDataInFirstPageAdded(HSSFWorkbook workbook) {
        assertEquals("Row1", workbook.getSheetAt(0).getRow(2).getCell(0).getStringCellValue());
        assertEquals("Row2", workbook.getSheetAt(0).getRow(3).getCell(0).getStringCellValue());
    }

    private void assertDataInSecondPageAdded(HSSFWorkbook workbook) {
        assertEquals("Row3", workbook.getSheetAt(0).getRow(4).getCell(0).getStringCellValue());
        assertEquals("Row4", workbook.getSheetAt(0).getRow(5).getCell(0).getStringCellValue());
    }
}
