package org.motechproject.casexml.request.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.motechproject.casexml.request.Pregnancy;

public class PregnancyConverter implements Converter{
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        Pregnancy pregnancy = (Pregnancy) o;
        writer.addAttribute("case_type","pregnancy");
        writer.setValue(pregnancy.getPregnancy_id());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Pregnancy.class);
    }
}
