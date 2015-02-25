.. _ivr-module:

==========
IVR Module
==========

Overview
========

    The IVR module can initiate outbound IVR calls and receive call status from IVR vendors. Text templates,
    typically CCXML/VXML, are stored in ``Template`` entities.  Specifics about vendors are stored in ``Config``
    entities.

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

    In addition to storing CDR in the database, the IVR module also sends a
    :java:ref:`org.motechproject.event.MotechEvent` for each IVR Call Status REST call:

        :``subject``: ivr_call_status
        :``payload``:
            ``TIMESTAMP``, ``CONFIG``, ``FROM``, ``TO``, ``CALL_DIRECTION``, ``CALL_STATUS``, ``MOTECH_CALL_ID``,
            ``PROVIDER_CALL_ID``, ``PROVIDER_EXTRA_DATA``

Motech Task Trigger
-------------------

    The **ivr_call_status** event subject is also a Motech Task Trigger.

``/template`` REST request
==========================

    Text [#]_ template requests are sent to the IVR module by IVR providers using REST calls,
    typically using VXML's ``<submit>`` element. Apart from returning text templates,
    this REST request works exactly the same as the :ref:`ivr-status-request`,
    it stores ``CallDetailRecord`` entities, sends Motech events and triggers Motech Tasks.

    .. [#]
        Text or CCXML or VXML or anything really. Some non VXML providers like, for example,
        India's `KooKoo <http://kookoo.in>`_, operate the same way except the templates are not VXML but
        generally some sort of proprietary XML.

Velocity
--------

    Essentially, `Velocity <http://velocity.apache.org/engine/devel/user-guide.html>`_ is a template engine which
    enables you to dynamically return text. Here's a simple template named ``hello``:

    ::

        Hello, $world.

    Assuming Motech is running on your local machine and you created an IVR config named ``voxeo``, you can get the IVR
    module to return the template by CURLing:

    ::

        $curl -w "\n" "http://localhost:8080/motech-platform-server/module/ivr/template/voxeo/hello"
        Hello, $world.
        $

    Nothing special, eh? But now add a query parameter named ``world`` with some value:

    ::

        $curl -w "\n" "http://localhost:8080/motech-platform-server/module/ivr/template/voxeo/hello?world=Frank"
        Hello, Frank.
        $

    All query parameters are available. But wait, there's more! Read on...

The ``$dataServices`` element
-----------------------------

    In addition to query parameters, the special element ``$dataServices`` [#]_ is available inside your templates. It
    can be used to query the :ref:`database <data_services>` using the following methods:

    * ``findOne(entityClassName, lookupName, params)``: returns one entity instance
    * ``findMany(entityClassName, lookupName, params)``: returns a list of entity instances
    * ``count(entityClassName, lookupName, params)``: returns a number of entity instances
    * ``retrieveAll(entityClassName)``: returns all instances of an entity
    * ``countAll(entityClassName)``: returns the number of all instances of an entity

    .. [#] Using a query parameter named ``$dataServices`` is not a good idea and will produced undefined results.

    The methods above use the following arguments:
        :entityClassName: the fully qualified class name for that entity, for example
          for a DDE [#]_ ``org.motechproject.ivr.domain.CallDetailRecord`` or for a EUDE [#]_ named ``Patient`` :
          ``org.motechproject.mds.entity.Patient``
        :lookupName: the name [#]_ of the lookup to use
        :params: a map containig zero or more key:value pairs corresponding to the arguments required by the
          given lookup, see how to use a map in the following sample template.

        .. [#] Developer Defined Entity
        .. [#] End User Defined Entity
        .. [#] Don't confuse the lookup name (ie: 'Find by name') with the lookup method name (ie: 'findByName').

    So, let's say, for example, we created a ``Patient`` MDS entity with a ``name`` and a ``number`` field and a
    'Lookup by Number' lookup which takes a ``number`` argument. The following template would extract the name of the
    patient whose number is '123':

    ::

        Hello, $dataServices.findOne("org.motechproject.mds.entity.Patient", "Lookup by Number", {"number" : "123"}).name

Injecting custom services
-------------------------

    Not only the ``$dataServices`` element can be used in IVR templates. It is also possible to inject any arbitrary
    OSGi service into the Velocity context. All services configured in the ``servicesMap`` field of the :std:ref:`configuration <config>`
    field will be available to the template executed with that configuration. All these services will be injected
    as variables, so for example if your configuration is as follows: ``myService:org.example.service.MyService``, then
    ``org.example.service.MyService`` will be available as ``$myService`` in the Velocity template.

REST call
---------

    The IVR provider makes a ``GET`` or ``POST`` HTTP call to Motech & the IVR module:

    ::

        http://{motech-server}/{motech-war-file}/module/ivr/template/{config}

    See :ref:`ivr-status-request` for additional details.

Motech Event
------------

    The event sent is similar to that in :ref:`ivr-status-request` with two exceptions: the subject is
    **ivr_template_request** and the event payload contains an additional ``template`` element which contains the name
    of the requested template.

Motech Task Trigger
-------------------

    The Motech Task Trigger is also similar to that in :ref:`ivr-status-request` with the same two exceptions as above,
    a different title and an additional element, you guessed it: the template name,  to the payload.

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

    Module writers can use the :java:ref:`org.motechproject.ivr.service.OutboundCallService` ``initiateCall`` method.


Initiating an outbound call via a REST call
-------------------------------------------

    ``GET`` or ``POST`` HTTP call to: ``http://{motech-server}/{motech-war-file}/module/ivr/call/{config}``

    Where ``{config}`` is used for ``configName`` and the HTTP query parameters are used for ``params``

    .. note:: The default security rules for the ``/call`` http endpoint are ``USERNAME_PASSWORD``.


Initiating an outbound call via the :ref:`tasks`
------------------------------------------------

    Create a task where the action is IVR - Initiate Call. Use the UI to specify the ``config`` and ``params``
    parameters:

    .. image:: img/ivr_initiate_call_task.png
        :scale: 100 %
        :alt: IVR Module - Initiate outbound call via the Tasks Module - UI
        :align: center

.. _config:

Settings
========

    IVR provider Configs are created in the Settings tab. Click on **Modules** / **IVR** / **Settings**:

        .. image:: img/ivr_settings.png
            :scale: 100 %
            :alt: IVR Module - Settings
            :align: center

        Configs consist of:

        * ``name``: The config name
        * ``authenticationRequired``:
            Select if the IVR provider requires authentication headers when initiating outbound calls.
        * ``username``:
            Optional username for providers that require authentication.
        * ``password``:
            Optional password for providers that require authentication.
        * ``outgoingCallMethod``: Which HTTP method to use, either ``GET`` or ``POST``.
        * ``statusFieldMap``:
            A map where each key corresponds to a field name coming from the IVR provider and each value corresponds to
            the matching IVR ``CallDetailRecord`` field.
        * ``outgoingCallUriTemplate``:
            A URI template where any ``[xxx]`` string will be replaced by the value identified by the ``xxx`` [#]_ key in
            the provided ``params`` map. Additionally, the entire ``params`` map is added as request parameters to the
            HTTP call.
        * ``ignoredStatusFields``:
            A list of fields to be ignored when receiving IVR Call Status from the provider. All other fields received
            during IVR Call Status and not mapped to CDR fields are added to the ``providerExtraData``
          ``CallDetailRecord`` map field.
        * ``servicesMap``:
            A map (in the "key1: value1, key2: value2" notation) of services that can be injected
            in Velocity templates where key is the name used in Velocity and value is the class of the OSGi service, for example
            to inject ``org.motechproject.mds.service.EntityService`` as ``entityService``, use ``entityService: org.motechproject.mds.service.EntityService``
        * ``jsonResponse``:
            Select if the provider returns JSON data after placing an outbound call.

        .. [#] Note: no square brackets

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
