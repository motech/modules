package org.motechproject.openlmis.service.it;

import org.junit.After;
import org.junit.Before;
import org.motechproject.openlmis.service.DosageUnitService;
import org.motechproject.openlmis.service.FacilityService;
import org.motechproject.openlmis.service.FacilityTypeService;
import org.motechproject.openlmis.service.GeographicLevelService;
import org.motechproject.openlmis.service.GeographicZoneService;
import org.motechproject.openlmis.service.ProductCategoryService;
import org.motechproject.openlmis.service.ProductService;
import org.motechproject.openlmis.service.ProgramService;
import org.motechproject.openlmis.service.StockStatusService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;

public abstract class BaseOpenlmisIT extends BasePaxIT {

	@Inject
    private ProgramService programService;
    
	@Inject
    private ProductCategoryService productCategoryService;
    
	@Inject
    private DosageUnitService dosageUnitService;
    
	@Inject
    private ProductService productService;
    
	@Inject
    private GeographicLevelService geographicLevelService;
    
	@Inject
    private GeographicZoneService geographicZoneService;
    
    @Autowired
    private FacilityTypeService facilityTypeService;
    
    @Inject
    private FacilityService facilityService;
    
    @Inject
    private StockStatusService stockStatusService;

    @Before
    public void baseSetUp() {
        clearDatabase();
    }

    @After
    public void baseTearDown () {
        clearDatabase();
    }

    private void clearDatabase () {
    	geographicLevelService.deleteAll();
        geographicZoneService.deleteAll();
        facilityTypeService.deleteAll();
        facilityService.deleteAll();
        dosageUnitService.deleteAll();
        productCategoryService.deleteAll();
        productService.deleteAll();
        programService.deleteAll();
    }
}
