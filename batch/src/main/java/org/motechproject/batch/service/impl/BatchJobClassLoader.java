package org.motechproject.batch.service.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class BatchJobClassLoader extends ClassLoader {

    private String xmlPath;
    public BatchJobClassLoader(ClassLoader l, String xmlPath) {
        super(l);
        this.xmlPath = xmlPath;
    }

    @Override
    public URL findResource(String name) {

        URL url = super.findResource(name);
        if (url == null) {
            String absNmae = xmlPath + name;
            try {
                File file = new File(absNmae);
                if (file.exists()) {
                    url = new File(absNmae).toURI().toURL();
                }
            } catch (MalformedURLException e) {

                return null;
            }
        }
        return url;
    }
}
