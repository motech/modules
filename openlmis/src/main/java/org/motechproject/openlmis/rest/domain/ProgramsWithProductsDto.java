package org.motechproject.openlmis.rest.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A class to model the Open LMIS API's ProgramsWithProducts resource.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramsWithProductsDto {
    
    private List<ProgramsWithProductDto> programsWithProducts; //(ProgramWithProduct, optional),

    public List<ProgramsWithProductDto> getProgramsWithProducts() {
        return programsWithProducts;
    }

    public void setProgramsWithProducts(List<ProgramsWithProductDto> programsWithProducts) {
        this.programsWithProducts = programsWithProducts;
    }

    @Override
    public int hashCode() {
        return Objects.hash(programsWithProducts.toArray());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProgramsWithProductsDto other = (ProgramsWithProductsDto) obj;
        return Objects.equals(this.programsWithProducts, other.programsWithProducts);
    }
}
