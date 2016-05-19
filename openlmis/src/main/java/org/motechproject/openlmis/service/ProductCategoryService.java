package org.motechproject.openlmis.service;

import java.util.List;

import org.motechproject.openlmis.domain.ProductCategory;
import org.motechproject.openlmis.rest.domain.ProductCategoryDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.openlmis.domain.ProductCategory}
 */
public interface ProductCategoryService extends GenericCrudService<ProductCategory> {
    ProductCategory findByCode(String code);
    ProductCategory findByOpenlmisId(Integer id);
    List<ProductCategory> findByName(String name);
    ProductCategory createFromDetails(ProductCategoryDto details);
}
