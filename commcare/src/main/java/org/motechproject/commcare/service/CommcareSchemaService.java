package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.FormSchemaJson;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The <code>CommcareSchemaService</code> is responsible for retrieving
 * CommCare schemas from MOTECH database. The important point is that implementers
 * should NOT query CommCareHQ for data, but rather use current, local copy of the data.
 */
public interface CommcareSchemaService {

    /**
     * Retrieves form schemas for all modules from MOTECH databse.
     * @return List of all from schemas
     */
    List<FormSchemaJson> getAllFormSchemas();

    /**
     * Retrieves case types and fields that are assigned to the case type.
     * @return Map of case types. Key represents a case type, while a value is a
     *         collection of fields assigned to the case type.
     */
    Map<String, Set<String>> getAllCaseTypes();
}
