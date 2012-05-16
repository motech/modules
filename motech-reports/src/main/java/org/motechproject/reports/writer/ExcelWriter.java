package org.motechproject.reports.writer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.reports.builder.excel.PagedReportBuilder;
import org.motechproject.reports.model.ReportDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExcelWriter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void writeExcelToResponse(HttpServletResponse response, ReportDataSource reportDataSource, String reportName, String fileName) {
        try {
            initializeExcelResponse(response, fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            HSSFWorkbook excelWorkbook = createExcelWorkBook(reportDataSource, reportName);
            if (null != excelWorkbook)
                excelWorkbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("Error while writing excel report to response: " + e.getMessage());
        }
    }


    private HSSFWorkbook createExcelWorkBook(ReportDataSource reportDataSource, String reportName) {
        try {
            return new PagedReportBuilder(reportDataSource, reportName).build();
        } catch (Exception e) {
            logger.error("Error while generating excel report: " + e.getMessage());
            return null;
        }
    }


    private void initializeExcelResponse(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        response.setContentType("application/vnd.ms-excel");
    }
}
