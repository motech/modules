package org.motechproject.batch.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class is called from applicationbatch, so that basecontext can can acces
 * the file
 * 
 * @author haritha
 * 
 */
public class PropertyWriter {

    private static final Logger LOGGER = Logger.getLogger(PropertyWriter.class);

    public PropertyWriter(String fileName, Properties props) {
        LOGGER.error("In propertywriter constructor....");
        FileWriter w;
        try {
            w = new FileWriter(fileName);
            props.store(w, "FromSqlDbManager");
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}