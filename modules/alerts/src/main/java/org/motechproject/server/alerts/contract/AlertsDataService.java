package org.motechproject.server.alerts.contract;

import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.server.alerts.domain.Alert;
import org.motechproject.server.alerts.domain.AlertStatus;
import org.motechproject.server.alerts.domain.AlertType;

import java.util.List;

public interface AlertsDataService extends MotechDataService<Alert> {

    @Lookup
    List<Alert> findByExternalId(@LookupField(name = "externalId") String externalId);

    @Lookup
    List<Alert> findByAlertType(@LookupField(name = "alertType") AlertType alertType);

    @Lookup
    List<Alert> findByStatus(@LookupField(name = "status") AlertStatus alertStatus);

    @Lookup
    List<Alert> findByPriority(@LookupField(name = "priority") Long priority);

    @Lookup
    List<Alert> findByDateTime(@LookupField(name = "dateTime") Range<DateTime> dateTimeRange);

}
