package org.motechproject.ipf.service;

import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.mds.annotations.InstanceLifecycleListener;
import org.motechproject.mds.annotations.InstanceLifecycleListenerType;

/**
 * Task service is used to register IPF task channel with template recipient actions.
 */
public interface IPFTaskService {

    /**
     * Automatically updates the IPF task channel after creating/updating template in the mds.
     *
     * @param ipfTemplate created/updated template
     */
    @InstanceLifecycleListener({InstanceLifecycleListenerType.POST_STORE})
    void templateChanged(IPFTemplate ipfTemplate);

    /**
     * Automatically updates the IPF task channel after deleting template from the mds.
     *
     * @param ipfTemplate deleted template
     */
    @InstanceLifecycleListener({InstanceLifecycleListenerType.POST_DELETE})
    void templateRemoved(IPFTemplate ipfTemplate);

    /**
     * Logs information about updating task channel.
     *
     * @param ipfTemplate deleted template
     */
    @InstanceLifecycleListener({InstanceLifecycleListenerType.PRE_DELETE})
    void preTemplateRemoved(IPFTemplate ipfTemplate);
}
