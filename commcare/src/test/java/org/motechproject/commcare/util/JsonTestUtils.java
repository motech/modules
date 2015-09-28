package org.motechproject.commcare.util;

import org.apache.commons.io.FileUtils;
import org.motechproject.testing.utils.FileHelper;

import java.io.IOException;

/**
 * Utility class for loading JSON files into a String. It also stores locations for resource JSON files use by tests.
 */
public class JsonTestUtils {

    public static final String VALID_STOCK_LEDGER = "json/domain/sampleStockLedger.json";
    public static final String MALFORMED_STOCK_LEDGER = "json/domain/malformedStockLedger.json";

    /**
     * Loads the JSON from the location given as {@code resourcePath}.
     *
     * @param resourcePath  the path to the resource
     * @return the resource file as string
     * @throws IOException if there were problems while opening file
     */
    public static String loadFromFile(String resourcePath) throws IOException {
        return FileUtils.readFileToString(FileHelper.getResourceFile(resourcePath));
    }

    /**
     * Utility class, should not be initiated.
     */
    private JsonTestUtils() {
    }
}
