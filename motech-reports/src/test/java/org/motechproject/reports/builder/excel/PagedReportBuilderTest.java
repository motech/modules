package org.motechproject.reports.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.motechproject.reports.controller.sample.SampleReportController;
import org.motechproject.reports.model.ReportDataSource;

import static org.junit.Assert.assertEquals;

public class PagedReportBuilderTest {

    @Test
    public void shouldAddTitle() {
        HSSFWorkbook workbook = new PagedReportBuilder(new ReportDataSource(new SampleReportController()), "sampleReport").build();
        assertEquals("Sample Reports", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
    }

    @Test
    public void shouldAddColumnHeaders() {
        HSSFWorkbook workbook = new PagedReportBuilder(new ReportDataSource(new SampleReportController()), "sampleReport").build();
        assertEquals("Id", workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
    }

    @Test
    public void shouldPageRowData() {
        HSSFWorkbook workbook = new PagedReportBuilder(new ReportDataSource(new SampleReportController()), "sampleReport").build();
        assertDataInFirstPageAdded(workbook);
        assertDataInSecondPageAdded(workbook);
    }

    private void assertDataInFirstPageAdded(HSSFWorkbook workbook) {
        assertEquals("id1", workbook.getSheetAt(0).getRow(2).getCell(0).getStringCellValue());
        assertEquals("id2", workbook.getSheetAt(0).getRow(3).getCell(0).getStringCellValue());
    }

    private void assertDataInSecondPageAdded(HSSFWorkbook workbook) {
        assertEquals("id3", workbook.getSheetAt(0).getRow(4).getCell(0).getStringCellValue());
    }
}
