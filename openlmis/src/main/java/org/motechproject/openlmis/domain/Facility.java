package org.motechproject.openlmis.domain;

import javax.jdo.annotations.Unique;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

/**
 * Represents a OpenLMIS Facility
 */

@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureOpenlmis" })
public class Facility {
    
    @Field(required = true)
    @Unique
    private Integer openlmisid;

    @Field(required = true)
    @Unique
    private String code;
    
    @Field
    private String name;
    
    @Field
    private String description;
    
    @Field
    private String gln;
    
    @Field
    private String mainPhone;
    
    @Field
    private String fax;
    
    @Field
    private String address1;

    @Field
    private String address2;
    
    @Field
    private GeographicZone geographicZone;
    
    @Field
    private FacilityType type;
    
    @Field
    private Integer catchmentPopulation;

    @Field
    private Double latitude;
    
    @Field
    private Double longitude;
    
    @Field
    private Double altitude;

    @Field
    private Integer operatedById;
    
    @Field
    private Double coldStorageGrossCapacity;

    @Field
    private Double coldStorageNetCapacity;
    
    @Field
    private Boolean suppliesOthers;

    @Field
    private Boolean sdp;

    @Field
    private Boolean hasElectricity;

    @Field
    private Boolean online;
    
    @Field
    private Boolean hasElectronicScc;

    @Field
    private Boolean hasElectronicDar;

    @Field
    private Boolean active;

    @Field
    private String goLiveDate;
        
    @Field
    private String goDownDate;

    @Field
    private Boolean satellite;

    @Field
    private String comment;

    @Field
    private Integer satelliteParentId;

    @Field
    private Boolean dataReportable;

    public Integer getOpenlmisid() {
        return openlmisid;
    }

    public void setOpenlmisid(Integer openlmisid) {
        this.openlmisid = openlmisid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGln() {
        return gln;
    }

    public void setGln(String gln) {
        this.gln = gln;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
        this.mainPhone = mainPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public GeographicZone getGeographicZone() {
        return geographicZone;
    }

    public void setGeographicZone(GeographicZone geographicZone) {
        this.geographicZone = geographicZone;
    }

    public FacilityType getType() {
        return type;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }

    public Integer getCatchmentPopulation() {
        return catchmentPopulation;
    }

    public void setCatchmentPopulation(Integer catchmentPopulation) {
        this.catchmentPopulation = catchmentPopulation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Integer getOperatedById() {
        return operatedById;
    }

    public void setOperatedById(Integer operatedById) {
        this.operatedById = operatedById;
    }

    public Double getColdStorageGrossCapacity() {
        return coldStorageGrossCapacity;
    }

    public void setColdStorageGrossCapacity(Double coldStorageGrossCapacity) {
        this.coldStorageGrossCapacity = coldStorageGrossCapacity;
    }

    public Double getColdStorageNetCapacity() {
        return coldStorageNetCapacity;
    }

    public void setColdStorageNetCapacity(Double coldStorageNetCapacity) {
        this.coldStorageNetCapacity = coldStorageNetCapacity;
    }

    public Boolean getSuppliesOthers() {
        return suppliesOthers;
    }

    public void setSuppliesOthers(Boolean suppliesOthers) {
        this.suppliesOthers = suppliesOthers;
    }

    public Boolean getSdp() {
        return sdp;
    }

    public void setSdp(Boolean sdp) {
        this.sdp = sdp;
    }

    public Boolean getHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(Boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getHasElectronicScc() {
        return hasElectronicScc;
    }

    public void setHasElectronicScc(Boolean hasElectronicScc) {
        this.hasElectronicScc = hasElectronicScc;
    }

    public Boolean getHasElectronicDar() {
        return hasElectronicDar;
    }

    public void setHasElectronicDar(Boolean hasElectronicDar) {
        this.hasElectronicDar = hasElectronicDar;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getGoLiveDate() {
        return goLiveDate;
    }

    public void setGoLiveDate(String goLiveDate) {
        this.goLiveDate = goLiveDate;
    }

    public String getGoDownDate() {
        return goDownDate;
    }

    public void setGoDownDate(String goDownDate) {
        this.goDownDate = goDownDate;
    }

    public Boolean getSatellite() {
        return satellite;
    }

    public void setSatellite(Boolean satellite) {
        this.satellite = satellite;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getSatelliteParentId() {
        return satelliteParentId;
    }

    public void setSatelliteParentId(Integer satelliteParentId) {
        this.satelliteParentId = satelliteParentId;
    }

    public Boolean getDataReportable() {
        return dataReportable;
    }

    public void setDataReportable(Boolean dataReportable) {
        this.dataReportable = dataReportable;
    }

}
