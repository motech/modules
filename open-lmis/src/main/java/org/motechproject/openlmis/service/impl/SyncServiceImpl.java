package org.motechproject.openlmis.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.motechproject.openlmis.domain.Program;
import org.motechproject.openlmis.rest.domain.DosageUnitDto;
import org.motechproject.openlmis.rest.domain.FacilityDto;
import org.motechproject.openlmis.rest.domain.FacilityTypeDto;
import org.motechproject.openlmis.rest.domain.GeographicLevelDto;
import org.motechproject.openlmis.rest.domain.GeographicZoneDto;
import org.motechproject.openlmis.rest.domain.ProductCategoryDto;
import org.motechproject.openlmis.rest.domain.ProductDto;
import org.motechproject.openlmis.rest.domain.ProgramDto;
import org.motechproject.openlmis.rest.domain.StockStatusDto;
import org.motechproject.openlmis.rest.service.OpenlmisWebService;
import org.motechproject.openlmis.service.DosageUnitService;
import org.motechproject.openlmis.service.FacilityService;
import org.motechproject.openlmis.service.FacilityTypeService;
import org.motechproject.openlmis.service.GeographicLevelService;
import org.motechproject.openlmis.service.GeographicZoneService;
import org.motechproject.openlmis.service.ProductCategoryService;
import org.motechproject.openlmis.service.ProductService;
import org.motechproject.openlmis.service.ProgramService;
import org.motechproject.openlmis.service.StockStatusService;
import org.motechproject.openlmis.service.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link org.motechproject.openlmis.service.SyncService}
 */
@Service("syncService")
@PropertySource("classpath:openlmis.properties")
public class SyncServiceImpl implements SyncService {
    @Autowired
    private OpenlmisWebService openlmisWebService;
    
    @Autowired
    private ProgramService programService;
    
    @Autowired
    private ProductCategoryService productCategoryService;
    
    @Autowired
    private DosageUnitService dosageUnitService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private GeographicLevelService geographicLevelService;
    
    @Autowired
    private GeographicZoneService geographicZoneService;
    
    @Autowired
    private FacilityTypeService facilityTypeService;
    
    @Autowired
    private FacilityService facilityService;
    
    @Autowired
    private StockStatusService stockStatusService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncServiceImpl.class);
    
    @Value("${openlmis.stockstatus.fromyear}")
    private int fromYear;

    @Override
    @Transactional
    public boolean sync() {
        LOGGER.debug("Starting Sync");
        try {
            long startTime = System.nanoTime();
            dropExistingData();
            // add new data
            addPrograms();
            
            addProductCategories();
            addDosageUnits();
            addProducts();
            
            addGeographicLevels();
            addGeographicZones();
            addFacilityTypes();
            addFacilites();

            addStockStatus();
            
            long endTime = System.nanoTime();

            LOGGER.debug("Sync successful.");
            LOGGER.debug("Time for sync: " + TimeUnit.SECONDS.convert(endTime - startTime,
                    TimeUnit.NANOSECONDS) + "seconds");
            return true;
        } catch (RuntimeException e) {
            LOGGER.error("Problem with OpenLMIS application Schema. Sync unsuccessful.", e);
            return false;
        }
    }
    
    private void addPrograms() {
        List<ProgramDto> programDtos = openlmisWebService.getPrograms();
        for (ProgramDto dto : programDtos) {
            programService.createFromDetails(dto);
        }
    }
    
    private void addProductCategories() {
        List<ProductCategoryDto> productCategories = openlmisWebService.getProductCategories();
        for (ProductCategoryDto dto : productCategories) {
            productCategoryService.createFromDetails(dto);
        }
    }
    
    private void addDosageUnits() {
        List<DosageUnitDto> dosageUnitDtos = openlmisWebService.getDosageUnits();
        for (DosageUnitDto dto : dosageUnitDtos) {
            dosageUnitService.createFromDetails(dto);
        }
    }
    
    private void addProducts() {
        List<ProductDto> productDtos = openlmisWebService.getProducts();
        for (ProductDto dto : productDtos) {
            productService.createFromDetails(dto);
        }
    }
    
    private void addGeographicLevels() {
        List<GeographicLevelDto> geographicLevelDtos = openlmisWebService.getGeographicLevels();
        for (GeographicLevelDto dto : geographicLevelDtos) {
            geographicLevelService.createFromDetails(dto);
        }
    }
    
    private void addGeographicZones() {
        List<GeographicZoneDto> geographicZoneDtos = openlmisWebService.getGeographicZones();
        for (GeographicZoneDto dto : geographicZoneDtos) {
            geographicZoneService.createFromDetails(dto);
        }
    }
    
    private void addFacilityTypes() {
        List<FacilityTypeDto> facilityTypeDtos = openlmisWebService.getFacilityTypes();
        for (FacilityTypeDto dto : facilityTypeDtos) {
            facilityTypeService.createFromDetails(dto);
        } 
    }
    
    private void addFacilites() {
        List<FacilityDto> facilityDtos = openlmisWebService.getFacilities();
        for (FacilityDto dto : facilityDtos) {
            facilityService.createFromDetails(dto);
        }
    }
    
    private void addStockStatus() {
        DateTime now = new DateTime();
        List<Program> programs = programService.findAll();
        for (Program program : programs) {
            for (int year = fromYear; year <= now.getYear(); year++) {
                int lastMonth = (year == now.getYear() ? now.getMonthOfYear() : DateTimeConstants.DECEMBER);
                for (int month = DateTimeConstants.JANUARY; month <= lastMonth; month++) {
                    List<StockStatusDto> stockStatusDtos = openlmisWebService.getStockStatus(month, year, program.getCode());
                    for (StockStatusDto dto : stockStatusDtos) {
                        stockStatusService.createFromDetails(dto);
                    }
                }
            }
        }
    }
    
    private void dropExistingData() {
        geographicLevelService.deleteAll();
        geographicZoneService.deleteAll();
        facilityTypeService.deleteAll();
        facilityService.deleteAll();
        dosageUnitService.deleteAll();
        productCategoryService.deleteAll();
        productService.deleteAll();
        programService.deleteAll();
        stockStatusService.deleteAll();
    }
}
