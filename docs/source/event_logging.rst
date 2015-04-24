.. _event-logging-module:

====================
Event Logging Module
====================

.. contents::
    :depth: 3

############
Introduction
############

The Event Logging Module allows you to log information about events fired in MOTECH. It is possible to filter which events get
logged using :code:`EventLoggers`. The module uses logging services to store information about these events. Each service must
be registered using the :code:`EventLoggingServiceManager` which will generate the appropriate listeners for the specified
subjects. After registering these services, events with the matching subjects will be processed (logged) by them. The Event
Logging Module provides two implementations of the logging service, :code:`DbEventLoggingService` and :code:`FileEventLoggingService`,
but it is also possible to provide your own implementation.

#############################
LoggableEvent and EventLogger
#############################

The logging services use :code:`EventLogger` implementations to log events. Each :code:`EventLogger` contains data about
events to log in the form of :code:`LoggableEvent`. :code:`EventLogger` contains followings methods:

- :code:`void addLoggableEvents(List<LoggableEvent> loggableEvents)` - Adds data about events to log.
- :code:`void removeLoggableEvents(List<LoggableEvent>` -  Removes data about events to log.
- :code:`List<LoggableEvent> getLoggableEvents()` - Returns data about events to log.
- :code:`void clearLoggableEvents()` - Removes all data about events to log.
- :code:`abstract void log(MotechEvent eventToLog)` - Abstract method used to store data about events.

:code:`LoggableEvent` class should be used to check whether the event should be stored by :code:`EventLogger`. :code:`LoggableEvent`
contains method to check events(:code:`boolean isLoggableEvent(MotechEvent eventToLog)`). This method checks event subject and compare
event parameters with flags.

+--------------+--------------------------------------------------------------------------+--------------------------+
|Field         |Description                                                               |Type                      |
+==============+==========================================================================+==========================+
|eventSubjects |List of event subjects which will be logged by :code:`EventLogger`.       |List<String>              |
|              |Wildcards are allowed.                                                    |                          |
+--------------+--------------------------------------------------------------------------+--------------------------+
|flags         |List of :std:ref:`special flags <event-flags-label>`. They are used to    |List<? extends EventFlag> |
|              |filter events on the basis of the parameters.                             |                          |
+--------------+--------------------------------------------------------------------------+--------------------------+

The :code:`EventLoggingService` provides a method that allows you to retrieve set of all event subjects for which this service
is listening.

-:code:`Set<String> getLoggedEventSubjects()`

##########################
EventLoggingServiceManager
##########################

:code:`EventLoggingServiceManager` is an OSGI service through which you can register or update your logging service. Its main
function is to manage logging services. The manager keeps a list of all registered logging services.

- :code:`void registerEventLoggingService(EventLoggingService eventLoggingService)` - registers MOTECH Listener in MOTECH Platform Event module for events with subjects concluded in :code:`EventLogger` objects of given logging service.
- :code:`void updateEventLoggingService(EventLoggingService eventLoggingService)` - updates MOTECH Listener for events with subjects concluded in :code:`EventLogger` objects of given logging service(Method not implemented yet).

We can create and register a new logging service as follows:

.. code-block:: java

    EventLoggingService newLoggingService = new EventLoggingServiceImplementation();
    eventLoggingServiceManager.registerEventLoggingService(newLoggingService);

################
Database logging
################

Description
###########

The Event Logging Module automatically creates one :code:`DbEventLoggingService`. By default, this service logs all events fired in MOTECH.
Information about events is stored in the database.

DB Event Converter and Log Mapping
##################################

Description
-----------

The DB loggers(:code:`DbEventLogger`) uses a :code:`DefaultDbToLogConverter` class to build records from events. The main objective
of this component is to map the event parameters. This mapping process can remove, add or replace event parameters.

DefaultDbToLogConverter
-----------------------

:code:`DbEventLoggingService` to convert events is using a :code:`DefaultDbToLogConverter`. The conversion involves the removal
of unwanted event parameters or their replacement to others. The conversion is made on the basis of the information contained in db
loggers. This information is represented by :code:`LogMappings` class which contains following fields.

.. _log-mapping-label:

+------------+-------------------------------------------------------------------------------------+---------------------------------+
|Field       |Description                                                                          |Type                             |
+============+=====================================================================================+=================================+
|mappings    |List of :code:`KeyValue`, which contains informations about                          |List<KeyValue>                   |
|            |:std:ref:`replacing event params <replacing-event-params-label>`.                    |                                 |
+------------+-------------------------------------------------------------------------------------+---------------------------------+
|excludes    |If events contains a parameter key which is included in this list then this param    |List<String>                     |
|            |will be excluded from database log.                                                  |                                 |
+------------+-------------------------------------------------------------------------------------+---------------------------------+
|includes    |If events contains a parameter key which is included in this list then this param    |List<String>                     |
|            |will be included in database log. By default all parameters are included so you      |                                 |
|            |don't need to specify keys. You can use this param for example for include a         |                                 |
|            |parameter which was replaced by :code:`mappings` content.                            |                                 |
+------------+-------------------------------------------------------------------------------------+---------------------------------+

.. _replacing-event-params-label:

:code:`KeyValue` class contains following fields:

- :code:`startKey(String)`
- :code:`startValue (Object)`
- :code:`endKey (String)`
- :code:`endValue (Object)`

If we specify mappings section in :code:`LogMappings` then Db log converter will map event parameters as follows. When an event
contains parameter with key equals to :code:`startKey` and this parameter has value equal to :code:`startValue` then this
parameter is replaced by parameter with key :code:`endKey` and with value from :code:`endValue`. So we can replace some parameters
before logging an event.

DbEventLoggingService configuration
-----------------------------------

