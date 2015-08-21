package org.motechproject.sms.event;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.sms.tasks.SmsTasksNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import static org.motechproject.sms.event.constants.EventSubjects.CONFIGS_CHANGED;

/**
 * The <code>ConfigurationEventHandler</code> class listens to notifications about configuration changes
 * and when such event is received it tries to update sms task channel with updated configurations.
 */
@Component
public class ConfigurationEventHandler {

    @Autowired
    private SmsTasksNotifier smsTasksNotifier;

    @MotechListener(subjects = CONFIGS_CHANGED)
    public synchronized void configsChanged(MotechEvent event) {
        smsTasksNotifier.updateTasksInfo();
    }
}
