package org.motechproject.batch.mds.service;

import org.motechproject.batch.mds.BatchJobParameters;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * Class to query {@link BatchJobParameters} entity. The implementation will be generated
 * at runtime by MDS.
 *
 * @author naveen
 *
 */
public interface BatchJobParameterMDSService extends MotechDataService<BatchJobParameters> {

    /**
     * Finds batch job parameters using the given job ID.
     * @param batchJobId the id of the batch job
     * @return list of parameter entities for the batch job
     */
    @Lookup(name = "By JobId")
    List<BatchJobParameters> findByJobId(@LookupField(name = "batchJobId") Integer batchJobId);

}
