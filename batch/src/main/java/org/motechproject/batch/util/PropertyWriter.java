package org.motechproject.batch.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is called from applicationBatch, so that baseContext can access the
 * file
 *
 * @author haritha
 */
public class PropertyWriter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PropertyWriter.class);

    public PropertyWriter(String fileName, Properties props) {
        try (FileWriter w = new FileWriter(fileName)) {
            props.store(w, "FromSqlDbManager");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
