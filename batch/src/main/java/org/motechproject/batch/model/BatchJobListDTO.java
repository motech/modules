package org.motechproject.batch.model;

import java.util.List;

/**
 * Class containing list of {@link BatchJobDTO} to be sent across in
 * a REST response.
 *
 * @author Naveen
 *
 */
public class BatchJobListDTO {

    /**
     * The list of jobs.
     */
    private List<BatchJobDTO> batchJobDtoList;

    /**
     * @return the list of jobs
     */
    public List<BatchJobDTO> getBatchJobDtoList() {
        return batchJobDtoList;
    }


    /**
     * @param batchJobDtoList the list of jobs
     */
    public void setBatchJobDtoList(List<BatchJobDTO> batchJobDtoList) {
        this.batchJobDtoList = batchJobDtoList;
    }
}
