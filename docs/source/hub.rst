.. _hub-module:

==========
Hub Module
==========

.. contents::
    :depth: 3

############
Introduction
############

The Hub module is an implementation of the PubSubHubbub Hub specification (PubSubHubbub is a simple publication - subscription
protocol). MOTECH Hub implementation exposes API for publishers and subscribers, allowing publishing and distributing
content to all interested subscribers when subscribed resources are updated. MOTECH Hub allows any external module
can subscribe to a specific resource and be notified when it changes. Any module can also act as publisher to make content
available for other modules.

#####
Setup
#####

Setup fields
------------

In order to use MOTECH Hub module hub base url have to be set before performing any subscribe/unsubscribe/publish requests.
It can be done by REST call or by Hub module UI configuration panel. UI configuration panel also offers possibility to
set retry count and retry interval parameters. For explanation of these parameters please look at the table below.

+---------------+-----------------------------------------------------------------------------------------------------+
|Parameter      |Description                                                                                          |
+===============+=====================================================================================================+
|hubBaseUrl     |The base URL of the hub server. Will be used while distributing the updated content of the topic to  |
|               |the subscribers, as part of the Link Header.                                                         |
+---------------+-----------------------------------------------------------------------------------------------------+
|retry.count    |The number of request send retries that Hub module will take in case of failed hub request.          |
+---------------+-----------------------------------------------------------------------------------------------------+
|retry.interval |The time between request retries in case of failed hub request.                                      |
+---------------+-----------------------------------------------------------------------------------------------------+

Configure via UI
----------------

To configure Hub module by UI run MOTECH with Hub module installed and in Admin panel go to Manage Modules. Click
Settings in the Action column for Hub module. Now you can set values for fields described above. After configuration is done
choose "Submit" or "Submit and restart".

.. note::

    Choosing "Sumbit" or "Submit and restart" is enough to change ``hubBaseUrl``, but the publishers should be informed about
    the change in the Hub base URL, so they still can inform it when their topics are updated.

Configure by REST CALL
----------------------

To configure hub base url by REST send HTTP ``POST`` call to:

 ::

    http://{motech-server}/{motech-war-file}/module/hub/settings

with content type set to ``application/json`` and request body JSON structured as shown below:

.. code-block:: json

    {
        "settings": {
            "hubBaseUrl": "your_url"
        }
    }

.. note::

    Using REST you can set only ``hubBaseUrl`` parameter. If you need to change ``retry.count`` or ``retry.interval``
    you have to use the UI.

#########################
Subscribing/Unsubscribing
#########################

Subscribing/Unsubscribing process
---------------------------------

To make a subscription the subscriber first has to send subscription request to the hub. Input parameters are validated
by ``HubValidator`` and if validation passes without error then the Hub responds to the subscriber with an HTTP status
``202 Accepted``. This is done to acknowledge subscriber that his subscription request has reached the Hub. After that the
Hub starts intent verification in order to prevent an attacker from creating unwanted subscriptions or unsubscribing from
desired ones.

The subscriber must confirm that he want to make pending subscription. The hub sends HTTP ``GET`` request with ``hub.challenge``
parameter in it to the subscriber. The subscriber must respond with an HTTP success (2xx) code with a response
body equal to the ``hub.challenge`` parameter. The subscription is made if described process will pass without any failures.

Validation and intent verification also apply for unsubscribing.

After the subscription is made the subscriber will be notified about changes to the resource on
his ``hub.callback`` URL, defined when subscription request was sent. Notifications will be turned off after successful
unsubscribe request or when hub.lease_seconds pass.

Please look at the end of this section to find description of all fields used in subscribing/unsubscribing requests.

REST CALLs
----------

One of two methods to make (un)subscription is HTTP REST call. To make (un)subscription send HTTP ``POST`` request to:

    ::

        http://{motech-server}/{motech-war-file}/module/hub/?hub.callback={callbackUrl}&hub.mode={hubMode}&hub.topic={topic}&hub.lease_seconds={lease_seconds}&hub.secret={secret}

with content type set to ``application/x-www-form-urlencoded`` and ``{hubMode}`` to one of two literal strings "subscribe"
or "unsubscribe" depending on the goal of the request. For detailed description of parameters refer to the table at
the end of this section.

The Hub will respond to URL passed as a callback parameter with HTTP ``GET`` request with the following query string
arguments appended:

    - :code:`hub.mode`: The literal string "subscribe" or "unsubscribe", which matches the value from original request.
    - :code:`hub.topic`: The topic URL given in the corresponding subscription request.
    - :code:`hub.challenge`: A hub-generated random string used for intent verification.

To confirm (un)subscription the subscriber must respond with an HTTP success code (2xx) with a response body equal to the
``hub.challenge`` parameter.

OSGi Services
-------------

The Hub module exposes OSGi ``SubscriptionService`` for subscribing:

