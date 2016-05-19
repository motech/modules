package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's Facility resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacilityDto {
    
    private String goDownDate; //(string, optional),
    private Boolean satellite; //(boolean, optional),
    private Boolean hasElectronicDar; //(boolean, optional),
    private Integer geographicZoneId; //(integer, optional),
    private Boolean hasElectronicScc; //(boolean, optional),
    private Double altitude; //(number, optional),
    private Integer operatedById; //(integer, optional),
    private String address1; //(string, optional),
    private String address2; //(string, optional),
    private Double coldStorageGrossCapacity; //(number, optional),
    private Boolean online; //(boolean, optional),
    private Integer id; //(integer, optional),
    private String gln; //(string, optional),
    private Boolean hasElectricity; //(boolean, optional),
    private String description; //(string, optional),
    private String name; //(string, optional),
    private Double longitude; //(number, optional),
    private Boolean sdp; //(boolean, optional),
    private String fax; //(string, optional),
    private Integer satelliteParentId; //(integer, optional),
    private Double coldStorageNetCapacity; //(number, optional),
    private String code; //(string, optional),
    private Boolean dataReportable; //(boolean, optional),
    private Boolean suppliesOthers; //(boolean, optional),
    private String mainPhone; //(string, optional),
    private String goLiveDate; //(string, optional),
    private Boolean active; //(boolean, optional),
    private Double latitude; //(number, optional),
    private String comment; //(string, optional),
    private Integer catchmentPopulation; //(integer, optional),
    private Integer typeId; //(integer, optional)
    
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

    public Boolean getHasElectronicDar() {
        return hasElectronicDar;
    }

    public void setHasElectronicDar(Boolean hasElectronicDar) {
        this.hasElectronicDar = hasElectronicDar;
    }

    public Integer getGeographicZoneId() {
        return geographicZoneId;
    }

    public void setGeographicZoneId(Integer geographicZoneId) {
        this.geographicZoneId = geographicZoneId;
    }

    public Boolean getHasElectronicScc() {
        return hasElectronicScc;
    }

    public void setHasElectronicScc(Boolean hasElectronicScc) {
        this.hasElectronicScc = hasElectronicScc;
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

    public Double getColdStorageGrossCapacity() {
        return coldStorageGrossCapacity;
    }

    public void setColdStorageGrossCapacity(Double coldStorageGrossCapacity) {
        this.coldStorageGrossCapacity = coldStorageGrossCapacity;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGln() {
        return gln;
    }

    public void setGln(String gln) {
        this.gln = gln;
    }

    public Boolean getHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(Boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getSdp() {
        return sdp;
    }

    public void setSdp(Boolean sdp) {
        this.sdp = sdp;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getSatelliteParentId() {
        return satelliteParentId;
    }

    public void setSatelliteParentId(Integer satelliteParentId) {
        this.satelliteParentId = satelliteParentId;
    }

    public Double getColdStorageNetCapacity() {
        return coldStorageNetCapacity;
    }

    public void setColdStorageNetCapacity(Double coldStorageNetCapacity) {
        this.coldStorageNetCapacity = coldStorageNetCapacity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getDataReportable() {
        return dataReportable;
    }

    public void setDataReportable(Boolean dataReportable) {
        this.dataReportable = dataReportable;
    }

    public Boolean getSuppliesOthers() {
        return suppliesOthers;
    }

    public void setSuppliesOthers(Boolean suppliesOthers) {
        this.suppliesOthers = suppliesOthers;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
        this.mainPhone = mainPhone;
    }

    public String getGoLiveDate() {
        return goLiveDate;
    }

    public void setGoLiveDate(String goLiveDate) {
        this.goLiveDate = goLiveDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCatchmentPopulation() {
        return catchmentPopulation;
    }

    public void setCatchmentPopulation(Integer catchmentPopulation) {
        this.catchmentPopulation = catchmentPopulation;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(goDownDate, satellite, hasElectronicDar, geographicZoneId, 
                hasElectronicScc, altitude, operatedById, address1, 
                address2, coldStorageGrossCapacity, online, id, gln,
                hasElectricity, description, name, longitude, 
                sdp, fax, satelliteParentId, coldStorageNetCapacity, 
                code, dataReportable, suppliesOthers, mainPhone, 
                goLiveDate, active, latitude, comment, 
                catchmentPopulation, typeId); 
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FacilityDto other = (FacilityDto) obj;
        return equalsLocationSatelliteBasicDetailsStatus(other)
                && equalsCapacity(other) && equalsContact(other)
                && equalsOperationalDetails(other)
                && equalsBooleanAttributes(other);
    }
    
    private boolean equalsLocationSatelliteBasicDetailsStatus(FacilityDto other) {
        return equalsLocation(other) && equalsSatelliteDetails(other)
                && equalsBasicDetails(other) && equalsStatus(other);
    }
    
    private boolean equalsLocation(FacilityDto other) {
        return Objects.equals(this.address1, other.address1)
                && Objects.equals(this.address2, other.address2)
                && Objects.equals(this.latitude, other.latitude)
                && Objects.equals(this.longitude, other.longitude);
    }

    private boolean equalsBasicDetails(FacilityDto other) {
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.id, other.id)
                && Objects.equals(this.code, other.code);
    }

    private boolean equalsSatelliteDetails(FacilityDto other) {
        return Objects.equals(this.satellite, other.satellite)
                && Objects.equals(this.satelliteParentId, other.satelliteParentId)
                && Objects.equals(this.geographicZoneId, other.geographicZoneId)
                && Objects.equals(this.altitude, other.altitude);
    }
    
    private boolean equalsStatus(FacilityDto other) {
        return Objects.equals(this.goDownDate, other.goDownDate)
                && Objects.equals(this.goLiveDate, other.goLiveDate)
                && Objects.equals(this.active, other.active)
                && Objects.equals(this.online, other.online);
    }

    private boolean equalsCapacity(FacilityDto other) {
        return Objects.equals(this.coldStorageGrossCapacity, other.coldStorageGrossCapacity)
                && Objects.equals(this.coldStorageNetCapacity, other.coldStorageNetCapacity)
                && Objects.equals(this.dataReportable, other.dataReportable)
                && Objects.equals(this.suppliesOthers, other.suppliesOthers);
    }
    
    private boolean equalsContact(FacilityDto other) {
        return Objects.equals(this.mainPhone, other.mainPhone)
                && Objects.equals(this.fax, other.fax)
                && Objects.equals(this.sdp, other.sdp)
                && Objects.equals(this.gln, other.gln);
    }
    
    private boolean equalsOperationalDetails(FacilityDto other) {
        return Objects.equals(this.operatedById, other.operatedById)
                && Objects.equals(this.catchmentPopulation, other.catchmentPopulation)
                && Objects.equals(this.typeId, other.typeId)
                && Objects.equals(this.comment, other.comment);
    }

    private boolean equalsBooleanAttributes(FacilityDto other) {
        return Objects.equals(this.hasElectronicDar, other.hasElectronicDar)
                && Objects.equals(this.hasElectronicScc, other.hasElectronicScc)
                && Objects.equals(this.hasElectricity, other.hasElectricity);
    }
}
