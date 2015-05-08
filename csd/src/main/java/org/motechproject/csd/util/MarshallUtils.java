package org.motechproject.csd.util;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

public final class MarshallUtils {

    private MarshallUtils() {
    }

    public static String marshall(Object obj, URL schemaURL, Class... classesToBeBound) throws JAXBException, SAXException {

        JAXBContext context = JAXBContext.newInstance(classesToBeBound);

        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        if (schemaURL != null) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(schemaURL);
            marshaller.setSchema(schema);
        }

        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);

        return writer.toString();
    }

    public static Object unmarshall(String xml, URL schemaURL, Class... classesToBeBound) throws JAXBException, SAXException {

        JAXBContext context = JAXBContext.newInstance(classesToBeBound);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        if (schemaURL != null) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(schemaURL);
            unmarshaller.setSchema(schema);
        }

        StringReader reader = new StringReader(xml);

        return unmarshaller.unmarshal(reader);
    }
}
