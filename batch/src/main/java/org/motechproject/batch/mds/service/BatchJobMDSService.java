package org.motechproject.batch.mds.service;

import org.motechproject.batch.mds.BatchJob;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

/**
 * Class to query the {@link BatchJob} entity. Implementation will be generated
 * at runtime by MDS.
 */
public interface BatchJobMDSService extends MotechDataService<BatchJob> {

    /**
     * Finds a batch job by name.
     * @param jobName the name to query for
     * @return the found job, or null if no found
     */
    @Lookup(name = "By JobName")
    BatchJob findByJobName(@LookupField(name = "jobName") String jobName);

}
