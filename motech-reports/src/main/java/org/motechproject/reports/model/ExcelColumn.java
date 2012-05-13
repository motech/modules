package org.motechproject.reports.model;

import org.apache.poi.ss.usermodel.CellStyle;

public class ExcelColumn {

    public static final int BASIC_COLUMN_WIDTH = 4000;

    public static final int DEFAULT_TEXT_ALIGNMENT = CellStyle.ALIGN_CENTER;

    private String header;
    private int width;
    private int cellType;
    private int textAlignment;

    public ExcelColumn(String header, int cellType, int width, int textAlignment) {
        this.header = header;
        this.cellType = cellType;
        this.width = width;
        this.textAlignment = textAlignment;
    }

    public ExcelColumn(String header, int cellType, int columnWidth) {
        this(header, cellType, columnWidth, DEFAULT_TEXT_ALIGNMENT);
    }

    public ExcelColumn(String header, int cellType) {
        this(header, cellType, BASIC_COLUMN_WIDTH, DEFAULT_TEXT_ALIGNMENT);
    }

    public String getHeader() {
        return header;
    }

    public int getWidth() {
        return width;
    }

    public int getCellType() {
        return cellType;
    }

    public int getTextAlignment() {
        return textAlignment;
    }
}
