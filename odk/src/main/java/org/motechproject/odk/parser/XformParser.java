package org.motechproject.odk.parser;

import org.motechproject.odk.domain.FormDefinition;

import javax.xml.xpath.XPathExpressionException;

/**
 * Parses XML form definitions.
 */
public interface XformParser {

    /**
     * Parses the string xForm
     * @param xForm
     * @param configurationName
     * @return
     * @throws XPathExpressionException
     */
    FormDefinition parse(String xForm, String configurationName) throws XPathExpressionException;
}
