package org.motechproject.openlmis.service.impl;

import java.util.List;

import org.motechproject.openlmis.domain.Facility;
import org.motechproject.openlmis.repository.FacilityDataService;
import org.motechproject.openlmis.repository.FacilityTypeDataService;
import org.motechproject.openlmis.repository.GeographicZoneDataService;
import org.motechproject.openlmis.rest.domain.FacilityDto;
import org.motechproject.openlmis.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.FacilityService}
 */
@Service("facilityService")
public class FacilityServiceImpl implements FacilityService {
    @Autowired
    private FacilityDataService facilityDataService;
    
    @Autowired
    private FacilityTypeDataService facilityTypeDataService;
    
    @Autowired
    private GeographicZoneDataService geographicZoneDataService;

    @Override
    public Facility findByOpenlmisId(Integer id) {
        return facilityDataService.findByOpenlmisId(id);
    }
    
    @Override
    public List<Facility> findByName(String name) {
        return facilityDataService.findByName(name);
    }

    @Override
    public void update(Facility program) {
        facilityDataService.update(program);
    }

    @Override
    public void delete(Facility program) {
        facilityDataService.delete(program);
    }

    @Override
    public Facility createFromDetails(FacilityDto details) {
        Facility facility = new Facility();
        facility.setActive(details.getActive());
        facility.setAddress1(details.getAddress1());
        facility.setAddress2(details.getAddress2());
        facility.setAltitude(details.getAltitude());
        facility.setCatchmentPopulation(details.getCatchmentPopulation());
        facility.setCode(details.getCode());
        facility.setColdStorageGrossCapacity(details.getColdStorageGrossCapacity());
        facility.setComment(details.getComment());
        facility.setDataReportable(details.getDataReportable());
        facility.setDescription(details.getDescription());
        facility.setFax(details.getFax());
        facility.setGeographicZone(geographicZoneDataService.findByOpenlmisId(details.getGeographicZoneId()));
        facility.setGln(details.getGln());
        facility.setGoDownDate(details.getGoDownDate());
        facility.setGoLiveDate(details.getGoLiveDate());
        facility.setHasElectricity(details.getHasElectricity());
        facility.setHasElectronicDar(details.getHasElectronicDar());
        facility.setHasElectronicScc(details.getHasElectronicScc());
        facility.setLatitude(details.getLatitude());
        facility.setLongitude(details.getLongitude());
        facility.setMainPhone(details.getMainPhone());
        facility.setName(details.getName());
        facility.setOnline(details.getOnline());
        facility.setOpenlmisid(details.getId());
        facility.setOperatedById(details.getOperatedById());
        facility.setSatellite(details.getSatellite());
        facility.setSatelliteParentId(details.getSatelliteParentId());
        facility.setSdp(details.getSdp());
        facility.setSuppliesOthers(details.getSuppliesOthers());
        facility.setType(facilityTypeDataService.findByOpenlmisId(details.getTypeId()));
        return facilityDataService.create(facility);
    }

    @Override
    public void deleteAll() {
        facilityDataService.deleteAll();
    }

    @Override
    public List<Facility> findAll() {
        return facilityDataService.retrieveAll();
    }

    @Override
    public Facility findByCode(String code) {
        return facilityDataService.findByCode(code);
    }

}
