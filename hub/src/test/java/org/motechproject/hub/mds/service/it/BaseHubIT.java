package org.motechproject.hub.mds.service.it;

import org.motechproject.testing.osgi.BasePaxIT;
import org.ops4j.pax.exam.Option;

import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

/**
 * Adds support for a custom mvn local repository.
 */
public abstract class BaseHubIT extends BasePaxIT {

    public static final String LOCAL_REPO_MAVEN_SYSTEM_PROPERTY = "maven.repo.local";
    public static final String LOCAL_REPO_PAX_OPTION = "org.ops4j.pax.url.mvn.localRepository";

    @Override
    protected Option controlOptions() {
        Option ctrlOption = super.controlOptions();

        String localRepo = System.getProperty(LOCAL_REPO_MAVEN_SYSTEM_PROPERTY);
        if (localRepo != null) {
            ctrlOption = composite(ctrlOption, systemProperty(LOCAL_REPO_PAX_OPTION).value(localRepo));
        }

        return ctrlOption;
    }
}
