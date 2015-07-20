package org.motechproject.batch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is called from applicationBatch, so that baseContext can access the
 * file. It writes the configuration into a file.
 *
 * @author haritha
 */
public class PropertyWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyWriter.class);

    /**
     * Writes properties to a file.
     * @param file the file path that should be used fro writing the properties
     * @param props the properties to write
     */
    public PropertyWriter(String file, Properties props) {
        try (FileWriter w = new FileWriter(file)) {
            props.store(w, "FromSqlDbManager");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
