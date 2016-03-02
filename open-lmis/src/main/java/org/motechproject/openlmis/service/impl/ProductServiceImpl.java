package org.motechproject.openlmis.service.impl;

import java.util.List;

import org.motechproject.openlmis.domain.DosageUnit;
import org.motechproject.openlmis.domain.Product;
import org.motechproject.openlmis.repository.ProductDataService;
import org.motechproject.openlmis.rest.domain.ProductDto;
import org.motechproject.openlmis.service.DosageUnitService;
import org.motechproject.openlmis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.ProductService}
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductDataService productDataService;
    
    @Autowired
    private DosageUnitService dosageUnitService;
    
    @Override
    public Product findByOpenlmisId(Integer id) {
        return productDataService.findByOpenlmisId(id);
    }
    
    @Override
    public void update(Product product) {
        productDataService.update(product);
    }

    @Override
    public void delete(Product product) {
        productDataService.delete(product);
    }

    @Override
    public Product createFromDetails(ProductDto details) {
        Product product = new Product();
        DosageUnit dosageUnit = dosageUnitService.findByOpenlmisId(details.getDosageUnitId());
        product.setActive(details.getActive());
        product.setAlternateItemCode(details.getAlternateItemCode());
        product.setAlternateName(details.getAlternateName());
        product.setAlternatePackSize(details.getAlternatePackSize());
        product.setApprovedByWHO(details.getApprovedByWHO());
        product.setArchived(details.getArchived());
        product.setCartonHeight(details.getCartonHeight());
        product.setCartonLength(details.getCartonLength());
        product.setCartonsPerPallet(details.getCartonsPerPallet());
        product.setCartonWidth(details.getCartonWidth());
        product.setCode(details.getCode());
        product.setContraceptiveCYP(details.getContraceptiveCYP());
        product.setControlledSubstance(details.getControlledSubstance());
        product.setDescription(details.getDescription());
        product.setDispensingUnit(details.getDispensingUnit());
        product.setDosageUnit(dosageUnit);
        product.setDosesPerDispensingUnit(details.getDosesPerDispensingUnit());
        product.setExpectedShelfLife(details.getExpectedShelfLife());
        product.setFlammable(details.getFlammable());
        product.setFullName(details.getFullName());
        product.setFullSupply(details.getFullSupply());
        product.setGenericName(details.getGenericName());
        product.setGtin(details.getGtin());
        product.setHazardous(details.getHazardous());
        product = addMoreFromDetails(product, details);
        return productDataService.create(product);
    }
    
    private Product addMoreFromDetails(Product product, ProductDto details) {
        product.setIsKit(details.getIsKit());
        product.setLightSensitive(details.getLightSensitive());
        product.setManufacturer(details.getManufacturer());
        product.setManufacturerBarCode(details.getManufacturerBarCode());
        product.setManufacturerCode(details.getManufacturerCode());
        product.setMohBarCode(details.getMohBarCode());
        product.setOpenlmisid(details.getId());
        product.setPackHeight(details.getPackHeight());
        product.setPackLength(details.getPackLength());
        product.setPackRoundingThreshold(details.getPackRoundingThreshold());
        product.setPackSize(details.getPackSize());
        product.setPacksPerCarton(details.getPacksPerCarton());
        product.setPackWeight(details.getPackWeight());
        product.setPackWidth(details.getPackWidth());
        product.setPrimaryName(details.getPrimaryName());
        product.setRoundToZero(details.getRoundToZero());
        product.setSpecialStorageInstructions(details.getSpecialStorageInstructions());
        product.setSpecialTransportInstructions(details.getSpecialTransportInstructions());
        product.setStoreRefrigerated(details.getStoreRefrigerated());
        product.setStrength(details.getStrength());
        product.setTracer(details.getTracer());
        product.setType(details.getType());
        return product;
    }

    @Override
    public void deleteAll() {
        productDataService.deleteAll();
    }

    @Override
    public List<Product> findAll() {
        return productDataService.retrieveAll();
    }

    @Override
    public Product findByCode(String code) {
        return productDataService.findByCode(code);
    }

}
