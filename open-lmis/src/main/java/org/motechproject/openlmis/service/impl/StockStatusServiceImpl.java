package org.motechproject.openlmis.service.impl;

import java.util.List;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.motechproject.openlmis.domain.Facility;
import org.motechproject.openlmis.domain.Product;
import org.motechproject.openlmis.domain.Program;
import org.motechproject.openlmis.domain.StockStatus;
import org.motechproject.openlmis.repository.StockStatusDataService;
import org.motechproject.openlmis.rest.domain.StockStatusDto;
import org.motechproject.openlmis.service.FacilityService;
import org.motechproject.openlmis.service.ProductService;
import org.motechproject.openlmis.service.ProgramService;
import org.motechproject.openlmis.service.StockStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.StockStatusService}
 */
@Service("stockStatusService")
public class StockStatusServiceImpl implements StockStatusService {
    
    @Autowired
    private StockStatusDataService stockStatusDataService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProgramService programService;
    
    @Autowired
    private FacilityService facilityService;
    
    private static final DateTimeFormatter DATE_TIME_FORMAT = ISODateTimeFormat.basicDate();

    @Override
    public void update(StockStatus stockStatus) {
        stockStatusDataService.update(stockStatus);
    }

    @Override
    public void delete(StockStatus stockStatus) {
        stockStatusDataService.delete(stockStatus);
    }

    @Override
    public StockStatus createFromDetails(StockStatusDto details) {
        StockStatus stockStatus = new StockStatus();
        Product product = productService.findByCode(details.getProductCode());
        Program program = programService.findByCode(details.getProgramCode());
        List<Facility> facilities = facilityService.findByName(details.getFacilityName());
        stockStatus.setAmc(details.getAmc());
        stockStatus.setDistrict(details.getDistrict());
        if (facilities.size() > 0) {
            stockStatus.setFacility(facilities.get(0));
        }
        stockStatus.setMos(details.getMos());
        stockStatus.setProduct(product);
        stockStatus.setProgram(program);
        stockStatus.setReportedDate(DATE_TIME_FORMAT.parseDateTime(details.getReportedDate()));
        stockStatus.setReportedMonth(details.getReportedMonth());
        stockStatus.setReportedYear(details.getReportedYear());
        stockStatus.setReportMonth(details.getReportMonth());
        stockStatus.setReportPeriodName(details.getReportPeriodName());
        stockStatus.setReportQuarter(details.getReportQuarter());
        stockStatus.setReportYear(details.getReportYear());
        return stockStatusDataService.create(stockStatus);
    }

    @Override
    public void deleteAll() {
        stockStatusDataService.deleteAll();
    }

    @Override
    public List<StockStatus> findAll() {
        return stockStatusDataService.retrieveAll();
    }
}
