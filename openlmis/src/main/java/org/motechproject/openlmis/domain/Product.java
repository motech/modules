package org.motechproject.openlmis.domain;

import javax.jdo.annotations.Unique;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

/**
 * Represents a OpenLMIS Product
 */

@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"configureOpenlmis" })
public class Product {

    @Field
    @Unique
    private Integer openlmisid;

    @Field
    @Unique
    private String code;
    
    @Field
    private String alternateItemCode;
    
    @Field
    private String manufacturer;
    
    @Field
    private String manufacturerCode;
    
    @Field
    private String manufacturerBarCode;
    
    @Field
    private String mohBarCode;
    
    @Field
    private String gtin;
    
    @Field
    private String type;
    
    @Field
    private String primaryName;
    
    @Field
    private String fullName;
    
    @Field
    private String genericName;
    
    @Field
    private String alternateName;
    
    @Field
    private String description;
    
    @Field
    private String strength;
    
    @Field
    private DosageUnit dosageUnit;
    
    @Field
    private String dispensingUnit;
    
    @Field
    private Integer dosesPerDispensingUnit;
    
    @Field
    private Boolean storeRefrigerated;
    
    @Field
    private Boolean storeRoomTemperature;
    
    @Field
    private Boolean hazardous;
    
    @Field
    private Boolean flammable;
    
    @Field
    private Boolean controlledSubstance;
    
    @Field
    private Boolean lightSensitive;
    
    @Field
    private Boolean approvedByWHO;
    
    @Field
    private Double contraceptiveCYP;
    
    @Field
    private Integer packSize;
    
    @Field
    private Integer alternatePackSize;
    
    @Field
    private Double packLength;
    
    @Field
    private Double packWidth;
    
    @Field
    private Double packHeight;
    
    @Field
    private Double packWeight;
    
    @Field
    private Integer packsPerCarton;
    
    @Field
    private Double cartonLength;
    
    @Field
    private Double cartonWidth;
    
    @Field
    private Double cartonHeight;
    
    @Field
    private Integer cartonsPerPallet;
    
    @Field
    private Integer expectedShelfLife;
    
    @Field
    private String specialStorageInstructions;
    
    @Field
    private String specialTransportInstructions;
    
    @Field
    private Boolean active;
    
    @Field
    private Boolean fullSupply;
    
    @Field
    private Boolean tracer;
    
    @Field
    private Integer packRoundingThreshold;
    
    @Field
    private Boolean roundToZero;
    
    @Field
    private Boolean archived;
    
    @Field
    private String formId;
    
    @Field
    private Boolean isKit;
    
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

    public String getAlternateItemCode() {
        return alternateItemCode;
    }

