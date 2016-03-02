package org.motechproject.openlmis.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.openlmis.domain.Product;


/**
 * MDS data service for {@link org.motechproject.openlmis.domain.Product}
 */
public interface ProductDataService extends MotechDataService<Product> {

    @Lookup
    Product findByOpenlmisId(@LookupField(name = "openlmisid") Integer openlmisId);

    @Lookup
    Product findByCode(@LookupField(name = "code") String code);
    
}
