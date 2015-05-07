.. _message-campaign-module:

=======================
Message Campaign Module
=======================

.. contents:: Table of Contents
   :depth: 3

############
Introduction
############
The MOTECH Message Campaign module is used to enroll users into messaging campaigns. A user may be a patient, worker,
lab, facility, or other desired recipient of the information to be disseminated. A campaign is a course of informational
messages that are sent to the target user on an assigned date or schedule. A user can be entered into a campaign at any
point during its duration via a Campaign Request. Users may be removed or re-enrolled into campaigns. The languages
and formats of the campaign messages can be specified (IVR, SMS, English, etc). However when a campaign message fires,
the handling of that event, including the actual messaging of the end user(IVR, SMS, etc.) is left to the implementation.
Campaign messages follow a clearly defined schedule and are automatically triggered by the scheduling system.

######################
Types of the campaigns
######################
Each campaign has a type, which is determined by an enumeration called **CampaignType**. The CampaignType determines the type
of message that will apply to the campaign. In a particular campaign, all campaign messages must be of the same campaign type.
The module contains five different campaign types: absolute, offset, repeat interval, day of week, and cron. All campaigns
contain a field for the campaign name and a list of campaign messages. Each type of campaign has additional fields
that correspond to its type specification. Each campaign message has the fields name, formats, languages, messageKey,
and startTime, along with additional fields depending on its CampaignType.

Campaign common fields
######################

Campaign fields
---------------
- **name**
  The name of the campaign. Campaign requests use this parameter to associate a user with a given campaign
- **type**
  Specifies the type of the campaign
- **messages**
  An array of messages of the campaign.

Message fields
--------------
- **name**
  The name of the campaign message
- **formats**
  The format(s) for the message
- **languages**
  The language(s) for the message
- **messageKey**
  A key uniquely identifying this message
- **startTime**
  The time at which the message will be sent.

Absolute campaign
#################
In this type of campaign all messages have absolute, predefined delivery dates and times. Absolute campaign messages
contain the additional field: date, which indicates the date that they will be sent on at the time specified by the startTime.

Example
-------

.. code-block:: json

    [{
        "name" : "Absolute Dates Message Program",
         "type" : "ABSOLUTE",
         "messages" : [
            {
              "name" : "First",
              "formats" : ["IVR", "SMS"],
              "languages" : ["en"],
              "messageKey": "random-1",
              "date" : "2015-01-15",
              "startTime" : "10:30"
            },
            {
              "name" : "Second",
              "formats" : ["IVR"],
              "languages" : ["en"],
              "messageKey": "random-2",
              "startTime" : "10:30",
              "date" : "2015-01-12"
            }
         ]
    }]

Offset campaign
###############
In this type of campaign all messages have delivery dates and times with defined delay. Offset campaigns include
an additional field: maxDuration, which is optional. This field specifies the maximum duration of the campaign.
Offset campaign messages contain the additional field timeOffset, which defines the amount of time from the reference date
or current date that will elapse before the message is sent. If no reference date is supplied, then the message campaign scheduler
uses the current date as the reference date. OFFSET campaigns allow clients to enroll into the sequence of messages
at any point within the sequence.

Example
-------

.. code-block:: json

    [{
      "name" : "Relative Dates Message Program",
      "type" : "OFFSET",
      "messages" : [
         {
         "name" : "Week 1",
         "formats" : ["IVR"],
         "languages" : ["en"],
         "messageKey": "child-info-week-1",
         "timeOffset" : "1 Week",
         "startTime" : "10:30"
         },
         {
         "name" : "Week 1A",
         "formats" : ["SMS"],
         "languages" : ["en"],
         "messageKey": "child-info-week-1a",
         "timeOffset" : "1 Week",
         "startTime" : "10:30"
          },
          {
          "name" : "Week 1B",
          "formats" : ["SMS"],
          "languages" : ["en"],
          "messageKey": "child-info-week-1b",
          "timeOffset" : "9 Days",
          "startTime" : "10:30"
          }
      ]
    }]

Repeat Interval campaign
########################
In this type of campaign all messages repeat periodically based on their interval value. Repeat interval campaigns include
an additional field: maxDuration, which is optional and specifies the maximum duration in which the messages will be repeated.
Repeating campaign messages contain the following additional message field: repeatEvery. The repeat interval combined
with the maximum duration determines how many messages will be scheduled at the time specified by the startTime.

