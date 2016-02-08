package org.motechproject.odk.parser.factory;

import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.exception.ConfigurationTypeException;
import org.motechproject.odk.parser.XformParser;
import org.motechproject.odk.parser.impl.XformParserKobo;
import org.motechproject.odk.parser.impl.XformParserODK;

/**
 * Factory class for {@link XformParser}
 */
public class XformParserFactory {

    /**
     * Returns the appropriate {@link XformParser} based on the configuration type.
     * @param type
     * @return
     */
    public XformParser getParser(ConfigurationType type) throws ConfigurationTypeException {

        switch (type) {
            case ONA:
                return new XformParserODK();
            case ODK:
                return new XformParserODK();
            case KOBO:
                return new XformParserKobo();
            default:
                throw new ConfigurationTypeException("No parser for configuration type: " + type);
        }
    }
}
