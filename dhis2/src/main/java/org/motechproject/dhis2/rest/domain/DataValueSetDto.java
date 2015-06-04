package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


/**
 * A class to model a DHIS2 Data Value Set
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataValueSetDto {

    private String completeDate;
    private String period;
    private String orgUnit;
    private String attributeOptionCombo;
    private List<DataValueDto> dataValues;
    private String dataSet;
    private String comment;
    private String categoryOptionCombo;

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public String getAttributeOptionCombo() {
        return attributeOptionCombo;
    }

    public void setAttributeOptionCombo(String attributeOptionCombo) {
        this.attributeOptionCombo = attributeOptionCombo;
    }

    public List<DataValueDto> getDataValues() {
        return dataValues;
    }

    public void setDataValues(List<DataValueDto> dataValues) {
        this.dataValues = dataValues;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCategoryOptionCombo() {
        return categoryOptionCombo;
    }

    public void setCategoryOptionCombo(String categoryOptionCombo) {
        this.categoryOptionCombo = categoryOptionCombo;
    }
}