Example
-------

.. code-block:: json

    [{
      "name" : "Relative Parameterized Dates Message Program",
        "type" : "REPEAT_INTERVAL",
        "maxDuration" : "5 weeks",
        "messages" : [
            {
                "name" : "Weekly Message #1",
                "formats" : ["IVR", "SMS"],
                "languages" : ["en"],
                "messageKey": "child-info-week-{Offset}-1",
                "repeatEvery" : "1 Week",
                "startTime" : "10:30"
            },
            {
                "name" : "Weekly Message #2",
                "formats" : ["SMS"],
                "languages" : ["en"],
                "messageKey": "child-info-week-{Offset}-2",
                "repeatEvery" : "9 Days",
                "startTime" : "10:30"
            },
            {
                "name" : "Weekly Message #3",
                "formats" : ["SMS"],
                "languages" : ["en"],
                "messageKey": "child-info-week-{Offset}-3",
                "repeatEvery" : "12 Days",
                "startTime" : "10:30"
            }
        ]
    }]

Cron campaign
#############
In this type of campaign all messages are scheduled using a cron expression. Each cron campaign message contains
an additional field that identifies a cron expression. The cron expression determines the periodic schedule the messages will follow.
Here you can find more information about cron expressions `Cron <http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger/>`_

Example
-------

.. code-block:: json

    [{
        "name" : "Cron based Message Program",
        "type" : "CRON",
        "maxDuration" : "5 weeks",
        "messages" : [
            {
                "name" : "First",
                "formats" : ["IVR", "SMS"],
                "languages" : ["en"],
                "messageKey": "cron-message",
                "cron" : "0 0 12 * * ?"
            }
        ]
    }]

Day Of Week campaign
####################
In this type of campaign all messages fire on the days of week(Monday, Tuesday, etc.) specified. Each day of week campaign
message contains an additional field: maxDuration, which specifies the maximum duration of the campaign.
Day of week campaign messages contain the additional field: repeatOn, which specifies which days of the week the message will be sent on.

Example
-------

.. code-block:: json

    [{
        "name" : "PREGNANCY",
        "type" : "DAY_OF_WEEK",
        "maxDuration" : "40 Weeks",
        "messages" : [
            {
                "name" : "Pregnancy Message",
                "formats" : ["SMS"],
                "languages" : ["en"],
                "messageKey": "PREGNANCY",
                "repeatOn" : ["Monday", "Wednesday", "Friday"],
                "startTime" : "10:30"
            }
        ]
    }]

##################
Creating campaigns
##################
There are two possibilities to create **Message Campaigns** :

Through the UI
##############
You can upload JSON files entering the **Settings** tab.

            .. image:: img/message_campaign_settings.png
                    :scale: 100 %
                    :alt: Uploading JSON file
                    :align: center

Using configuration file
########################
You can copy a file to the message-campaign directory. The name of the file has to be **message-campaigns.json**.
Message campaign records will be created after MOTECH started.

################################
Enrolling a user into a campaign
################################
To enroll a user into a campaign you have two options :

Through the UI
##############
Go to the **Campaigns** tab, choose a message campaign and add an enrollee.

            .. image:: img/message_campaign_enrollee.png
                    :scale: 100 %
                    :alt: Add enrollee
                    :align: center

Using CampaignRequest
#####################
A campaign request associates a user (worker, patient, lab, etc.) with a unique campaign name, start time, and reference date.
The campaign name determines which campaign will be retrieved or unscheduled from the JSON document. The reference date
determines the calendar date that the campaign will begin for that user. If no reference date is supplied, then the current
date upon enrollment is used in its place. The start time specifies what time of day in hours and minutes that the message
will be sent. **CampaignRequest** has following fields.

+------------------+-----------+--------------------------------------------------------------------------------------+
|Name              |Type       | Description                                                                          |
+==================+===========+======================================================================================+
|externalId        |String     |A client defined id to identify the enrollment                                        |
+------------------+-----------+--------------------------------------------------------------------------------------+
|campaignName      |String     |The campaign into which the entity should be enrolled                                 |
+------------------+-----------+--------------------------------------------------------------------------------------+
|referenceDate     |LocalDate  |The date the campaign has started for this enrollment. It can be in the past          |
|                  |           |resulting in a delayed enrollment.                                                    |
+------------------+-----------+--------------------------------------------------------------------------------------+
|startTime         |Time       |Time of the day at which the alert must be raised. This overrides the campaign's      |
|                  |           |deliverTime.                                                                          |
+------------------+-----------+--------------------------------------------------------------------------------------+

