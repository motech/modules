package org.motechproject.commcare.util;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.motechproject.commcare.domain.CaseXml;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for mapping the {@link CaseXml} class objects to the domain class objects and the other way round. The
 * domain class is passed as the class parameter.
 *
 * @param <T>  the domain class
 */
public class CaseMapper<T> {

    private Class<T> clazz;

    /**
     * Creates an instance of the {@link CaseMapper} that is capable of mapping the {@link CaseXml} class objects into
     * objects of the class passed as the {@code clazz} parameter.
     *
     * @param clazz  the domain class
     */
    public CaseMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Maps the given instance of the {@link CaseXml} to an instance of the domain class.
     *
     * @param ccCase  the instance of the {@link CaseXml} class
     * @return the created instance of the domain class
     */
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

    /**
     * Maps the given instance of the domain class to an instance of the {@code CaseXml} class.
     *
     * @param careCase  the instance of the domain class
     * @return the created instance of the {@link CaseXml} class
     */
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
