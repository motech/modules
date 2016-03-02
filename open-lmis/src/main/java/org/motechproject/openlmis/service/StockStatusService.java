package org.motechproject.openlmis.service;

import org.motechproject.openlmis.domain.StockStatus;
import org.motechproject.openlmis.rest.domain.StockStatusDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.openlmis.domain.StockStatus}
 */
public interface StockStatusService extends GenericCrudService<StockStatus> {
    StockStatus createFromDetails(StockStatusDto details);
}