The message campaign module exposes the **org.motechproject.messagecampaign.service.MessageCampaignService** interface.
There are few methods provided for enrolling/unenrolling a user into a campaign:

.. code-block:: java

    /**
     * Enrolls the external id into the campaign as specified in the request. The enrolled entity will have events raised
     * against it according to the campaign definition.
     */
    void enroll(CampaignRequest enrollRequest);

    /**
     * Unenrolls an external from the campaign as specified in the request. The entity will no longer receive events from
     * the campaign.
     */
    void unenroll(String externalId, String campaignName);

    /**
     * Update existing campaign enrollment with data specified in the request.
     */
    void updateEnrollment(CampaignRequest enrollRequest, Long enrollmentId);

    /**
     * Unenrolls all campaigns which match criteria provided by a CampaignEnrollmentsQuery object.
     */
    void stopAll(CampaignEnrollmentsQuery query);

#######
Example
#######
Below is an example of a JSON document that includes two campaigns, each with a number of campaign messages.
Each campaign object has three fields: name, type and an array of messages. The REPEAT_INTERVAL campaign has an additional
maxDuration field. Each message contains fields determined by its type. Campaign requests associate the user with these
campaigns. A reference to the campaign's name, such as "Absolute Dates Message Program" must be included in the CampaignRequest.
This allows the system to schedule jobs for the user based on the associated campaign. In the below example,
if a user is enrolled in "Absolute Dates Message Program", two separate jobs (messages) will be scheduled.
In the "Relative Parameterized Dates Message Program", twelve jobs will be scheduled for the user. The number of repeating
messages is determined by the repeat intervals compared with the maximum duration. In the example below, Repeating Message #1
would have five scheduled messages due to a maximum duration of five weeks and repeat intervals of 1 week. Repeating Message #2
would have four scheduled messages due to a maximum duration of five weeks and repeat intervals of nine days.

.. code-block:: json

    [
      /* Absolute Campaign */
      {
        "name" : "Absolute Dates Message Program",
        "type" : "ABSOLUTE",
        "messages" : [
            {
                "name" : "First",
                "formats" : ["IVR", "SMS"],
                "languages" : ["en"],
                "messageKey": "random-1",
                "date" : "2013-06-15",
                "startTime" : "10:30"
            },
            {
                "name" : "Second",
                "formats" : ["IVR"],
                "languages" : ["en"],
                "messageKey": "random-2",
                "startTime" : "10:30",
                "date" : "2013-06-22"
            }
        ]
      },
      /* Repeat Interval Campaign */
      {
        "name" : "Relative Parameterized Dates Message Program",
        "type" : "REPEAT_INTERVAL",
        "maxDuration" : "5 weeks",
        "messages" : [
            {
                "name" : "Weekly Message #1",
                "formats" : ["IVR", "SMS"],
                "languages" : ["en"],
                "messageKey": "child-info-week-{Offset}-1",
                "repeatInterval" : "1 Week",
                "startTime" : "10:30"
            },
            {
                "name" : "Weekly Message #2",
                "formats" : ["SMS"],
                "languages" : ["en"],
                "messageKey": "child-info-week-{Offset}-2",
                "repeatInterval" : "9 Days",
                "startTime" : "10:30"
            },
            {
                "name" : "Weekly Message #3",
                "formats" : ["SMS"],
                "languages" : ["en"],
                "messageKey": "child-info-week-{Offset}-3",
                "repeatInterval" : "12 Days",
                "startTime" : "10:30"
            }
        ]
      }
    ]

####################
Querying enrollments
####################
The **org.motechproject.messagecampaign.service.MessageCampaignService** interface, in addition to its capability to
enroll/unenroll all message(s), is capable of searching Campaign Enrollment Records based on particular criteria provided by
a Campaign Enrollments Query object. This service's search() allows the client to query for enrollment records using
various criteria: external ID, status, and/or campaign name. Queries are built by adding one or more of these criteria
to the Campaign Enrollment Query's list of criteria. The clauses together define specific search criteria.
The method returns a list of matching Campaign Enrollment Records.

Examples:

