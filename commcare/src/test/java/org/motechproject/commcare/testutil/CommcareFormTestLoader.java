package org.motechproject.commcare.testutil;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper for loading form JSON files.
 */
public final class CommcareFormTestLoader {

    public static String registrationFormJson() {
        return loadFormJson("domain/registrationForm.json");
    }

    public static String testFormJson() {
        return loadFormJson("domain/testForm.json");
    }

    public static String testFormTwoJson() {
        return loadFormJson("domain/testFormTwo.json");
    }

    public static String sampleFormJson() {
        return loadFormJson("domain/sampleForm.json");
    }

    public static String formListJson() {
        return loadFormJson("domain/formList.json");
    }

    private static String loadFormJson(String path) {
        try (InputStream is = CommcareFormTestLoader.class.getClassLoader().getResourceAsStream("json/" + path)) {
            return IOUtils.toString(is);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load json/" + path, e);
        }
    }

    private CommcareFormTestLoader() {
    }
}
