.. _sms-module:

==========
SMS Module
==========

Description
===========

The SMS API module provides implementers with an event-driven SMS framework. Implementers can use this module on its own to send SMS messages via the user interface. This module can also be combined with other modules along with MOTECH core functionalities to create an events based SMS messaging campaign.

Setup and Configuration
=======================

Choose SMS Provider
-------------------

Choose an SMS service provider and set up an account:

* `Clickatell <http://www.clickatell.com/>`_
* `KooKoo <http://kookoo.ozonetel.com/>`_
* `Nuntium <http://instedd.org/technologies/nuntium/>`_
* `Plivo <http://www.plivo.com/>`_
* `Rancard <http://rancardmobility.com/>`_
* `Twilio <http://www.twilio.com/>`_
* `Voxeo <http://voxeo.com/>`_

SMS Provider Configurations
---------------------------

Click on Modules >> SMS >> Settings

    .. image:: img/ExampleConfiguration.png
        :scale: 50 %

Depending on the SMS service you use, the required fields will differ. Make sure that the name of your configuration contains no spaces. The authorization credentials should be provided by the SMS service provider.

Remember to save the configuration before moving on.

Configure Your Server URL
-------------------------

* Click on Admin >> Settings
* Enter your server URL, including /motech-platform-server
* Example:  http://123.456.789.00:8080/motech-platform-server

    .. image:: img/PlatformSettings.png
        :scale: 50 %

Send a Test SMS
---------------
#. Click on Modules >> SMS
#. Select which configuration you want to use
#. Enter the phone number(s) you want to use, separated by a comma
#. Enter the message body
#. Select your delivery time
#. Click on Send

    .. image:: img/SendSMS.png
        :scale: 50%

Tasks
=====

The SMS module produces the following task triggers:

* Outbound SMS - Retrying
* Outbound SMS - Aborted
* Outbound SMS - Scheduled
* Outbound SMS - Pending
* Outbound SMS - Dispatched
* Outbound SMS - Delivery Confirmed
* Outbound SM - Failure Confirmed
* Inbound SMS

These events can be used with the Tasks module to trigger actions in other modules. The SMS module also listens for the “Send SMS” event. When a task emits a send SMS event, the SMS module consumes the event, extracts the payload,and sends an SMS based on the information in the payload. See the Tasks Module documentation for more information on how to create tasks.

Events
======

All of the events used by SMS module can also be implemented programmatically when developing a new MOTECH module. The following is a list of all events emitted and consumed by the SMS module.

Events Consumed
---------------

* SmsEventSubjects.SEND_SMS
* SmsEventSubjects.PENDING
* SmsEventSubjects.SCHEDULED
* SmsEventSubjects.RETRYING
* SmsEventSubjects.INBOUND_SMS

Events Emitted
--------------

* SmsEventSubjects.PENDING
* SmsEventSubjects.SCHEDULED
* SmsEventSubjects.RETRYING
* SmsEventSubjects.ABORTED
* SmsEventSubjects.DISPATCHED
* SmsEventSubjects.DELIVERY_CONFIRMED
* SmsEventSubjects.FAILURE_CONFIRMED

API
===

The SMS module provides a single service interface for raising SendSMS events in the system. The SMS API will split a single message into multiple messages based on a maximum character count of 160. Each split message will have its own send SMS event raised in the system.

    .. image:: img/SendSmsDiagram.bmp

It is possible to listen for an inbound SMS by implementing an event handler. Use the @MotechListener annotation to listen for the inbound SMS event:

    @MotechListener(subjects = "inbound_sms")