package org.motechproject.openlmis.rest.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's ProgramsWithProduct resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramsWithProductDto {
    
    private String programName; //(string, optional),
    private String programCode; //(string, optional),
    private List<ProductDto> products; //
    
    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    @Override
    public int hashCode() {
        return Objects.hash(programName, programCode, products);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProgramsWithProductDto other = (ProgramsWithProductDto) obj;
        return Objects.equals(this.programName, other.programName)
                && Objects.equals(this.programCode, other.programCode)
                && Objects.equals(this.products, other.products);
    }
}
