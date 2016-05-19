package org.motechproject.openlmis.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.openlmis.domain.ProductCategory;


/**
 * MDS data service for {@link org.motechproject.openlmis.domain.ProductCategory}
 */
public interface ProductCategoryDataService extends MotechDataService<ProductCategory> {

    @Lookup
    ProductCategory findByOpenlmisId(@LookupField(name = "openlmisid") Integer openlmisId);

    @Lookup
    ProductCategory findByCode(@LookupField(name = "code") String code);
    
    @Lookup
    List<ProductCategory> findByName(@LookupField(name = "name") String name);
}
