package org.motechproject.alerts.contract;

import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.alerts.domain.Alert;
import org.motechproject.alerts.domain.AlertStatus;
import org.motechproject.alerts.domain.AlertType;

import java.util.List;

/**
 * Data Service interface for {@link org.motechproject.alerts.domain.Alert}s. The implementation
 * is provided by the Motech Data Services module.
 */
public interface AlertsDataService extends MotechDataService<Alert> {

    @Lookup
    List<Alert> findByExternalId(@LookupField(name = "externalId") String externalId);

    @Lookup
    List<Alert> findByAlertType(@LookupField(name = "alertType") AlertType alertType);

    @Lookup
    List<Alert> findByStatus(@LookupField(name = "status") AlertStatus alertStatus);

    long countFindByStatus(@LookupField(name = "status") AlertStatus alertStatus);

    @Lookup
    List<Alert> findByPriority(@LookupField(name = "priority") Long priority);

    @Lookup
    List<Alert> findByDateTime(@LookupField(name = "dateTime") Range<DateTime> dateTimeRange);

}
