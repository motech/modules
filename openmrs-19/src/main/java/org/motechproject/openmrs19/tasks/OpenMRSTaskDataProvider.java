package org.motechproject.openmrs19.tasks;

import org.motechproject.commons.api.AbstractDataProvider;
import org.motechproject.openmrs19.domain.OpenMRSProvider;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.NAME;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.PACKAGE_ROOT;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.PROVIDER;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.UUID;

/**
 * This is the OpenMRS task data provider that is registered with the task module as a data source.
 * It allows retrieving objects from OpenMRS model and using it in tasks.
 */
@Service("openMRSTaskDataProvider")
public class OpenMRSTaskDataProvider extends AbstractDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSTaskDataProvider.class);
    private static final List<Class<?>> SUPPORTED_CLASS = Arrays.asList(OpenMRSProvider.class);

    private OpenMRSProviderService providerService;

    @Autowired
    public OpenMRSTaskDataProvider(ResourceLoader resourceLoader, OpenMRSProviderService providerService) {
        Resource resource = resourceLoader.getResource("task-data-provider.json");
        if (resource != null) {
            setBody(resource);
        }

        this.providerService = providerService;
    }

    @Override
    public List<Class<?>> getSupportClasses() {
        return SUPPORTED_CLASS;
    }

    @Override
    public String getPackageRoot() {
        return PACKAGE_ROOT;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object lookup(String type, String lookupName, Map<String, String> lookupFields) {
        Object obj = null;

        //In case of any trouble with the type, method supports logs an error
        if (supports(type)) {
            switch (type) {
                case PROVIDER: obj = getProvider(lookupName, lookupFields);
                    break;
            }
        }

        return obj;
    }

    private OpenMRSProvider getProvider(String lookupName, Map<String, String> lookupFields) {
        OpenMRSProvider provider = null;

        switch (lookupName) {
            case BY_UUID: provider = providerService.getProviderByUuid(lookupFields.get(UUID));
                break;
            default: LOGGER.error("Lookup with name {} doesn't exist for provider object", lookupName);
                break;
        }

        return provider;
    }
}
