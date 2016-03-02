package org.motechproject.openlmis.service;

import org.motechproject.openlmis.domain.Product;
import org.motechproject.openlmis.rest.domain.ProductDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.openlmis.domain.Product}
 */
public interface ProductService extends GenericCrudService<Product> {
    Product findByCode(String code);
    Product findByOpenlmisId(Integer id);
    Product createFromDetails(ProductDto details);
}
