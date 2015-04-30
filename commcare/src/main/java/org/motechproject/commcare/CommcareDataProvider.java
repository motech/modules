package org.motechproject.commcare;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CommcareFixture;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareUser;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.service.impl.CommcareCaseServiceImpl;
import org.motechproject.commcare.service.impl.CommcareFixtureServiceImpl;
import org.motechproject.commcare.service.impl.CommcareFormServiceImpl;
import org.motechproject.commcare.service.impl.CommcareUserServiceImpl;
import org.motechproject.commcare.tasks.builder.CommcareDataProviderBuilder;
import org.motechproject.commcare.util.FormValueParser;
import org.motechproject.commons.api.AbstractDataProvider;
import org.motechproject.commons.api.DataProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The <code>CommcareDataProvider</code> class is responsible for providing
 * lookups for certain CommCare entities. Thanks to this class, users
 * can use Commcare as a data source in Tasks.
 */
public class CommcareDataProvider extends AbstractDataProvider {
    private static final String ID_FIELD = "id";

    private static final String COMMCARE_USER = "CommcareUser";
    private static final String COMMCARE_FORM = "CommcareForm";
    private static final String COMMCARE_FIXTURE = "CommcareFixture";
    private static final String CASE_INFO  = "CaseInfo";

    private CommcareUserServiceImpl commcareUserService;
    private CommcareFixtureServiceImpl commcareFixtureService;
    private CommcareCaseServiceImpl commcareCaseService;
    private CommcareFormServiceImpl commcareFormService;

    private CommcareDataProviderBuilder dataProviderBuilder;
    private BundleContext bundleContext;
    private ServiceRegistration serviceRegistration;

    private List<String> supportedTypes = new ArrayList<>();


    @Autowired
    public CommcareDataProvider(ResourceLoader resourceLoader) {
        Resource resource = resourceLoader.getResource("task-data-provider.json");

        if (resource != null) {
            setBody(resource);
        }

        supportedTypes.addAll(Arrays.asList(COMMCARE_USER, COMMCARE_FIXTURE, COMMCARE_FORM, CASE_INFO));
    }

    @Override
    public String getName() {
        return "Commcare";
    }

    @Override
    public Object lookup(String type, String lookupName, Map<String, String> lookupFields) {
        Object obj = null;

        if (supports(type) && lookupFields.containsKey(ID_FIELD)) {
            String id = lookupFields.get(ID_FIELD);
            String configName = type.substring(type.lastIndexOf('-') + 1);

            if (type.startsWith(COMMCARE_USER)) {
                obj = getUser(id, configName);
            } else if (type.startsWith(COMMCARE_FIXTURE)) {
                obj = getFixture(id, configName);
            } else if (type.startsWith(CASE_INFO)) {
                CaseInfo caseInfo = getCase(id, configName);
                obj = caseInfo == null ? null : convertCaseToMap(caseInfo);
            } else if (type.startsWith(COMMCARE_FORM)) {
                CommcareForm commcareForm = getForm(id, configName);
                obj = commcareForm == null ? null : convertFormToMap(commcareForm);
            }
        }

        return obj;
    }

    @Override
    public List<Class<?>> getSupportClasses() {
        // This is used by default implementation of the AbstractTaskDataProvider
        // We provide our own method to determine whether the type is supported, therefore null here
        return null;
    }

    @Override
    public boolean supports(String type) {
        for (String supported : supportedTypes) {
            if (type.startsWith(supported)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPackageRoot() {
        return "org.motechproject.commcare.domain";
    }

    public void updateDataProvider() {
        setBody(dataProviderBuilder.generateDataProvider());
        // we unregister the service, then register again
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
            serviceRegistration = null;
        }

        serviceRegistration = bundleContext.registerService(DataProvider.class.getName(), this, null);
    }

    private Map<String, Object> convertFormToMap(CommcareForm form) {
        FormValueElement root = form.getForm();
        form.setForm(null);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultMap = mapper.convertValue(form, Map.class);
        resultMap.putAll(FormValueParser.parseFormToMap(root, "/data"));

        return resultMap;
    }

    private Map<String, Object> convertCaseToMap(CaseInfo caseInfo) {
        Map<String, String> fields = caseInfo.getFieldValues();
        caseInfo.setFieldValues(null);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultMap = mapper.convertValue(caseInfo, Map.class);
        if (fields != null) {
            resultMap.putAll(fields);
        }

        return resultMap;
    }

    private CommcareUser getUser(String id, String configName) {
        return commcareUserService.getCommcareUserById(id, configName);
    }

    private CommcareFixture getFixture(String id, String configName) {
        return commcareFixtureService.getCommcareFixtureById(id, configName);
    }

    private CaseInfo getCase(String id, String configName) {
        return commcareCaseService.getCaseByCaseId(id, configName);
    }

    private CommcareForm getForm(String id, String configName) {
        return commcareFormService.retrieveForm(id, configName);
    }

    @Autowired
    public void setCommcareUserService(CommcareUserServiceImpl commcareUserService) {
        this.commcareUserService = commcareUserService;
    }

    @Autowired
    public void setCommcareFixtureService(CommcareFixtureServiceImpl commcareFixtureService) {
        this.commcareFixtureService = commcareFixtureService;
    }

    @Autowired
    public void setCommcareCaseService(CommcareCaseServiceImpl commcareCaseService) {
        this.commcareCaseService = commcareCaseService;
    }

    @Autowired
    public void setCommcareFormService(CommcareFormServiceImpl commcareFormService) {
        this.commcareFormService = commcareFormService;
    }

    @Autowired
    public void setDataProviderBuilder(CommcareDataProviderBuilder dataProviderBuilder) {
        this.dataProviderBuilder = dataProviderBuilder;
    }

    @Autowired
    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }
}