The Event Logging Module to configure default database logging uses a :code:`event-mappings.json` configuration file.

Below find an example of the :code:`event-mappings.json` configuration file:

.. code-block:: json

    [
        {
            "subjects": [
                "org.sms",
                "org.ivr"
            ],
            "mappings": [
                {
                    "status": "ok",
                    "result": "success"
                },
                {
                    "status": "error",
                    "result": "failure"
                }
            ],
            "excludes": [
                "exclude1",
                "exclude2"
            ],
            "includes": [
                "include1",
                "include2"
            ],
            "flags": [
                {
                    "keyValuePairsPresent": {
                        "requiredParameter1": "value1",
                        "requiredParameter2": "value2"
                    }
                },
                {
                    "keyValuePairsPresent": {
                        "requiredParameter3": "value3"
                    }
                }
            ]
        }
    ]

Below find a default configuration file:

.. code:: json

    [
        {
            "subjects": [
                "*"
            ]
        }
    ]

This configuration file is represented by :code:`MappingsJson` class:

+------------+-------------------------------------------------------------------------------------+---------------------------------+
|Field       |Description                                                                          |Type                             |
+============+=====================================================================================+=================================+
|subject     |List of event subjects which will be log.                                            |List<String>                     |
+------------+-------------------------------------------------------------------------------------+---------------------------------+
|mappings    |List of maps used for creating KeyValue list in :code:`LogMappings` class.           |List<Map<String, String>>        |
+------------+-------------------------------------------------------------------------------------+---------------------------------+
|excludes    |List of parameter keys which will be excluded from log.                              |List<String>                     |
+------------+-------------------------------------------------------------------------------------+---------------------------------+
|includes    |List of parameter keys which will be included in log.                                |List<String>                     |
+------------+-------------------------------------------------------------------------------------+---------------------------------+
|flags       |List of :std:ref:`ParametersPresentEventFlag <parameters-present-event-flag-label>`. |List<ParametersPresentEventFlag> |
+------------+-------------------------------------------------------------------------------------+---------------------------------+

EventLog
########

Represents a single, logged event record, that is persisted in the database. Each :code:`EventLog` contains following fields:

+------------+-----------------------------------------------------+-----------------------+
|Field       |Description                                          |Type                   |
+============+=====================================================+=======================+
|subject     |Subject of logged event.                             |String                 |
+------------+-----------------------------------------------------+-----------------------+
|parameters  |The parameters(key value pairs) of the logged event. |Map<String, Object>    |
+------------+-----------------------------------------------------+-----------------------+
|timeStamp   |Timestamp of logged event.                           |org.joda.time.DateTime |
+------------+-----------------------------------------------------+-----------------------+

Browsing Logs
#############

You can retrieving event logs using the MDS user interface, or using the :code:`DbEventQueryService` which is an OSGI service and
allows to query for log records, given certain criteria.

- :code:`getAllEventsBySubject(String subject)` - Retrieves all events that match a given subject.
- :code:`getAllEventsByParameter(String parameter, String value)` - Retrieves all events that match a given parameter and key-value pair.
- :code:`getAllEventsBySubjectAndParameter(String subject, String parameter, String value)` - Retrieves all events that match a given subject and parameter key-value pair.

############
File logging
############

Description
###########

The Event Logging Module provides possibility to store logs in files. To do this you must use :code:`FileEventLoggingService`.
By default, no :code:`FileEventLoggingService` instance is created.

FileEventLoggingService
#######################

:code:`FileEventLoggingService` is using :code:`FileEventLogger` instances to log events. When event arrives each logger added to
:code:`FileEventLoggingService` process it. Each :code:`FileEventLogger` contains list of :code:`File` objects. This list is
used to create files if they not exist and for storing information about fired events. :code:`FileEventLogger` for converting
events use :code:`DefaultFileToLogConverter`. Which stores all event parameters to file.

To enable file logging you must add :code:`@Service` annotation to the :code:`FileEventLoggingService` and implement
:code:`@PostConstruct` method. In this method you should configure file logger. You can also implement constructor which
will configure service. Last step is to register service instance by :code:`EventLoggingServiceManager`.

.. code-block:: java

    @PostConstruct
    public void configureFileLoggingService() {
        List<File> loggingFiles = new ArrayList<>();
        //Here we must add some files in which logs will be saved

        List<LoggableEvent> loggableEvents = new ArrayList<>();
        //Here we must specify some loggable events

        //creating and adding new logger
        FileEventLogger logger = new FileEventLogger(loggableEvents, loggingFiles, eventConverter);
        fileEventLoggers.add(logger);
    }

Below you can see a sample file content.

.. code-block:: txt

    EVENT: eventSubject1 at TIME: 2015-04-17T14:20:45.713+02:00 with PARAMETERS: param1/1 param2/5 param3/moduleName1
    EVENT: eventSubject2 at TIME: 2015-04-17T14:21:45.713+02:00 with PARAMETERS: param1/3 param2/value param3/moduleName2
    EVENT: eventSubject3 at TIME: 2015-04-17T14:22:45.713+02:00 with PARAMETERS: param1/4 param2/value param3/moduleName3

.. _event-flags-label:

################
Parameters Flags
################

.. _parameters-present-event-flag-label:

ParametersPresentEventFlag
##########################

This class is used by :code:`LoggableEvent` class for filter events by parameters.

+---------------------+---------------------------------------------------------+---------------------+
|Field                |Description                                              |Type                 |
+=====================+=========================================================+=====================+
|keyValuePairsPresent |Key-value pairs representing parameters and their values |Map<String, String>  |
|                     |which are required in the event to log.                  |                     |
+---------------------+---------------------------------------------------------+---------------------+

RegexEventFlag
##############

Not implemented yet.