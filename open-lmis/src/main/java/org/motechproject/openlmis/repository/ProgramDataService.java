package org.motechproject.openlmis.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.openlmis.domain.Program;


/**
 * MDS data service for {@link org.motechproject.openlmis.domain.Program}
 */
public interface ProgramDataService extends MotechDataService<Program> {

    @Lookup
    Program findByOpenlmisId(@LookupField(name = "openlmisid") Integer openlmisId);

    @Lookup
    Program findByCode(@LookupField(name = "code") String code);
    
    @Lookup
    List<Program> findByName(@LookupField(name = "name") String name);
}
