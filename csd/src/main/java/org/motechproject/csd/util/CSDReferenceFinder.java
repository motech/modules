package org.motechproject.csd.util;

import org.motechproject.csd.constants.CSDConstants;
import org.motechproject.csd.domain.AbstractID;
import org.motechproject.csd.domain.BaseMainEntity;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.filter.Filter;
import org.motechproject.mds.filter.FilterValue;
import org.motechproject.mds.filter.Filters;
import org.motechproject.mds.helper.DataServiceHelper;
import org.motechproject.mds.service.MotechDataService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CSDReferenceFinder {

    private BundleContext bundleContext;

    private Set<Class> csdClasses;

    private Map<String, Map<Class, Field>> csdFieldRefs = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(CSDReferenceFinder.class);

    public CSDReferenceFinder() throws IOException, ClassNotFoundException {
        csdClasses = findAnnotatedClasses("org.motechproject.csd.domain", Entity.class);

        if (csdClasses.isEmpty()) {
            csdClasses = new HashSet<Class>(CSDConstants.CSD_CLASSES);
        }
        for (Class c : csdClasses) {
            csdFieldRefs.put(c.getName(), findReferencingFields(c));
        }
    }

    @Autowired
    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    final Map<Class, Field> findReferencingFields(Class clazz) {
        Map<Class, Field> referencingFields = new HashMap<>();

        for (Class c : csdClasses) {
            for (Field f : c.getDeclaredFields()) {
                if (f.getType().toString().equals(clazz.toString())) {
                    referencingFields.put(c, f);
                } else if (f.getGenericType() instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
                    Type argType = parameterizedType.getActualTypeArguments()[0];
                    if (argType.toString().equals(clazz.toString())) {
                        referencingFields.put(c, f);
                    }
                }
            }
        }
        return referencingFields;
    }

    public Set<Object> findReferences(AbstractID o) {
        Set<Object> referencingEntities = new HashSet<>();

        Map<Class, Field> refs = csdFieldRefs.get(o.getClass().getName());
        for (Map.Entry<Class, Field> ref : refs.entrySet()) {
            try {
                referencingEntities.addAll(findReferences(o, ref.getKey(), ref.getValue()));
            } catch (ReflectiveOperationException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return referencingEntities;
    }

    public List<Object> findReferences(AbstractID o, Class c, final Field f) throws ReflectiveOperationException {
        final Object id = o.getId();

        FilterValue filterValue = new FilterValue() {
            @Override
            public Object valueForQuery() {
                return id;
            }

            @Override
            public String paramTypeForQuery() {
                return Long.class.getName();
            }

            @Override
            public List<String> operatorForQueryFilter() {
                return f.getGenericType() instanceof ParameterizedType
                        ? Arrays.asList(".contains(", ")") : Arrays.asList("==");
            }
        };
        Filter filter = new Filter(f.getName(), new FilterValue[]{filterValue});

        MotechDataService dataService = DataServiceHelper.getDataService(bundleContext, c.getName());
        return dataService.filter(new Filters(filter), null);
    }

    public BaseMainEntity findParentEntity(Object o) {
        Set<Object> references = findReferences((AbstractID) o);
        for (Object ref : references) {
            if (ref instanceof BaseMainEntity) {
                return (BaseMainEntity) ref;
            } else {
                BaseMainEntity entity = findParentEntity(ref);
                if (entity != null) {
                    return entity;
                }
            }
        }
        return null;
    }

    private Set<Class> findAnnotatedClasses(String packageName, Class annotationClass) throws IOException, ClassNotFoundException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

        Set<Class> classes = new HashSet<>();
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                Class c = Class.forName(metadataReader.getClassMetadata().getClassName());
                if (c != null && c.getAnnotation(annotationClass) != null) {
                    classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        }
        return classes;
    }

}
