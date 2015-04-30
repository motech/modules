package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.CSD;
import org.motechproject.mds.annotations.InstanceLifecycleListener;
import org.motechproject.mds.domain.InstanceLifecycleListenerType;

public interface CSDService {

    CSD getCSD();

    CSD getByLastModified(DateTime lastModified);

    void update(CSD csd);

    void delete();

    String getXmlContent();

    void fetchAndUpdate(String xmlUrl);

    void fetchAndUpdateUsingREST(String xmlUrl);

    void fetchAndUpdateUsingSOAP(String xmlUrl, DateTime lastModified);

    void saveFromXml(String xml);

    @InstanceLifecycleListener(value = InstanceLifecycleListenerType.POST_STORE, packageName = "org.motechproject.csd")
    void updateParentModificationDate(Object o);
}
