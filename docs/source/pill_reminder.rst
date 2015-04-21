.. _pill-reminder-module:

====================
Pill Reminder Module
====================

Description
-----------

The Pill Reminder Module provides repeated reminders about pill regimens to users. Users are enrolled in a regimen and then reminded to take the appropriate medicines at the appropriate times of the day.

Information for Implementation
------------------------------

The Pill Reminder module provides implementers with the following functionality:

- Enroll users in a custom pill regimen
- Custom per-user reminders for specific medicines during defined time windows

OSGi Service API
----------------
The Pill Reminder module exposes an OSGi service to perform common operations on pill reminders. The service provides the following API:

.. code-block:: java

  public interface PillReminderService {
    // creates a new pill regimen entry in Motech Data Services (MDS) and logs a new
    // daily job in Motech's scheduler.
    void createNew(DailyPillRegimenRequest dailyPillRegimenRequest);

    // clears and then recreates existing entries in MDS and scheduler
    void renew(DailyPillRegimenRequest newDailyScheduleRequest);

    // set the date in which a patient's response to a pill reminder was last
    // last recorded.
    void dosageStatusKnown(Long pillRegimenId, Long dosageId, LocalDate lastCapturedDate);

    // get the pill regimen associated with the patient's id
    PillRegimenResponse getPillRegimen(String externalId);

    // remove the patient's pill regimen from MDS and the scheduler
    void remove(String externalID);
  }

Common Actions
--------------

In the example code snippets below, it is the implementer's responsibility to provide an external ID for the user.

Enroll a User in a Regimen
~~~~~~~~~~~~~~~~~~~~~~~~~~
.. code-block:: java

  LocalDate startDate = DateUtil.today();
  LocalDate endDate = startDate.plusDays(2);

  // The user is expected to take two different medicines with overlapping but different time windows
  MedicineRequest medicineRequest1 = new MedicineRequest("m1", startDate, endDate);
  MedicineRequest medicineRequest2 = new MedicineRequest("m2", startDate.plusDays(1), startDate.plusDays(4));
  List<MedicineRequest> medicineRequests = asList(medicineRequest1, medicineRequest2);

  // The user must take the medicine at 9:05
  DosageRequest dosageRequest = new DosageRequest(9, 5, medicineRequests);

  // The user has 5 hours to take their medicine.  The system will remind them every 20 minutes with a 5 minute overtime buffer.
  // The pill is only taken once a day.  (Only one dosage request)
  DailyPillRegimenRequest dailyPillRegimenRequest = new DailyPillRegimenRequest(userId, 5, 20, 5, asList(dosageRequest));

  pillReminderService.createNew(dailyPillRegimenRequest);

Respond to a Reminder Event
~~~~~~~~~~~~~~~~~~~~~~~~~~~
.. code-block:: java

  @MotechListener(subjects = EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT)
  public void handlePillReminderEvent(MotechEvent motechEvent) {
      Map<String, Object> parameters = motechEvent.getParameters();
      String patientDocId = (String) parameters.get(EventKeys.EXTERNAL_ID_KEY);
      String regimenId = (String) parameters.get(EventKeys.PILLREMINDER_ID_KEY);
      String dosageId = (String) parameters.get(EventKeys.DOSAGE_ID_KEY);
      int timesAlreadyCalled = (Integer) parameters.get(EventKeys.PILLREMINDER_TIMES_SENT);
      int totalCallsToBeMade = (Integer) parameters.get(EventKeys.PILLREMINDER_TOTAL_TIMES_TO_SEND);

      // In this example we place a call to the patient to remind them
      call.execute(patientDocId, regimenId, dosageId, timesAlreadyCalled, totalCallsToBeMade);
  }

Configuration
-------------

This module does not require any custom configuration.

Events
------

The Pill Reminder module emits and consumes the following events, with the keys of each payload and their corresponding type given below.

Emitted Events
~~~~~~~~~~~~~~

::

  EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT
    (String) EventKeys.EXTERNAL_ID_KEY
    (String) EventKeys.PILLREMINDER_ID_KEY
    (String) EventKeys.DOSAGE_ID_KEY
    (Integer) EventKeys.PILLREMINDER_TIMES_SENT
    (Integer) EventKeys.PILLREMINDER_TOTAL_TIMES_TO_SEND

The Pill Reminder module exposes this event to the Task module as a task trigger, allowing other modules to respond to pill reminder events using the Task module user interface.


Consumed Events
~~~~~~~~~~~~~~~

::

  EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_DOSAGE_STATUS_KNOW
    (Long) EventKeys.PILL_REGIMEN_ID
    (Long) EventKeys.DOSAGE_ID_KEY
    (Date) EventKeys.LAST_CAPTURE_DATE

  EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_UNSUBSCRIBE
    (String) EventKeys.EXTERNAL_ID_KEY

The Pill Reminder module exposes this event to the Task module as a task action, allowing other modules to schedule pill reminders using the Task module user interface.
