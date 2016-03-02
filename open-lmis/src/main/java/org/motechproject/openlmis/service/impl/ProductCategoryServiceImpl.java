package org.motechproject.openlmis.service.impl;

import java.util.List;

import org.motechproject.openlmis.domain.ProductCategory;
import org.motechproject.openlmis.repository.ProductCategoryDataService;
import org.motechproject.openlmis.rest.domain.ProductCategoryDto;
import org.motechproject.openlmis.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.ProductCategoryService}
 */
@Service("productCategoryService")
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryDataService productCategoryDataService;

    @Override
    public ProductCategory findByOpenlmisId(Integer id) {
        return productCategoryDataService.findByOpenlmisId(id);
    }
    
    @Override
    public void update(ProductCategory productCategory) {
        productCategoryDataService.update(productCategory);
    }

    @Override
    public void delete(ProductCategory productCategory) {
        productCategoryDataService.delete(productCategory);
    }

    @Override
    public ProductCategory createFromDetails(ProductCategoryDto details) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCode(details.getCode());
        productCategory.setOpenlmisid(details.getId());
        productCategory.setDisplayOrder(details.getDisplayOrder());
        productCategory.setName(details.getName());
        return productCategoryDataService.create(productCategory);
    }

    @Override
    public void deleteAll() {
        productCategoryDataService.deleteAll();
    }

    @Override
    public List<ProductCategory> findAll() {
        return productCategoryDataService.retrieveAll();
    }

    @Override
    public ProductCategory findByCode(String code) {
        return productCategoryDataService.findByCode(code);
    }

    @Override
    public List<ProductCategory> findByName(String name) {
        return productCategoryDataService.findByName(name);
    }

    

}
