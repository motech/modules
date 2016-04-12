package org.motechproject.ihe.interop.service;

import org.motechproject.ihe.interop.domain.CdaTemplate;
import org.motechproject.mds.annotations.InstanceLifecycleListener;
import org.motechproject.mds.annotations.InstanceLifecycleListenerType;

/**
 * Task service is used to register IHE Interop task channel with template recipient actions.
 */
public interface IHETaskService {

    /**
     * Automatically updates the IHE Interop task channel after creating/updating template in the mds.
     *
     * @param cdaTemplate created/updated template
     */
    @InstanceLifecycleListener({InstanceLifecycleListenerType.POST_STORE})
    void templateChanged(CdaTemplate cdaTemplate);

    /**
     * Automatically updates the IHE Interop task channel after deleting template from the mds.
     *
     * @param cdaTemplate deleted template
     */
    @InstanceLifecycleListener({InstanceLifecycleListenerType.POST_DELETE})
    void templateRemoved(CdaTemplate cdaTemplate);

    /**
     * Logs information about updating task channel.
     *
     * @param cdaTemplate deleted template
     */
    @InstanceLifecycleListener({InstanceLifecycleListenerType.PRE_DELETE})
    void preTemplateRemoved(CdaTemplate cdaTemplate);
}
