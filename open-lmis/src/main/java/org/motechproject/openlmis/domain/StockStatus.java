package org.motechproject.openlmis.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

/**
 * Represents a OpenLMIS StockStatus
 */

@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureOpenlmis" })
public class StockStatus {
    
    @Field
    private DateTime reportedDate;
    
    @Field
    private Integer reportedMonth;
    
    @Field
    private Integer reportedYear;
    
    @Field
    private String reportPeriodName;
    
    @Field
    private Facility facility;
    
    @Field
    private Integer reportMonth;
    
    @Field
    private Integer reportYear;
    
    @Field
    private Integer reportQuarter;
    
    @Field
    private Integer amc;
    
    @Field
    private Integer mos;
    
    @Field
    private String district;
    
    @Field
    private Program program;
    
    @Field
    private Product product;

    public DateTime getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(DateTime reportedDate) {
        this.reportedDate = reportedDate;
    }

    public Integer getReportedMonth() {
        return reportedMonth;
    }

    public void setReportedMonth(Integer reportedMonth) {
        this.reportedMonth = reportedMonth;
    }

    public Integer getReportedYear() {
        return reportedYear;
    }

    public void setReportedYear(Integer reportedYear) {
        this.reportedYear = reportedYear;
    }

    public String getReportPeriodName() {
        return reportPeriodName;
    }

    public void setReportPeriodName(String reportPeriodName) {
        this.reportPeriodName = reportPeriodName;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Integer getReportMonth() {
        return reportMonth;
    }

    public void setReportMonth(Integer reportMonth) {
        this.reportMonth = reportMonth;
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

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


}
