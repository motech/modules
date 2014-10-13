package org.motechproject.commcare.tasks;

import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.InputStream;

/**
 * Loads resources for velocity from the CommCare bundle.
 */
public class VelocityResourceLoader extends ClasspathResourceLoader {

    @Override
    public InputStream getResourceStream(String name) {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(name);
        if (resource == null) {
            resource = getClass().getResourceAsStream(name);
        }
        return resource;
    }
}
