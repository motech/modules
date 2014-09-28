.. _ivr-module:

==========
IVR Module
==========

Overview
========

The IVR module can initiate outbound IVR calls and receive call status from IVR vendors. Text templates,
typically CCXML/VXML, are stored in ``Template`` entities.  Specifics about vendors are stored in ``Config`` entities.

.. _ivr-status-request:

``/status`` REST request
========================

IVR Call status information is sent to the IVR module by the IVR provider using REST calls,
typically using CCXML's ``<send>`` element or VXML's ``<data>`` element. The IVR module stores the call status
information in ``CallDetailRecord`` entities (CDR) in the database.

REST call
---------

The IVR provider makes a ``GET`` or ``POST`` HTTP call to Motech & the IVR module:

::

    http://{motech-server}/{motech-war-file}/module/ivr/status/{config}

Where ``{motech-server}`` is the address of the Motech server, ``{motech-war-file}`` is the name of the Motech war
file, by default ``motech-platform-server`` and  ``{config}`` is the name of the configuration representing the IVR
provider.

The ``GET`` or ``POST`` parameters provide Motech with the information about the IVR call status. The following
parameters are interpreted by Motech:

    :``from``: is the phone number making the call.
    :``to``: is the phone number receiving the call.
    :``callDirection``:
        should be either ``INCOMING`` or ``OUTGOING``. Any other string will be added to the ``providerExtraData``
        map and ``callDirection`` will be ``UNKNOWN``.
    :``callStatus``:
        should be either one of ``INITIATED``, ``IN_PROGRESS``, ``ANSWERED``, ``BUSY``, ``FAILED``,
        ``NO_ANSWER``. Any other string will be added to the ``providerExtraData`` map and ``callStatus`` will be
        ``UNKNOWN``.
    :``motechCallId``:
        if available (ie: included as a parameter in the outgoing call HTTP call),
        the Motech originated GUID uniquely identifying this call.
    :``providerCallId``:
        if available, the provider's call id. For example, CCXML's ``session.id`` would be a good choice.
    :``timestamp``:
        if provided, a string representing the time at which the status is being sent, if not,
        the time at which the status is being received by Motech.

    .. note::
        Any parameters not recognized in the list above will be added to the ``providerExtraData`` map.
        Parameters listed in the config's ``ignoreStatusFields`` list are ignored. Should the IVR provider not be
        able to name some of the REST call parameters, the config's ``statusFieldMap`` can be used.

Each successful REST call results in one new ``CallDetailRecord`` database record.

Motech Event
------------

In addition to storing CDR in the database, the IVR module also sends a :doc:`../org/motechproject/event/MotechEvent`
for each IVR Call Status REST call:

    :``subject``: ivr_call_status
    :``payload``:
        ``TIMESTAMP``, ``CONFIG``, ``FROM``, ``TO``, ``CALL_DIRECTION``, ``CALL_STATUS``, ``MOTECH_CALL_ID``,
        ``PROVIDER_CALL_ID``, ``PROVIDER_EXTRA_DATA``


Motech Task Trigger
-------------------

The **ivr_call_status** event subject is also a Motech Task Trigger.

.. _ivr-template-request:

``/template`` REST request
==========================

