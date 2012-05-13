package org.motechproject.reports.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;


public class MotechCellStyle {

    public static final int TITLE_FONT_HEIGHT = 280;

    private HSSFSheet worksheet;
    private short alignment;

    public MotechCellStyle(HSSFSheet worksheet) {
        this(worksheet, CellStyle.ALIGN_LEFT);
    }

    public MotechCellStyle(HSSFSheet worksheet, short alignment) {
        this.worksheet = worksheet;
        this.alignment = alignment;
    }

    public HSSFCellStyle style() {
        Font font = worksheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) TITLE_FONT_HEIGHT);

        HSSFCellStyle cellStyle = worksheet.getWorkbook().createCellStyle();

        cellStyle.setAlignment(alignment);
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        return cellStyle;
    }
}
