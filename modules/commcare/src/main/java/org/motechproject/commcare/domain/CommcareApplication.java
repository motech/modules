package org.motechproject.commcare.domain;

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.commons.couchdb.model.MotechBaseDataObject;

import java.util.List;

/**
 * Controller that handles the incoming full form feed from CommCareHQ.
 */
@TypeDiscriminator("doc.type === 'CommcareApplication'")
public class CommcareApplication extends MotechBaseDataObject {

    private static final long serialVersionUID = 1L;

    private String applicationName;
    private String resourceUri;
    private List<CommcareModuleJson> modules;

    public CommcareApplication() {
    }

    public CommcareApplication(String applicationName, String resourceUri, List<CommcareModuleJson> modules) {
        this();
        this.applicationName = applicationName;
        this.resourceUri = resourceUri;
        this.modules = modules;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public List<CommcareModuleJson> getModules() {
        return modules;
    }

    public void setModules(List<CommcareModuleJson> modules) {
        this.modules = modules;
    }
}
