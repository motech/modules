package org.motechproject.csd.listener;

import org.motechproject.mds.annotations.InstanceLifecycleListener;
import org.motechproject.mds.annotations.InstanceLifecycleListenerType;

public interface CSDInstanceLifecycleListener {

    @InstanceLifecycleListener(value = InstanceLifecycleListenerType.POST_STORE, packageName = "org.motechproject.csd")
    void updateParentModificationDate(Object o);
}
