package org.motechproject.commcare.request.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.motechproject.commcare.request.Pregnancy;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/1/12
 * Time: 8:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class PregnancyConverter implements Converter{
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        Pregnancy pregnancy = (Pregnancy) o;
        //writer.startNode("pregnancy_id");
        writer.addAttribute("case_type","pregnancy");
        writer.setValue(pregnancy.getPregnancy_id());

        //writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Pregnancy.class);
    }
}
