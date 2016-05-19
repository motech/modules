package org.motechproject.openlmis.rest.service;

import java.util.List;

import org.motechproject.openlmis.rest.domain.DosageUnitDto;
import org.motechproject.openlmis.rest.domain.FacilityDto;
import org.motechproject.openlmis.rest.domain.FacilityTypeDto;
import org.motechproject.openlmis.rest.domain.GeographicLevelDto;
import org.motechproject.openlmis.rest.domain.GeographicZoneDto;
import org.motechproject.openlmis.rest.domain.ProductCategoryDto;
import org.motechproject.openlmis.rest.domain.ProductDto;
import org.motechproject.openlmis.rest.domain.ProgramDto;
import org.motechproject.openlmis.rest.domain.StockStatusDto;

public interface OpenlmisWebService {
    
    List<ProgramDto> getPrograms();

    List<ProductCategoryDto> getProductCategories();

    List<DosageUnitDto> getDosageUnits();

    List<ProductDto> getProducts();

    List<GeographicLevelDto> getGeographicLevels();

    List<GeographicZoneDto> getGeographicZones();

    List<FacilityTypeDto> getFacilityTypes();

    List<FacilityDto> getFacilities();

    List<StockStatusDto> getStockStatus(int month, int year, String program);
    
}