VXML [#]_ template requests are sent to the IVR module by IVR providers using REST calls,
typically using VXML's ``<submit>`` element. Apart from returning text templates,
the :ref:`ivr-template-request` works exactly the same as the :ref:`ivr-status-request`,
it stores ``CallDetailRecord`` entities, sends Motech events and triggers Motech Tasks.

.. [#]
    Some non VXML providers, for example India's KooKoo, operate the same way except the templates are not VXML but
    generally some sort of proprietary XML.

REST call
---------

The IVR provider makes a ``GET`` or ``POST`` HTTP call to Motech & the IVR module:

::

    http://{motech-server}/{motech-war-file}/module/ivr/template/{config}

See :ref:`ivr-status-request` for additional details.

Motech Event
------------

The event sent is similar to that in :ref:`ivr-status-request` with two exceptions: the subject is
**ivr_template_request** and the event payload contains an additional ``template`` element which contains the name of
the requested template.

Motech Task Trigger
-------------------

The Motech Task Trigger is also similar to that in :ref:`ivr-status-request` with the same two exceptions as above,
a different title and an additional element to the payload.

Initiating Outbound Calls
=========================

To initiate an outbound call from an IVR provider, the IVR makes a REST call to the IVR provider. The following two
parameters are required:

    :``configName``:
        the name of the IVR provider config where ``outgoingCallUriTemplate`` specifies the IVR provider outbound
        call URI
    :``params``:
        the parameters needed by the IVR provider to make the call, eg: destination number, resource id,
        status callback URI, security credentials, etc...

    The REST call to the IVR provider is constructed by using the config's ``outgoingCallUriTemplate`` field as the
    base URI, substituting any [xxx] placeholders with the values in ``params`` and also adding ``params`` to the
    HTTP request parameters.

There are three ways to have the IVR module initiate a call.

Initiating an outbound call via an API call
-------------------------------------------

.. TODO how do I get the link to initiateCall?

Module writers can use the :ref:`org.motechproject.ivr.service.OutboundCallService.initiateCall` method.


Initiating an outbound call via a REST call
-------------------------------------------

``GET`` or ``POST`` HTTP call to:

::

    http://{motech-server}/{motech-war-file}/module/ivr/call/{config}

Where ``{config}`` is used for ``configName`` and the HTTP query parameters are used for ``params``

.. note::
    The default security rules for the ``/call`` http endpoint are ``USERNAME_PASSWORD``.


Initiating an outbound call via the :ref:`tasks`
------------------------------------------------

Create a task where the action is IVR - Initiate Call. Use the UI to specify the ``config`` and ``params`` parameters:

    .. image:: img/ivr_initiate_call_task.png
        :scale: 100 %
        :alt: IVR Module - Initiate outbound call via the Tasks Module - UI
        :align: center

Settings
========

IVR provider Configs are created in the Settings tab. Click on **Modules** / **IVR** / **Settings**:

    .. image:: img/ivr_settings.png
        :scale: 100 %
        :alt: IVR Module - Settings
        :align: center

    Configs consist of:

    * ``name``: The config name
    * ``outgoingCallMethod``: Which HTTP method to use, either ``GET`` or ``POST``.
    * ``statusFieldMap``:
      A map where each key corresponds to a field name coming from the IVR provider and each value corresponds to
      the matching IVR ``CallDetailRecord`` field.
    * ``outgoingCallUriTemplate``:
      A URI template where any [``xxx``] string will be replaced by the value identified by the ``xxx`` key in the
      provided ``params`` map. Additionally, the entire ``params`` map is added as request parameters to the HTTP
      call.
    * ``ignoredStatusFields``:
      A list of fields to be ignored when receiving IVR Call Status from the provider. All other fields received
      during IVR Call Status and not mapped to CDR fields are added to the ``providerExtraData``
      ``CallDetailRecord`` map field.

Call Detail Records
===================

Like configs, CallDetailRecord fields are viewed using the :ref:`data_services` Data Browser:

    .. image:: img/ivr_cdr.png
        :scale: 100 %
        :alt: IVR Module - Editing an existing config
        :align: center

    Call Detail Records consist of:

    * ``timestamp``: The time at which the event happened, if not supplied by the provider,
      then supplied by the IVR module.
    * ``configName``: Name of the config that this CDR pertains to.
    * ``from``: Phone number which originated the call.
    * ``to``: Phone number which received the call.
    * ``callDirection``: ``INBOUND`` or ``OUTBOUND``, relatively to the IVR module. Or ``UNKNOWN``.
    * ``callStatus``: ``MOTECH_INITIATED``, ``INITIATED``, ``IN_PROGRESS``, ``ANSWERED``, ``BUSY``, ``FAILED``,
      ``NO_ANSWER``, or ``UNKNOWN``.
    * ``templateName``: The name of the requested template. Only for ``/template`` requests.
    * ``motechCallId``: A Motech (ie IVR Module) generated GUID uniquely identifying a call.
    * ``providerCallId``: An IVR provider generated identifier, useful to query the provider (who generally has some
      kind of a web interface) about a specific call.
    * ``providerExtraData``: A map containing any additional parameter received from the IVR provider and not mapped
      to any of the above fields.
