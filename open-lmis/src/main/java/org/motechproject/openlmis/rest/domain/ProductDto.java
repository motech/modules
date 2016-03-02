package org.motechproject.openlmis.rest.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's Product resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {

    private Double packWidth; // (number, optional),
    private Double contraceptiveCYP; // (number, optional),
    private DosageUnitDto dosageUnit; // (DosageUnit, optional),
    private String manufacturerCode; // (string, optional),
    private Boolean lightSensitive; // (boolean, optional),
    private String type; // (string, optional),
    private Double cartonLength; // (number, optional),
    private String alternateName; // (string, optional),
    private Integer formId; // (integer, optional),
    private String description; // (string, optional),
    private Boolean hazardous; // (boolean, optional),
    private Integer packsPerCarton; // (integer, optional),
    private Double cartonWidth; // (number, optional),
    private ProductFormDto form; // (ProductForm, optional),
    private String specialTransportInstructions; // (string, optional),
    private String code; // (string, optional),
    private String genericName; // (string, optional),
    private Boolean approvedByWHO; // (boolean, optional),
    private Double cartonHeight; // (number, optional),
    private Integer alternatePackSize; // (integer, optional),
    private String alternateItemCode; // (string, optional),
    private Integer dosesPerDispensingUnit; // (integer, optional),
    private Boolean isKit; // (boolean, optional),
    private Double packLength; // (number, optional),
    private String manufacturerBarCode; // (string, optional),
    private String manufacturer; // (string, optional),
    private List<KitProductDto> kitProductList; // (List[KitProduct], optional),
    private Boolean active; // (boolean, optional),
    private Double packHeight; // (number, optional),
    private Integer packRoundingThreshold; // (integer, optional),
    private String mohBarCode; // (string, optional),
    private Integer dosageUnitId; // (integer, optional),
    private ProductGroupDto productGroup; // (ProductGroup, optional),
    private Boolean tracer; // (boolean, optional),
    private Boolean fullSupply; // (boolean, optional),
    private Boolean roundToZero; // (boolean, optional),
    private String specialStorageInstructions; // (string, optional),
    private Integer productGroupId; // (integer, optional),
    private Boolean flammable; // (boolean, optional),
    private String dispensingUnit; // (string, optional),
    private Integer id; // (integer, optional),
    private Integer expectedShelfLife; // (integer, optional),
    private Boolean archived; // (boolean, optional),
    private Integer packSize; // (integer, optional),
    private Boolean storeRefrigerated; // (boolean, optional),
    private String strength; // (string, optional),
    private String gtin; // (string, optional),
    private Boolean storeRoomTemperature; // (boolean, optional),
    private String primaryName; // (string, optional),
    private Double packWeight; // (number, optional),
    private Boolean controlledSubstance; // (boolean, optional),
    private Integer cartonsPerPallet; // (integer, optional),
    private String fullName; // (string, optional)

    public Double getPackWidth() {
        return packWidth;
    }

    public void setPackWidth(Double packWidth) {
        this.packWidth = packWidth;
    }

    public Double getContraceptiveCYP() {
        return contraceptiveCYP;
    }

    public void setContraceptiveCYP(Double contraceptiveCYP) {
        this.contraceptiveCYP = contraceptiveCYP;
    }

    public DosageUnitDto getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(DosageUnitDto dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public Boolean getLightSensitive() {
        return lightSensitive;
    }

    public void setLightSensitive(Boolean lightSensitive) {
        this.lightSensitive = lightSensitive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getCartonLength() {
        return cartonLength;
    }

    public void setCartonLength(Double cartonLength) {
        this.cartonLength = cartonLength;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHazardous() {
        return hazardous;
    }

    public void setHazardous(Boolean hazardous) {
        this.hazardous = hazardous;
    }

    public Integer getPacksPerCarton() {
        return packsPerCarton;
    }

    public void setPacksPerCarton(Integer packsPerCarton) {
        this.packsPerCarton = packsPerCarton;
    }

    public Double getCartonWidth() {
        return cartonWidth;
    }

    public void setCartonWidth(Double cartonWidth) {
        this.cartonWidth = cartonWidth;
    }

    public ProductFormDto getForm() {
        return form;
    }

    public void setForm(ProductFormDto form) {
        this.form = form;
    }

    public String getSpecialTransportInstructions() {
        return specialTransportInstructions;
    }

    public void setSpecialTransportInstructions(String specialTransportInstructions) {
        this.specialTransportInstructions = specialTransportInstructions;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public Boolean getApprovedByWHO() {
        return approvedByWHO;
    }

    public void setApprovedByWHO(Boolean approvedByWHO) {
        this.approvedByWHO = approvedByWHO;
    }

    public Double getCartonHeight() {
        return cartonHeight;
    }

    public void setCartonHeight(Double cartonHeight) {
        this.cartonHeight = cartonHeight;
    }

    public Integer getAlternatePackSize() {
        return alternatePackSize;
    }

    public void setAlternatePackSize(Integer alternatePackSize) {
        this.alternatePackSize = alternatePackSize;
    }

    public String getAlternateItemCode() {
        return alternateItemCode;
    }

    public void setAlternateItemCode(String alternateItemCode) {
        this.alternateItemCode = alternateItemCode;
    }

    public Integer getDosesPerDispensingUnit() {
        return dosesPerDispensingUnit;
    }

    public void setDosesPerDispensingUnit(Integer dosesPerDispensingUnit) {
        this.dosesPerDispensingUnit = dosesPerDispensingUnit;
    }

    public Boolean getIsKit() {
        return isKit;
    }

    public void setIsKit(Boolean isKit) {
        this.isKit = isKit;
    }

    public Double getPackLength() {
        return packLength;
    }

    public void setPackLength(Double packLength) {
        this.packLength = packLength;
    }

    public String getManufacturerBarCode() {
        return manufacturerBarCode;
    }

    public void setManufacturerBarCode(String manufacturerBarCode) {
        this.manufacturerBarCode = manufacturerBarCode;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<KitProductDto> getKitProductList() {
        return kitProductList;
    }

    public void setKitProductList(List<KitProductDto> kitProductList) {
        this.kitProductList = kitProductList;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getPackHeight() {
        return packHeight;
    }

    public void setPackHeight(Double packHeight) {
        this.packHeight = packHeight;
    }

    public Integer getPackRoundingThreshold() {
        return packRoundingThreshold;
    }

    public void setPackRoundingThreshold(Integer packRoundingThreshold) {
        this.packRoundingThreshold = packRoundingThreshold;
    }

    public String getMohBarCode() {
        return mohBarCode;
    }

    public void setMohBarCode(String mohBarCode) {
        this.mohBarCode = mohBarCode;
    }

    public Integer getDosageUnitId() {
        return dosageUnitId;
    }

    public void setDosageUnitId(Integer dosageUnitId) {
        this.dosageUnitId = dosageUnitId;
    }

    public ProductGroupDto getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroupDto productGroup) {
        this.productGroup = productGroup;
    }

    public Boolean getTracer() {
        return tracer;
    }

    public void setTracer(Boolean tracer) {
        this.tracer = tracer;
    }

    public Boolean getFullSupply() {
        return fullSupply;
    }

    public void setFullSupply(Boolean fullSupply) {
        this.fullSupply = fullSupply;
    }

    public Boolean getRoundToZero() {
        return roundToZero;
    }

    public void setRoundToZero(Boolean roundToZero) {
        this.roundToZero = roundToZero;
    }

    public String getSpecialStorageInstructions() {
        return specialStorageInstructions;
    }

    public void setSpecialStorageInstructions(String specialStorageInstructions) {
        this.specialStorageInstructions = specialStorageInstructions;
    }

    public Integer getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(Integer productGroupId) {
        this.productGroupId = productGroupId;
    }

    public Boolean getFlammable() {
        return flammable;
    }

    public void setFlammable(Boolean flammable) {
        this.flammable = flammable;
    }

    public String getDispensingUnit() {
        return dispensingUnit;
    }

    public void setDispensingUnit(String dispensingUnit) {
        this.dispensingUnit = dispensingUnit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExpectedShelfLife() {
        return expectedShelfLife;
    }

    public void setExpectedShelfLife(Integer expectedShelfLife) {
        this.expectedShelfLife = expectedShelfLife;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Integer getPackSize() {
        return packSize;
    }

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }

    public Boolean getStoreRefrigerated() {
        return storeRefrigerated;
    }

    public void setStoreRefrigerated(Boolean storeRefrigerated) {
        this.storeRefrigerated = storeRefrigerated;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public Boolean getStoreRoomTemperature() {
        return storeRoomTemperature;
    }

    public void setStoreRoomTemperature(Boolean storeRoomTemperature) {
        this.storeRoomTemperature = storeRoomTemperature;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public Double getPackWeight() {
        return packWeight;
    }

    public void setPackWeight(Double packWeight) {
        this.packWeight = packWeight;
    }

    public Boolean getControlledSubstance() {
        return controlledSubstance;
    }

    public void setControlledSubstance(Boolean controlledSubstance) {
        this.controlledSubstance = controlledSubstance;
    }

    public Integer getCartonsPerPallet() {
        return cartonsPerPallet;
    }

    public void setCartonsPerPallet(Integer cartonsPerPallet) {
        this.cartonsPerPallet = cartonsPerPallet;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(packWidth, contraceptiveCYP, dosageUnit, manufacturerCode, lightSensitive, type,
                cartonLength, alternateName, formId, description, hazardous, packsPerCarton, cartonWidth, form,
                specialTransportInstructions, code, genericName, approvedByWHO, cartonHeight, alternatePackSize,
                alternateItemCode, dosesPerDispensingUnit, isKit, packLength, manufacturerBarCode, manufacturer,
                kitProductList, active, packHeight, packRoundingThreshold, mohBarCode, dosageUnitId, productGroup,
                tracer, fullSupply, roundToZero, specialStorageInstructions, productGroupId, flammable, dispensingUnit,
                id, expectedShelfLife, archived, packSize, storeRefrigerated, strength, gtin, storeRoomTemperature,
                primaryName, packWeight, controlledSubstance, cartonsPerPallet, fullName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProductDto other = (ProductDto) obj;
        return equalsDimensionsNameDescription(other) && 
                equalsQuantityCodeAndManufacturer(other) &&
                equalsBooleanAttributes(other) && equalsMappingAndDetails(other);
    }
    
    private boolean equalsDimensionsNameDescription(ProductDto other) {
        return equalsPackDimensions(other) &&
                equalsCartonDimensions(other) &&
                equalsName(other) && equalsDescription(other);
    }
    
    private boolean equalsQuantityCodeAndManufacturer(ProductDto other) {
        return equalsQuantity(other) && equalsCode(other) &&
            equalsManufacturer(other);
    }
    
    private boolean equalsBooleanAttributes(ProductDto other) {
        return equalsBooleanAttributesOne(other)
                && equalsBooleanAttributesTwo(other)
                && equalsBooleanAttributesThree(other)
                && equalsBooleanAttributesFour(other);
    }
    
    private boolean equalsMappingAndDetails(ProductDto other) {
        return equalsMappingDto(other) && equalsMappingIds(other)
                && equalsInstructionDetails(other)
                && equalsNumericalDetails(other);
    }

    private boolean equalsPackDimensions(ProductDto other) {
        return Objects.equals(this.packLength, other.packLength)
                && Objects.equals(this.packWidth, other.packWidth)
                && Objects.equals(this.packHeight, other.packHeight)
                && Objects.equals(this.packWeight, other.packWeight);
    }
    
    private boolean equalsCartonDimensions(ProductDto other) {
        return Objects.equals(this.cartonLength, other.cartonLength)
                && Objects.equals(this.cartonWidth, other.cartonWidth)
                && Objects.equals(this.cartonHeight, other.cartonHeight);
    }

    private boolean equalsName(ProductDto other) {
        return Objects.equals(this.fullName, other.fullName)
                && Objects.equals(this.primaryName, other.primaryName)
                && Objects.equals(this.genericName, other.genericName)
                && Objects.equals(this.alternateName, other.alternateName);
    }
    
    private boolean equalsDescription(ProductDto other) {
        return Objects.equals(this.description, other.description)
                && Objects.equals(this.id, other.id)
                && Objects.equals(this.type, other.type);
    }

    private boolean equalsQuantity(ProductDto other) {
        return Objects.equals(this.packsPerCarton, other.packsPerCarton)
                && Objects.equals(this.packSize, other.packSize)
                && Objects.equals(this.alternatePackSize, other.alternatePackSize)
                && Objects.equals(this.cartonsPerPallet, other.cartonsPerPallet);
    }

    private boolean equalsCode(ProductDto other) {
        return Objects.equals(this.alternateItemCode, other.alternateItemCode)
                && Objects.equals(this.code, other.code)
                && Objects.equals(this.mohBarCode, other.mohBarCode)
                && Objects.equals(this.gtin, other.gtin);
    }
    
    private boolean equalsManufacturer(ProductDto other) {
        return Objects.equals(this.manufacturerCode, other.manufacturerCode)
                && Objects.equals(this.manufacturerBarCode, other.manufacturerBarCode)
                && Objects.equals(this.manufacturer, other.manufacturer)
                && Objects.equals(this.gtin, other.gtin);
    }

    private boolean equalsBooleanAttributesOne(ProductDto other) {
        return Objects.equals(this.lightSensitive, other.lightSensitive) 
                && Objects.equals(this.hazardous, other.hazardous)
                && Objects.equals(this.approvedByWHO, other.approvedByWHO)
                && Objects.equals(this.isKit, other.isKit);
    }
    
    private boolean equalsBooleanAttributesTwo(ProductDto other) {
        return Objects.equals(this.active, other.active)
                && Objects.equals(this.tracer, other.tracer)
                && Objects.equals(this.fullSupply, other.fullSupply);
    }

    private boolean equalsBooleanAttributesThree(ProductDto other) {
        return Objects.equals(this.roundToZero, other.roundToZero)
                && Objects.equals(this.flammable, other.flammable)
                && Objects.equals(this.archived, other.archived);
    }
    
    private boolean equalsBooleanAttributesFour(ProductDto other) {
        return Objects.equals(this.storeRefrigerated, other.storeRefrigerated)
                && Objects.equals(this.storeRoomTemperature, other.storeRoomTemperature)
                && Objects.equals(this.controlledSubstance, other.controlledSubstance);
    }

    private boolean equalsMappingIds(ProductDto other) {
        return Objects.equals(this.dosageUnitId, other.dosageUnitId)
                && Objects.equals(this.productGroupId, other.productGroupId)
                && Objects.equals(this.formId, other.formId); 
    }
    
    private boolean equalsMappingDto(ProductDto other) {
        return Objects.equals(this.dosageUnit, other.dosageUnit)
                && Objects.equals(this.kitProductList, other.kitProductList)
                && Objects.equals(this.productGroup, other.productGroup)
                && Objects.equals(this.form, other.form); 
    }

    private boolean equalsInstructionDetails(ProductDto other) {
        return Objects.equals(this.specialTransportInstructions, other.specialTransportInstructions)
                && Objects.equals(this.specialStorageInstructions, other.specialStorageInstructions)
                && Objects.equals(this.expectedShelfLife, other.expectedShelfLife)
                && Objects.equals(this.strength, other.strength);
    }
    
    private boolean equalsNumericalDetails(ProductDto other) {
        return Objects.equals(this.contraceptiveCYP, other.contraceptiveCYP)
                && Objects.equals(this.packRoundingThreshold, other.packRoundingThreshold)
                && Objects.equals(this.dispensingUnit, other.dispensingUnit)
                && Objects.equals(this.dosesPerDispensingUnit, other.dosesPerDispensingUnit);
    }
}
