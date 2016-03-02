package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's StockStatus resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockStatusDto {
    
    private Integer reportedMonth; //(integer, optional),
    private String reportPeriodName; //(string, optional),
    private String facilityName; //(string, optional),
    private String productCode; //(string, optional),
    private Integer reportYear; //(integer, optional),
    private Integer reportQuarter; //(integer, optional),
    private Integer amc; //(integer, optional),
    private String programCode; //(string, optional),
    private Integer reportedYear; //(integer, optional),
    private String product; //(string, optional),
    private String reportedDate; //(string, optional),
    private Integer mos; //(integer, optional),
    private String district; //(string, optional),
    private Integer reportMonth; //(integer, optional),
    private String programName; //(string, optional)

    public Integer getReportedMonth() {
        return reportedMonth;
    }

    public void setReportedMonth(Integer reportedMonth) {
        this.reportedMonth = reportedMonth;
    }

    public String getReportPeriodName() {
        return reportPeriodName;
    }

    public void setReportPeriodName(String reportPeriodName) {
        this.reportPeriodName = reportPeriodName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getReportYear() {
        return reportYear;
    }

    public void setReportYear(Integer reportYear) {
        this.reportYear = reportYear;
    }

    public Integer getReportQuarter() {
        return reportQuarter;
    }

    public void setReportQuarter(Integer reportQuarter) {
        this.reportQuarter = reportQuarter;
    }

    public Integer getAmc() {
        return amc;
    }

    public void setAmc(Integer amc) {
        this.amc = amc;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public Integer getReportedYear() {
        return reportedYear;
    }

    public void setReportedYear(Integer reportedYear) {
        this.reportedYear = reportedYear;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(String reportedDate) {
        this.reportedDate = reportedDate;
    }

    public Integer getMos() {
        return mos;
    }

    public void setMos(Integer mos) {
        this.mos = mos;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getReportMonth() {
        return reportMonth;
    }

    public void setReportMonth(Integer reportMonth) {
        this.reportMonth = reportMonth;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportedMonth, reportPeriodName, facilityName, 
                productCode, reportYear, reportQuarter, amc, programCode, 
                reportedYear, product, reportedDate, mos, district, 
                reportMonth, programName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StockStatusDto other = (StockStatusDto) obj;
        return equalsReport(other) && equalsPeriod(other)
                && equalsProgramProduct(other)
                && Objects.equals(this.facilityName, other.facilityName)
                && Objects.equals(this.amc, other.amc)
                && Objects.equals(this.mos, other.mos)
                && Objects.equals(this.district, other.district);
    }
    
    private boolean equalsReport(StockStatusDto other) {
        return Objects.equals(this.reportPeriodName, other.reportPeriodName)
                && Objects.equals(this.reportYear, other.reportYear)
                && Objects.equals(this.reportQuarter, other.reportQuarter)
                && Objects.equals(this.reportMonth, other.reportMonth);
    }
    
    private boolean equalsPeriod(StockStatusDto other) {
        return Objects.equals(this.reportedYear, other.reportedYear)
                && Objects.equals(this.reportedMonth, other.reportedMonth)
                && Objects.equals(this.reportedDate, other.reportedDate);
    }
    
    private boolean equalsProgramProduct(StockStatusDto other) {
        return Objects.equals(this.programName, other.programName)
                && Objects.equals(this.programCode, other.programCode)
                && Objects.equals(this.productCode, other.productCode)
                && Objects.equals(this.product, other.product);
    }
}
