package org.motechproject.commcare.util;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.motechproject.commcare.domain.CaseXml;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CaseMapper<T> {

    private Class<T> clazz;

    public CaseMapper(Class<T> clazz) {
        this.clazz = clazz;
    }


    public T mapToDomainObject(CaseXml ccCase) {
        try {
            T instance = clazz.newInstance();

            BeanUtils.copyProperties(instance, ccCase);
            BeanUtils.populate(instance, ccCase.getFieldValues());

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to map case XML to a domain object", e);
        }
    }

    public CaseXml mapFromDomainObject(T careCase) {
        CaseXml ccCase = new CaseXml();

        try {
            BeanUtils.copyProperties(ccCase, careCase);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to map case XML from a domain object", e);
        }

        BeanMap beanMap = new BeanMap(careCase);
        removeStaticProperties(beanMap);

        Map<String, String> valueMap = new HashMap<>();
        while (beanMap.keyIterator().hasNext()) {
            valueMap.put(beanMap.keyIterator().next(), (String) beanMap.get(beanMap.keyIterator().next()));
        }
        ccCase.setFieldValues(valueMap);

        return ccCase;
    }

    private void removeStaticProperties(BeanMap beanMap) {
        beanMap.remove("case_id");
        beanMap.remove("api_key");
        beanMap.remove("date_modified");
        beanMap.remove("case_type");
        beanMap.remove("case_name");
        beanMap.remove("owner_id");
    }
}
