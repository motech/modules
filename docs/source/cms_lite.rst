.. _cms-lite-module:

===============
CMS Lite Module
===============

.. contents::
   :depth: 2

Description
===========

CMS Lite is a very simple CMS that allows for content to be entered and served. Content should be associated with a key and include attributes such as language and format. Content is provided both via API and HTTP. Content can be accessed in a single return value or streamed.

This module is typically used by a campaign module (e.g. Message Campaign or Pill Reminder) to store the audio or string content used for IVR calls or SMS messages. If a more sophisticated CMS is needed, implementers are encouraged to make use of one of the multitude of existing open source CMS solutions.

Information for Implementation
==============================

The CMS Lite module allows implementers to:

* Name content and associate it with a language
* Associate either strings or streams with content
* Test for the existence of content
* Load or stream content via API or HTTP

Content is stored in MOTECH Data Services (MDS).

OSGi Service APIs
=================

The CMS Lite module exposes its operations through an OSGi service interface called CMSLiteService. It also exports
the :java:ref:`StreamContent <org.motechproject.cmslite.api.model.StreamContent>` and
:java:ref:`StringContent <org.motechproject.cmslite.api.model.StringContent>` classes as part of its data model,
which are used in the APIs and examples below.

CMSLiteService
--------------

CMSLiteService supports storing and retrieving string and stream content, and checking whether content exists in the CMS.

.. code-block:: java

  public interface CMSLiteService {

      StreamContent getStreamContent(String language, String name) throws ContentNotFoundException;

      StringContent getStringContent(String language, String name) throws ContentNotFoundException;

      void removeStreamContent(String language, String name) throws ContentNotFoundException;

      void removeStringContent(String language, String name) throws ContentNotFoundException;

      void addContent(Content content) throws CMSLiteException;

      boolean isStreamContentAvailable(String language, String name);

      boolean isStringContentAvailable(String language, String name);

      List<Content> getAllContents();

      StringContent getStringContent(String stringContentId);

      StreamContent getStreamContent(String stringContentId);

      Byte[] getDetachedField(StreamContent instance);

      List<StringContent> getAllStringContents();

      List<StreamContent> getAllStreamContents();

      void deleteAllContents();

  }

Adding String Content
^^^^^^^^^^^^^^^^^^^^^

.. code-block:: java

  StringContent stringContent = new StringContent("language", "name", "value");
  cmsLiteService.addContent(stringContent);

Adding Stream Content
^^^^^^^^^^^^^^^^^^^^^

.. code-block:: java

  // It's assumed that you have created inputStream as an instance of java.io.InputStream
  StreamContent streamContent = new StreamContent("language", "name", inputStream, "checksum", "audio/x-wav");
  cmsLiteService.addContent(streamContent);

Testing Whether Content Exists
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: java

  if (cmsLiteService.isStreamContentAvailable("language", "name")) {
      // Content exists
  }

Retrieving content through HTTP
===============================

Content stored in the cms-lite module can be retrieved by doing a single HTTP GET request. Two HTTP endpoints are exposed -
one for string content, the other one for stream content. You need to provide the language and name of the desired resource in the url.

* String content:

    ::

        http://{motech-server}/module/cmsliteapi/string/{language}/{name}

* Stream content:

    ::

        http://{motech-server}/module/cmsliteapi/stream/{language}/{name}


.. note::

    There are no special security rules defined for these endpoints, meaning that the client must handle authenticating
    with MOTECH in order to be able to use them. You can set up security rules that will disable the security for these
    endpoints in case for example your IVR requires to retrieve them without auth. Doing that will make them publicly
    available though, so use your own judgement regarding this. Check the :std:ref:`Security Rules <security_rules>`
    documentation for more info on setting up custom security rules.

Events Consumed and Emitted
===========================

This module does not consume or emit any events.

Roles and Permissions
=====================

This module does not define any roles or permissions.