.. code-block:: java

    messageCampaignService.search(new CampaignEnrollmentsQuery().havingState(CampaignEnrollmentStatus.ACTIVE))

will find all active enrollments

.. code-block:: java

    messageCampaignService.search(
		new CampaignEnrollmentsQuery()
			.withCampaignName("My Program")
			.havingState(CampaignEnrollmentStatus.ACTIVE)

will find active enrollments enrolled into "My Program" campaign.

CampaignEnrollmentsQuery provided following methods :

.. code-block:: java

    /**
     * This provides the method for the Status Criterion using which campaign enrollments are filtered based on their status
     */
    public CampaignEnrollmentsQuery havingState(CampaignEnrollmentStatus campaignEnrollmentStatus)

    /**
     * This provides the method for the ExternalId Criterion using which campaign enrollments for an ExternalId are filtered
     */
    public CampaignEnrollmentsQuery withExternalId(String externalId)

    /**
     * This provides the method for the CampaignName Criterion using which campaign enrollments belongs to a particular
     * campaign are filtered
     */
    public CampaignEnrollmentsQuery withCampaignName(String campaignName)

    /**
     * This gives all the criterion which are present in the built query
     */
    public List<Criterion> getCriteria()

     /**
     * This gives the primary criterion in the built query, which is used to fetch the results from database
     */
    public Criterion getPrimaryCriterion()

     /**
     * This gives all the criterion other than primary criterion in the built query, which are used to filter the results
     * of the primary criterion
     */
    public List<Criterion> getSecondaryCriteria()

############
Fired events
############

Message Campaign module fires following events.

+----------------------------------------------------------------+------------------------------------------------------+
|Subject                                                         |Info                                                  |
+================================================================+======================================================+
|org.motechproject.server.messagecampaign.fired-campaign-message |Fired, when a new message job of the appropriate type |
|                                                                |is scheduled                                          |
+----------------------------------------------------------------+------------------------------------------------------+

Payload :
 - EventKeys.SCHEDULE_JOB_ID_KEY (JobID),
 - EventKeys.CAMPAIGN_NAME_KEY (CampaignName),
 - EventKeys.EXTERNAL_ID_KEY (ExternalID),
 - EventKeys.MESSAGE_KEY (MessageKey).

+----------------------------------------------------------------+------------------------------------------------------+
|Subject                                                         |Info                                                  |
+================================================================+======================================================+
|org.motechproject.server.messagecampaign.campaign-completed     |Fired, when a campaign is completed                   |
+----------------------------------------------------------------+------------------------------------------------------+

Payload :
 - EventKeys.SCHEDULE_JOB_ID_KEY (JobID),
 - EventKeys.CAMPAIGN_NAME_KEY (CampaignName),
 - EventKeys.EXTERNAL_ID_KEY (ExternalID).

+----------------------------------------------------------------+------------------------------------------------------+
|Subject                                                         |Info                                                  |
+================================================================+======================================================+
|org.motechproject.server.messagecampaign.enrolled-user          |Fired, when a new user is enrolled into a campaign    |
+----------------------------------------------------------------+------------------------------------------------------+

Payload :
 - EventKeys.CAMPAIGN_NAME_KEY (CampaignName),
 - EventKeys.EXTERNAL_ID_KEY (ExternalID).

+----------------------------------------------------------------+------------------------------------------------------+
|Subject                                                         |Info                                                  |
+================================================================+======================================================+
|org.motechproject.server.messagecampaign.unenrolled-user        |Fired, when a user is unenrolled from a campaign.     |
+----------------------------------------------------------------+------------------------------------------------------+

Payload :
 - EventKeys.CAMPAIGN_NAME_KEY (CampaignName),
 - EventKeys.EXTERNAL_ID_KEY (ExternalID).

#################################
Integration with the Tasks module
#################################

Triggers
########
For the **Message Campaign** module you can create tasks with two triggers : **Send Message** and **Campaign completed**.
To do it go to the Task module, click 'New task' and you should see the Message Campaign trigger list:

            .. image:: img/message_campaign_triggers.png
                    :scale: 100 %
                    :alt: Message campaign triggers
                    :align: center

Actions
#######
In the Task module, you can also use Message Campaign as a channel and select an action you want :

                .. image:: img/message_campaign_actions.png
                    :scale: 100 %
                    :alt: Message campaign actions
                    :align: center