.. code-block:: java

    public interface SubscriptionService {

        Long subscribe(String callbackUrl, Modes hubMode, String topic,
                String leaseSeconds, String secret) throws HubException;

    }

To subscribe with this service just call subscribe method passing all the parameters. This method returns id of the thread
which will verify the intent of the subscriber requesting subscription. If hubMode is ``unsubscribe`` the thread
won't be created and the method will return null. Detailed description of the parameters is shown in table below.

Parameters description
----------------------

Find description of the parameters used in (ub)subscribing in the table below:

+---------------+----------------+------------------------------------------------------------------------------------+
|Parameter      |OSGi Service    |Description                                                                         |
|               |parameter type  |                                                                                    |
+===============+================+====================================================================================+
|callbackUrl    |String          |The subscriber's callback URL. Notifications about updates in subscribed topic will |
|               |                |be delivered to this URL.                                                           |
+---------------+----------------+------------------------------------------------------------------------------------+
|hubMode        |Modes           |In REST call this parameter have to be either literal "subscribe" or "unsubscribe"  |
|               |                |string. In OSGi service enum ``Modes`` class is used, so it can take  a value of    |
|               |                |``Modes.SUBSCRIBE`` or ``Modes.UNSUBSCRIBE``. The mode represents the goal of the   |
|               |                |request.                                                                            |
+---------------+----------------+------------------------------------------------------------------------------------+
|topic          |String          |The topic URL that the subscriber wishes to subscribe to or unsubscribe from. Topic |
|               |                |is an URL to any resource that notifies the Hub about updates.                      |
+---------------+----------------+------------------------------------------------------------------------------------+
|leaseSeconds   |String          |Number of seconds for which the subscriber would like to have the subscription      |
|               |                |active. After this time pass subscription needs to be renewed.                      |
+---------------+----------------+------------------------------------------------------------------------------------+
|secret         |String          |A subscriber-provided secret string that will be used to compute an HMAC digest for |
|               |                |authorized content distribution.                                                    |
+---------------+----------------+------------------------------------------------------------------------------------+
|hub.challenge  |                |The parameter sent by the Hub in GET request in process of intent verification.     |
|               |                |The subscriber must respond with HTTP success (2xx) status with challenge parameter |
|               |                |in response body to confirm (un)subscription.                                       |
+---------------+----------------+------------------------------------------------------------------------------------+

##########
Publishing
##########

Whenever there is an update in topic, the publisher notifies the hub providing the resource URL (topic) which
is updated. Then hub fetches the updated content and distributes it to all the active subscribers of that topic.

REST CALL
---------

To notify about update by REST call send HTTP ``POST`` request to

    ::

        http://{motech-server}/{motech-war-file}/module/hub/?&hub.mode={hubMode}&hub.url={url}

with content type set to ``application/x-www-form-urlencoded``, ``{hubMode}`` to literal string "publish" and
``{url}`` to updated topic URL.

OSGI Service
------------

The Hub module exposes OSGi ``ContentDistributionService`` for publishing:

.. code-block:: java

    public interface ContentDistributionService {

        void distribute(String url);

    }

To publish with this service update your topic, then just call distribute method providing updated topic URL.

##############
Error handling
##############

Errors that occur when using Hub module are wrapped in a custom ``HubException`` exception class. This class contains
``reason`` field that contains description of what caused failure and ``hubErrors`` field, which contains ``HubError``
object.

``HubError`` object contains more detailed information about error, which at least are ``message``, ``code`` and ``httpStatus``.
Please refer to table shown below for detailed description of these fields.

+------------------+-----------+--------------------------------------------------------------------------------------+
|Name              |Type       | Description                                                                          |
+==================+===========+======================================================================================+
|reason            |String     |Reason for which the request failed. It may be include in the hub response as a       |
|                  |           |``hub.reason`` parameter.                                                             |
+------------------+-----------+--------------------------------------------------------------------------------------+
|hubErrors         |HubErrors  |Object extending HubErrors interface defining ``getMessage()``, ``getCode()`` and     |
|                  |           |``getHttpStatus()`` methods. These fields contains detailed information about error.  |
|                  |           |Please find description of them in rows below.                                        |
+------------------+-----------+--------------------------------------------------------------------------------------+
|message           |String     |Short message describing an error.                                                    |
+------------------+-----------+--------------------------------------------------------------------------------------+
|code              |int        |Custom error code for this error. These are error codes defined in Hub module:        |
|                  |           |    * 1001 - One or more input parameter(s) may be wrong.                             |
|                  |           |    * 1002 - Subscription not found.                                                  |
|                  |           |    * 1003 - Topic not found.                                                         |
+------------------+-----------+--------------------------------------------------------------------------------------+
|httpStatus        |HttpStatus |Http status associated with an error.                                                 |
+------------------+-----------+--------------------------------------------------------------------------------------+