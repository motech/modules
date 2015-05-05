package org.motechproject.dhis2.repository;

import org.motechproject.dhis2.domain.Program;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;


/**
 * MDS data service for {@link org.motechproject.dhis2.domain.Program}
 */
public interface ProgramDataService extends MotechDataService<Program> {

    @Lookup
    Program findByUuid(@LookupField(name = "uuid") String uuid);

    @Lookup
    List<Program> findByRegistration(@LookupField(name = "registration") Boolean registration);
}
