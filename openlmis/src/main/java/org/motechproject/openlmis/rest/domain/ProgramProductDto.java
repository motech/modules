package org.motechproject.openlmis.rest.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's ProgramProduct resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramProductDto {
    
    private ProductDto product; //(Product, optional),
    private Integer id; //(integer, optional),
    private ProgramProductISADto programProductIsa; //(ProgramProductISA, optional),
    private Boolean fullSupply; //(boolean, optional),
    private ProgramDto program; //(Program, optional),
    private ProductCategoryDto productCategory; //(ProductCategory, optional),
    private Integer dosesPerMonth; //(integer, optional),
    private Integer displayOrder; //(integer, optional),
    private Boolean active; //(boolean, optional),
    private MoneyDto currentPrice; //(Money, optional),
    private Integer productCategoryId; //(integer, optional)
    
    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProgramProductISADto getProgramProductIsa() {
        return programProductIsa;
    }

    public void setProgramProductIsa(ProgramProductISADto programProductIsa) {
        this.programProductIsa = programProductIsa;
    }

    public Boolean getFullSupply() {
        return fullSupply;
    }

    public void setFullSupply(Boolean fullSupply) {
        this.fullSupply = fullSupply;
    }

    public ProgramDto getProgram() {
        return program;
    }

    public void setProgram(ProgramDto program) {
        this.program = program;
    }

    public ProductCategoryDto getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryDto productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getDosesPerMonth() {
        return dosesPerMonth;
    }

    public void setDosesPerMonth(Integer dosesPerMonth) {
        this.dosesPerMonth = dosesPerMonth;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public MoneyDto getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(MoneyDto currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, id, programProductIsa, fullSupply, 
                program, productCategory, dosesPerMonth, displayOrder, 
                active, currentPrice, productCategoryId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProgramProductDto other = (ProgramProductDto) obj;
        return equalsProduct(other) && equalsDetails(other)
                && Objects.equals(this.programProductIsa, other.programProductIsa)
                && Objects.equals(this.dosesPerMonth, other.dosesPerMonth)
                && Objects.equals(this.displayOrder, other.displayOrder)
                && Objects.equals(this.currentPrice, other.currentPrice);
    }
    
    private boolean equalsProduct(ProgramProductDto other) {
        return Objects.equals(this.product, other.product)
                && Objects.equals(this.productCategory, other.productCategory)
                && Objects.equals(this.productCategoryId, other.productCategoryId);
    }
    
    private boolean equalsDetails(ProgramProductDto other) {
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.program, other.program)
                && Objects.equals(this.active, other.active)
                && Objects.equals(this.fullSupply, other.fullSupply);
    }
}
