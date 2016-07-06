package org.motechproject.alarms.service;

import org.motechproject.alarms.domain.Alarm;
import org.motechproject.alarms.domain.AlarmStatus;

import java.util.List;

public interface AlarmsService {

    List<Alarm> getAlarms();

    Alarm saveAlarm(Alarm alarm);

    void deleteAlarm(Long alarmId);

    void checkAlarm(Long alarmId);

    AlarmStatus enableAlarm(Long alarmId);

    AlarmStatus disableAlarm(Long alarmId);
}
