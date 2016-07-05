package org.motechproject.rapidpro.dataprovider;

import org.motechproject.commons.api.AbstractDataProvider;
import org.motechproject.rapidpro.service.ContactService;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link org.motechproject.commons.api.DataProvider}. Provides tasks access
 * for {@link Contact} objects.
 */
@Component("rapidproDataProvider")
public class RapidproDataProvider extends AbstractDataProvider {
    private static final String DATA_PROVIDER_RESOURCE = "rapidpro-data-provider.json";
    private static final String NAME = "Rapidpro";
    private static final String PACKAGE_ROOT = "org.motechproject.rapidpro.webservice.dto";
    private static final String EXTERNAL_ID_FIELD = "externalId";
    private static final String CONTACT = "Contact";
    private static final String EXTERNAL_ID_LOOKUP = "rapidpro.dataprovider.contact.externalId";

    private List<Class<?>> classes;
    private ContactService contactService;

    @Autowired
    public RapidproDataProvider(ResourceLoader resourceLoader, ContactService contactService) {
        this.contactService = contactService;
        this.classes = new ArrayList<>();
        this.classes.add(Contact.class);

        Resource resource = resourceLoader.getResource(DATA_PROVIDER_RESOURCE);
        if (resource != null) {
            setBody(resource);
        }
    }

    /**
     * Provides a list of classes the data provider supports.
     *
     * @return A List of classes supported by this data provider.
     */
    @Override
    public List<Class<?>> getSupportClasses() {
        return classes;
    }

    /**
     * Returns the root package for supported classes.
     *
     * @return The name of the package root for the supported classes.
     */
    @Override
    public String getPackageRoot() {
        return PACKAGE_ROOT;
    }

    /**
     * Returns the data provider name.
     *
     * @return The name of the data provider.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Performs the lookup for Tasks.
     *
     * @param type         The object type
     * @param lookupName   The name of the lookup
     * @param lookupFields The keys and values for lookup fields.
     * @return The object satisfying the lookup conditions, if it exists.
\     */
    @Override
    public Object lookup(String type, String lookupName, Map<String, String> lookupFields) {
        switch (type) {

            case CONTACT:
                return handleContactLookup(lookupName, lookupFields);

            default:
                return null;
        }
    }

    private Contact handleContactLookup(String lookupName, Map<String, String> lookupFields) {
        Contact contact = null;

        switch (lookupName) {

            case EXTERNAL_ID_LOOKUP:
                String externalId = lookupFields.get(EXTERNAL_ID_FIELD);
                contact = contactService.findByExternalId(externalId);
                break;
        }
        return contact;
    }
}
