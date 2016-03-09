package org.motechproject.dhis2.service;

import org.motechproject.dhis2.domain.DataSet;
import org.motechproject.dhis2.rest.domain.DataSetDto;

import java.util.List;

/**
 * Manages CRUD operations for the {@link DataSet} entities.
 */
public interface DataSetService {

    /**
     * Creates an instance of the {@link DataSet} class based on the information given in the {@code dto}.
     *
     * @param dto  the information about a data set
     * @return the created instance
     */
    DataSet createFromDetails(DataSetDto dto);

    /**
     * Retrieves all the data sets stored in the MOTECH database.
     *
     * @return the list of all data sets stored in the MOTECH database.
     */
    List<DataSet> findAll();

    /**
     * Retrieves data set with the given {@code uuid}.
     *
     * @param uuid  the uuid of the data set
     * @return the data set with the given uuid
     */
    DataSet findByUuid(String uuid);

    /**
     * Deletes all instances of the {@link DataSet} from the MOTECH database.
     */
    void deleteAll();
}