    public void setAlternateItemCode(String alternateItemCode) {
        this.alternateItemCode = alternateItemCode;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public String getManufacturerBarCode() {
        return manufacturerBarCode;
    }

    public void setManufacturerBarCode(String manufacturerBarCode) {
        this.manufacturerBarCode = manufacturerBarCode;
    }

    public String getMohBarCode() {
        return mohBarCode;
    }

    public void setMohBarCode(String mohBarCode) {
        this.mohBarCode = mohBarCode;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public DosageUnit getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(DosageUnit dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public String getDispensingUnit() {
        return dispensingUnit;
    }

    public void setDispensingUnit(String dispensingUnit) {
        this.dispensingUnit = dispensingUnit;
    }

    public Integer getDosesPerDispensingUnit() {
        return dosesPerDispensingUnit;
    }

    public void setDosesPerDispensingUnit(Integer dosesPerDispensingUnit) {
        this.dosesPerDispensingUnit = dosesPerDispensingUnit;
    }

    public Boolean getStoreRefrigerated() {
        return storeRefrigerated;
    }

    public void setStoreRefrigerated(Boolean storeRefrigerated) {
        this.storeRefrigerated = storeRefrigerated;
    }

    public Boolean getStoreRoomTemperature() {
        return storeRoomTemperature;
    }

    public void setStoreRoomTemperature(Boolean storeRoomTemperature) {
        this.storeRoomTemperature = storeRoomTemperature;
    }

    public Boolean getHazardous() {
        return hazardous;
    }

    public void setHazardous(Boolean hazardous) {
        this.hazardous = hazardous;
    }

    public Boolean getFlammable() {
        return flammable;
    }

    public void setFlammable(Boolean flammable) {
        this.flammable = flammable;
    }

    public Boolean getControlledSubstance() {
        return controlledSubstance;
    }

    public void setControlledSubstance(Boolean controlledSubstance) {
        this.controlledSubstance = controlledSubstance;
    }

    public Boolean getLightSensitive() {
        return lightSensitive;
    }

    public void setLightSensitive(Boolean lightSensitive) {
        this.lightSensitive = lightSensitive;
    }

    public Boolean getApprovedByWHO() {
        return approvedByWHO;
    }

    public void setApprovedByWHO(Boolean approvedByWHO) {
        this.approvedByWHO = approvedByWHO;
    }

    public Double getContraceptiveCYP() {
        return contraceptiveCYP;
    }

    public void setContraceptiveCYP(Double contraceptiveCYP) {
        this.contraceptiveCYP = contraceptiveCYP;
    }

    public Integer getPackSize() {
        return packSize;
    }

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }

    public Integer getAlternatePackSize() {
        return alternatePackSize;
    }

    public void setAlternatePackSize(Integer alternatePackSize) {
        this.alternatePackSize = alternatePackSize;
    }

    public Double getPackLength() {
        return packLength;
    }

    public void setPackLength(Double packLength) {
        this.packLength = packLength;
    }

    public Double getPackWidth() {
        return packWidth;
    }

    public void setPackWidth(Double packWidth) {
        this.packWidth = packWidth;
    }

    public Double getPackHeight() {
        return packHeight;
    }

    public void setPackHeight(Double packHeight) {
        this.packHeight = packHeight;
    }

    public Double getPackWeight() {
        return packWeight;
    }

    public void setPackWeight(Double packWeight) {
        this.packWeight = packWeight;
    }

    public Integer getPacksPerCarton() {
        return packsPerCarton;
    }

    public void setPacksPerCarton(Integer packsPerCarton) {
        this.packsPerCarton = packsPerCarton;
    }

    public Double getCartonLength() {
        return cartonLength;
    }

    public void setCartonLength(Double cartonLength) {
        this.cartonLength = cartonLength;
    }

    public Double getCartonWidth() {
        return cartonWidth;
    }

    public void setCartonWidth(Double cartonWidth) {
        this.cartonWidth = cartonWidth;
    }

    public Double getCartonHeight() {
        return cartonHeight;
    }

    public void setCartonHeight(Double cartonHeight) {
        this.cartonHeight = cartonHeight;
    }

    public Integer getCartonsPerPallet() {
        return cartonsPerPallet;
    }

    public void setCartonsPerPallet(Integer cartonsPerPallet) {
        this.cartonsPerPallet = cartonsPerPallet;
    }

    public Integer getExpectedShelfLife() {
        return expectedShelfLife;
    }

    public void setExpectedShelfLife(Integer expectedShelfLife) {
        this.expectedShelfLife = expectedShelfLife;
    }

    public String getSpecialStorageInstructions() {
        return specialStorageInstructions;
    }

    public void setSpecialStorageInstructions(String specialStorageInstructions) {
        this.specialStorageInstructions = specialStorageInstructions;
    }

    public String getSpecialTransportInstructions() {
        return specialTransportInstructions;
    }

    public void setSpecialTransportInstructions(String specialTransportInstructions) {
        this.specialTransportInstructions = specialTransportInstructions;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getFullSupply() {
        return fullSupply;
    }

    public void setFullSupply(Boolean fullSupply) {
        this.fullSupply = fullSupply;
    }

    public Boolean getTracer() {
        return tracer;
    }

    public void setTracer(Boolean tracer) {
        this.tracer = tracer;
    }

    public Integer getPackRoundingThreshold() {
        return packRoundingThreshold;
    }

    public void setPackRoundingThreshold(Integer packRoundingThreshold) {
        this.packRoundingThreshold = packRoundingThreshold;
    }

    public Boolean getRoundToZero() {
        return roundToZero;
    }

    public void setRoundToZero(Boolean roundToZero) {
        this.roundToZero = roundToZero;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Boolean getIsKit() {
        return isKit;
    }

    public void setIsKit(Boolean isKit) {
        this.isKit = isKit;
    }

}
