package org.motechproject.odk.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.odk.domain.FormInstance;

/**
 * Data service for {@link FormInstance}
 */
public interface FormInstanceDataService extends MotechDataService<FormInstance> {

    @Lookup
    FormInstance byInstanceId(@LookupField(name = "instanceId") String instanceId);

    @Lookup
    FormInstance byConfigNameAndTitle(@LookupField(name = "configName") String configName, @LookupField(name = "title") String title);



}
