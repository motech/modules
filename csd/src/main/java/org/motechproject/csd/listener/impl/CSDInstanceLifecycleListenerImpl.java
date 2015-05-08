package org.motechproject.csd.listener.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.BaseMainEntity;
import org.motechproject.csd.listener.CSDInstanceLifecycleListener;
import org.motechproject.csd.util.CSDReferenceFinder;
import org.motechproject.mds.helper.DataServiceHelper;
import org.motechproject.mds.service.MotechDataService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("csdInstanceLifecycleListener")
public class CSDInstanceLifecycleListenerImpl implements CSDInstanceLifecycleListener {

    @Autowired
    private BundleContext bundleContext;

    @Autowired
    private CSDReferenceFinder csdReferenceFinder;

    private static final Logger LOGGER = LoggerFactory.getLogger(CSDInstanceLifecycleListener.class);

    @Override
    public void updateParentModificationDate(Object o) {
        if (!(o instanceof BaseMainEntity)) {
            BaseMainEntity parentEntity = csdReferenceFinder.findParentEntity(o);
            if (parentEntity != null) {
                MotechDataService dataService = DataServiceHelper.getDataService(bundleContext, o.getClass().getName());
                parentEntity.setModificationDate(DateTime.now());
                dataService.update(parentEntity);
            } else {
                LOGGER.warn("Could not find parent entity for " + o);
            }
        }
    }
}
