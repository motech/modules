.. _atom-client-module:

==================
Atom Client Module
==================

Description
===========

This module will periodically fetch from one or more `Atom feed <https://en.wikipedia.org/wiki/Atom_%28standard%29>`_ sources and send a MOTECH event for each feed content item that changed since the last fetch.

The fetch job is triggered by a MOTECH event with the ``org.motechproject.atomclient.fetch`` subject.
The module can periodically send a fetch event based on the schedule in the cron expression in the ``atom-client-defaults.properties`` file.
Alternatively, a fetch job can be triggered as a task action or directly using the :java:ref:`org.motechproject.atomclient.service.AtomClientService` interface.


Configuration
=============

Atom Feeds
----------

The module will fetch from the Atom feed(s) listed in the atom-client-feeds.json file, for example:

.. code-block:: json

    {
      "feeds" :
        [
          {
            "url" : "http://test1.com/openmrs/ws/atomfeed/patient/recent",
            "regex" : "/([0-9a-f-]*)\\?"
          },
          {
            "url" : "http://servertwo:8080/openmrs/ws/atomfeed/Encounter/recent",
            "regex" : ""
          }
        ]
    }

The ``url`` element is the feed URL and the ``regex`` element, if non blank, is a `regular expression <https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html>`_ containing one capture block that will be used to extract a subsection of the Atom feed entry content. In the example above, the first feed will be fetched from ``http://test1.com/openmrs/ws/atomfeed/patient/recent`` and any content returned will be extracted according to the regular expression ``/([0-9a-f-]*)\\?``.

Fetch job cron
--------------

If one is provided, the module will periodically fetch on a `cron expression schedule <http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger>`_ specified by the ``atomclient.feed.cron`` property in the ``atom-client-defaults.properties`` file:

::

    atomclient.feed.cron=0/10 * * * * ?

The example above would schedule a fetch job every 10 seconds.

.. _tasks-label:
Tasks
=====

When fetching and for each feed content item that changed since the last time a fetch occurred, the Atom Client module will produce a ``Feed Change`` task trigger with the following payload data:


    ``url``
        The URL identifying the Atom feed

    ``published_date``
        When the content item was created

    ``updated_date``
        When the content item was modified

    ``raw_content``
        A map of complete content data, like {"1": "raw content data one", "2": "raw content data two"}

    ``extracted_content``
        If a valid non blank regex was specified in the feed config, extracted_content will contain a map of extracted content values, so for example if the regex specified to extract the first three characters of the raw content above, the map would be: {"1": "raw", "2": "raw"}

It also responds to the ``Fetch!`` task action by, you got it, fetching the Atom feed(s).


Events
======

Events Consumed
---------------

    *``org.motechproject.atomclient.service.impl.Constants.FETCH_MESSAGE``*
        Will fetch from the configured feed(s), if any.

    *``org.motechproject.atomclient.service.impl.Constants.RESCHEDULE_FETCH_JOB``*
        Used internally, reschedules the fetch cron job based on the cron job value currently in memory without having read the cron job value from the properties file.

    *``org.motechproject.config.core.constants.ConfigurationConstants.FILE_CHANGED_EVENT_SUBJECT``*
        If ``atom-client-feeds.json`` has changed, reloads the atom feed list, otherwise if ``atom-client-defaults.properties`` has changed, reschedules (or cancels) the fetch cron job.

Events Emitted
--------------

    *``org.motechproject.atomclient.service.impl.Constants.FEED_CHANGE_MESSAGE``*
        Emitted every time a feed content item has changed since the last fetch, see the :ref:`tasks <tasks-label>` payload data above.

API
===

The :java:ref:`org.motechproject.atomclient.service.AtomClientService` interface exposes a `fetch()` method which will direct the module to fetch from the configured Atom feed(s).
All other publicly exposed methods are used for configuration.
