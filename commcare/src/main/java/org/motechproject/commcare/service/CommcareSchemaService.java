package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.tasks.builder.model.CaseTypeWithApplicationName;
import org.motechproject.commcare.tasks.builder.model.FormWithApplicationName;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The <code>CommcareSchemaService</code> is responsible for retrieving
 * CommCare applications and schemas from MOTECH database. The important point is that implementers
 * should NOT query CommCareHQ for data, but rather use current, local copy of the data.
 */
public interface CommcareSchemaService {

    /**
     * Retrieves form schemas for all modules from MOTECH database for given configuration..
     * @return List of all from schemas
     */
    List<FormSchemaJson> getAllFormSchemas(String configName);

    /**
     * Same as {@link #getAllFormSchemas(String) getAllFormSchemas} but uses default CommcareHQ configuration.
     */
    List<FormSchemaJson> getAllFormSchemas();

    /**
     * Retrieves case types and fields that are assigned to the case type.
     * @return Map of case types. Key represents a case type, while a value is a
     *         collection of fields assigned to the case type.
     */
    Map<String, Set<String>> getAllCaseTypes(String configName);

    /**
     * Same as {@link #getAllCaseTypes(String) getAllCaseTypes} but uses default CommcareHQ configuration.
     */
    Map<String, Set<String>> getAllCaseTypes();

    /**
     * Retrieves applications by configuration name
     *
     * @return the list of matching applications
     */
    List<CommcareApplicationJson> retrieveApplications(String configName);
    
    /**
     * Retrieves case types with application name
     * @return Set of Cases with display names
     */
    Set<CaseTypeWithApplicationName> getCaseTypesWithApplicationName(String configName);

    /**
     * Retrieves forms with application name
     * @return Set of Forms with display names.
     */
    Set<FormWithApplicationName> getFormsWithApplicationName(String configName);

    /**
     * Retrieves reports metadata by configuration name
     *
     * @return the list of matching reports metadata
     */
    List<ReportsMetadataInfo> getReportsMetadata(String configName);
}
