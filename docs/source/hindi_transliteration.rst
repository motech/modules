.. _hindi-transliteration-module:

============================
Hindi Transliteration Module
============================

.. contents::
    :depth: 1

############
Description
############

The Hindi Transliteration module allows you to transliterate English words to Hindi using ITRANS encoding.
`ITRANS <https://en.wikipedia.org/wiki/ITRANS>` is a case-sensitive encoding, implying that transliterated names may not be capitalized.
Because of that, the best results would be achieved by passing data in all lower case.

#################
OSGi Service API
#################

The Hindi Transliteration module exposes an OSGi service :code:`org.motechproject.transliteration.hindi.service.TransliterationService`
which allows to transliterate English words to Hindi. The service provides the following API:

- :code:`String transliterate(String data)` - transliterates the provided English string to Hindi.