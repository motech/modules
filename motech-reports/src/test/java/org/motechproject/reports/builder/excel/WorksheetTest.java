package org.motechproject.reports.builder.excel;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class WorksheetTest {

    @Test
    public void shouldAddRowWhenNumberOfRowsDoesNotExceedLimit() {
        Worksheet worksheet = new Worksheet(new HSSFWorkbook(), "sheet", "Test", asList("Column"));
        boolean addedSheet = true;
        for (int i = 0; i <= maxDataRowIndex(); i++) {
            addedSheet &= worksheet.addRow(asList("test"));
        }
        assertTrue(addedSheet);
    }

    @Test
    public void shouldNotAddRowWhenNumberOfRowsDoesNotExceedLimit() {
        Worksheet worksheet = new Worksheet(new HSSFWorkbook(), "sheet", "Test", asList("Column"));
        for (int i = 0; i <= maxDataRowIndex(); i++) {
            worksheet.addRow(asList("test"));
        }
        assertFalse(worksheet.addRow(asList("test")));
        assertNull(worksheet.sheet.getRow(Worksheet.MAX_ROW_INDEX + 1));
    }

    private int maxDataRowIndex() {
        return Worksheet.MAX_ROW_INDEX - Worksheet.HEADER_ROW_COUNT;
    }
}
