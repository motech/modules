=======
Modules
=======

:std:ref:`Alerts <alerts-module>`
---------------------------------

Collects alerts for users in an inbox-like container

:std:ref:`Appointments <appointments-module>`
---------------------------------------------

Provides appointment scheduling and reminders

:std:ref:`Atom Client <atom-client-module>`
---------------------------------------------

Fetches Atom feed(s) and publishes events when content changes

:std:ref:`Batch <batch-module>`
-------------------------------

An implementation of Spring batch (version: 3.0.0.M3); it essentially deals with scheduling triggering of jobs

:std:ref:`Care Services Discovery (CSD) <csd-module>`
-----------------------------------------------------

Consumes the `IHE Care Services Discovery Profile <http://wiki.ihe.net/index.php/Care_Services_Discovery>`_ from a CSD registry such as `OpenInfoMan <https://github.com/openhie/openinfoman>`_, representing the information natively in MOTECH within MDS.

:std:ref:`CMS Lite <cms-lite-module>`
-------------------------------------

Provides basic content storage and retrieval

:std:ref:`CommCare <commcare-module>`
-------------------------------------

Integrates the MOTECH platform with CommCareHQ, an open-source platform to help manage community health workers

:std:ref:`Data Services <data-services-module>`
-----------------------------------------------

Integrates data from external data sources and provides sharable data schemas

:std:ref:`DHIS2 <dhis2-module>`
-------------------------------

Integrates MOTECH with the DHIS2 web API

:std:ref:`Email <email-module>`
-------------------------------

Sends and logs email messages

:std:ref:`Event Logging <event-logging-module>`
-----------------------------------------------

Allows MOTECH modules to easily see each others’ events

:std:ref:`Hindi Transliteration <hindi-transliteration-module>`
---------------------------------------------------------------

Supports transliteration of English strings to Hindi using ITRANS encoding

:std:ref:`Hub <hub-module>`
---------------------------

Provides an implementation of the PubSubHubbub Hub spec; exposes an API so other modules can act as publisher and make contents available to it for distribution

:std:ref:`IHE Interop <ihe-interop-module>`
-------------------------------------------

Allows MOTECH to send templated XML files based on Integrating the Health Enterprise health standards. Primarily built to convert incoming CommCare forms to Continuity of Care Documents (HL7 v3 CDA CCD)

:std:ref:`IVR <ivr-module>`
---------------------------

Integrating the MOTECH platform with a Interactive Voice Response (IVR) providers thus enabling support for voice/audio dialogs

:std:ref:`Message Campaign <message-campaign-module>`
-----------------------------------------------------

Enrolls users in message campaigns with flexible content-scheduling rules

:std:ref:`mTraining <mtraining-module>`
---------------------------------------

Provides data containers and APIs for defining mobile (e.g. SMS or IVR-based) training courses and tracking user enrollment and progress

:std:ref:`OpenMRS <openmrs-module>`
-----------------------------------

Integrates the MOTECH platform with OpenMRS, an open source electronic medical record platform

:std:ref:`Pill Reminder <pill-reminder-module>`
-----------------------------------------------

A flexible reminder system that may be used to alert patients when it is time to take their medications

:std:ref:`Schedule Tracking <schedule-tracking-module>`
-------------------------------------------------------

Enrolls users for alerts based on complex scheduling rules

:std:ref:`Scheduler <scheduler-module>`
---------------------------------------

Publishes events on a schedule, using the open source Quartz engine.

:std:ref:`SMS <sms-module>`
---------------------------

Provides a basic specification for integrating the MOTECH platform with an SMS provider to send/receive SMS messages

:std:ref:`Tasks <tasks-module>`
-------------------------------

Allows administrative users to author simple "tasks" that wire up different modules; for example, a task can be created to enroll a patient in a message campaign in response to an incoming SMS message containing specific text

.. toctree::
    :hidden:
    :includehidden:
    :glob:

    *
