package org.motechproject.commcare.request.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.motechproject.commcare.domain.IndexTask;
import org.motechproject.commcare.exception.MalformedCaseXmlException;
import org.motechproject.commcare.request.IndexSubElement;

/**
 * Converter responsible for (un)marshaling index elements in an XML document.
 */
public class IndexElementConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer,
            MarshallingContext marshallingContext) {
        IndexTask element = (IndexTask) o;

        for (IndexSubElement subElement : element.getIndices()) {
            if (subElement.getIndexNodeName() == null || subElement.getCaseType() == null || subElement.getCaseId() == null) {
                throw new MalformedCaseXmlException("An index element is malformed");
            } else {
                writer.startNode(subElement.getIndexNodeName());
                writer.addAttribute("case_type", subElement.getCaseType());
                writer.setValue(subElement.getCaseId());
                writer.endNode();
            }

        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
            UnmarshallingContext unmarshallingContext) {
        return null;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(IndexTask.class);
    }
}
