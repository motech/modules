.. _openmrs-module:

==============
OpenMRS Module
==============

.. contents::
   :depth: 3

The OpenMRS module allows MOTECH to integrate with an OpenMRS Platform instance (version 1.9 and higher), which is the core OpenMRS system, running the OpenMRS Reference Application and derivative works like `Bahmni <http://www.bahmni.org/>`_. From a developer perspective, the module provides data models and basic services to represent OpenMRS elements in MOTECH. For the end user, the module presents itself in the tasks module with numeruos data sources and actions (Specific list depends on version of the OpenMRS server).

About OpenMRS
-------------

"`OpenMRS <http://www.openmrs.org>`_ is a software platform and a reference application which enables design of a customized medical records system with no programming knowledge (although medical and systems analysis knowledge is required). It is a common platform upon which medical informatics efforts in developing countries can be built. The system is based on a conceptual database structure which is not dependent on the actual types of medical information required to be collected or on particular data collection forms and so can be customized for different uses." (`Source <http://openmrs.org/about/>`_)

Feature List
------------
Below is a list of features available in the OpenMRS module

- Configure the server URL through the UI
- Map a patient identifier between MOTECH and OpenMRS to support matching patients across multiple systems
- Query for patients, providers, encounters, relationships and program enrollments in a tasks data source
- Create patients from a MOTECH task action
- Create encounters from a MOTECH task action
- Create progrm enrollments from a MOTECH task action
- Update person from a MOTECH task action
- Update patient identifiers from a MOTECH task action
- Change state of program enrollment from a MOTECH task action

Setup, Configuration and Connection to OpenMRS
----------------------------------------------
Full setup and configuration instructions are available in our `implementer's getting started page <http://docs.motechproject.org/en/latest/get_started/connect_openmrs.html>`_.

Testing with curl
-----------------
We recommend that you become familiar with the OpenMRS Web Services API endpoints to be able to fully utilize the Tasks data source and actions. This can be done by using curl or online curl tools like `hurl.it <https://www.hurl.it/>`_ to see what's returned by the server. To make this easier, future versions of OpenMRS will integrate `Swagger documentation <http://swagger.io>`_ for the Web Service API. Here are some sample curl statements to get you started

**Get a Patient**

.. code-block:: shell

    curl -u admin:Admin123 http://demo.openmrs.org/openmrs/ws/rest/v1/patient/?q=Johnson

**Get a list of All Encounters for a Patient**

.. code-block:: shell

    curl -u admin:Admin123 http://demo.openmrs.org/openmrs/ws/rest/v1/encounter?patient=deb0905c-3b82-4631-88b2-b71425755cdf

**Get a Single Encounter by UUID**

.. code-block:: shell

    curl -u admin:Admin123 http://demo.openmrs.org/openmrs/ws/rest/v1/encounter/ff341b3e-2553-4ad7-aa9b-3ccbcd72cf59

**Get a Provider by UUID**

.. code-block:: shell

    curl -u admin:Admin123 http://demo.openmrs.org/openmrs/ws/rest/v1/provider/588870c5-22b5-4779-8fb0-666723a244bd


Tasks Integration
-----------------
The OpenMRS module currently acts as a data source and task action. This allows you to query OpenMRS for supplemental information and create patients or encounters when new task triggers are fired. For example, you could create a patient in OpenMRS when a Commcare registration form arrives in MOTECH.

Task Data Source
^^^^^^^^^^^^^^^^
The Tasks module queries the OpenMRS Web Services API and makes the results available to the task. These data sources are useful when supplemental information is needed from a task trigger such as patient, encounter and provider information. Click Add data source in the task and choose Source: OpenMRS to make this information available to the task. Each of the following objects are available as a data source. Please reference the `OpenMRS web services documentation <https://wiki.openmrs.org/display/docs/REST+Web+Service+Resources+in+OpenMRS+1.9>`_ for a description of each API call.

- Patient (Lookup by MOTECH ID or UUID)
    This allows you to lookup patient information by MOTECH Id or OpenMRS UUID. Returned fields include Patient UUID, Patient identifiers, MotechId, Person UUID, person display (name), person address (text field), Person age, Person birthDateEstimated flag, Person date of birth, Person date of death, Person created date, Person changed date, New person flag, Person gender and Person dead flag. **Note that New person flag is a 'yes' 'no' string that can be used as a filter.**
- Encounter (Lookup by Encounter UUID)
    This allows you to lookup encounter information by UUID. Returned fields include Encounter UUID, Encounter display (encounter type with date), Encounter date, Encounter type, Provider UUID, Provider display (name), Location UUID, Location display, Patient UUID and Patient display (name).
- Provider (Lookup by UUID)
    This allows you to lookup provider information by UUID. Simply put a provider is a wrapper around the OpenMRS Person with an identifier and name. Returned fields include Provider UUID, Provider identifier, Person UUID, Person display.
- Relationship (Lookup by Person UUID and Relationship Type UUID)
    This allows you to lookup relationship information by Person UUID and Relationship Type UUID. Returned fields include Relationship UUID, Person A UUID, Person B UUID, Start date, End date.
- Program Enrollment (Lookup by 'Patient MOTECH ID and Program Name' or 'Patient UUID and Program Name') OpenMRS v1.10+ only
    This allows you to lookup program enrollment information by Patient MOTECH ID and Program Name or Patient UUID and Program Name. Returned fields include Program Enrollment UUID, Patient UUID, Program UUID, 
Enrolled Date, Completed Date, Location Name, Enrolled, Current State UUID.

Task Actions
^^^^^^^^^^^^
The following OpenMRS task actions are available:

- Create Patient
    This task action allows you to create patients in OpenMRS. The task uses the POST command to the OpenMRS Rest Web Services module. Below is a description of each field in the action.

    +-------------------------+------------------------------------------------------------+
    |Field                    |Description                                                 |
    +=========================+============================================================+
    |Person given name        |The given name of the person. (required)                    |
    +-------------------------+------------------------------------------------------------+
    |Person middle name       |The middle name of the person.                              |
    +-------------------------+------------------------------------------------------------+
    |Person family name       |The family name of the person. (required)                   |
    +-------------------------+------------------------------------------------------------+
    |Address 1                |The first row of the person's address.                      |
    +-------------------------+------------------------------------------------------------+
    |Address 2                |The second row of the person's address.                     |
    +-------------------------+------------------------------------------------------------+
    |Address 3                |The third row of the person's address.                      |
    +-------------------------+------------------------------------------------------------+
    |Address 4                |The fourth row of the person's address.                     |
    +-------------------------+------------------------------------------------------------+
    |Address 5                |The fifth row of the person's address.                      |
    +-------------------------+------------------------------------------------------------+
    |Address 6                |The sixth row of the person's address.                      |
    +-------------------------+------------------------------------------------------------+
    |Address City/Village     |The city/village from the person's address.                 |
    +-------------------------+------------------------------------------------------------+
    |Address State/Province   |The state/province from the person's address.               |
    +-------------------------+------------------------------------------------------------+
    |Address Country          |The country from the person's address                       |
    +-------------------------+------------------------------------------------------------+
    |Address Postal Code      |The postal code from the the person's address.              |
    +-------------------------+------------------------------------------------------------+
    |Address County District  |The county district from the person's address.              |
    +-------------------------+------------------------------------------------------------+
    |Address Latitude         |The latitude from the the person's address.                 |
    +-------------------------+------------------------------------------------------------+
    |Address Longitutde       |The longitutde from the person's address.                   |
    +-------------------------+------------------------------------------------------------+
    |Address Start Date       |The start date of the the person's address.                 |
    +-------------------------+------------------------------------------------------------+
    |Address End Date         |The end date of the person's address.                       |
    +-------------------------+------------------------------------------------------------+
    |Person date of birth     |The date of birth of the person, if known. If not known,    |
    |                         |click the Person birth Date Estimated flag below and add an |
    |                         |age.                                                        |
    +-------------------------+------------------------------------------------------------+
    |Person birthDateEstimated|Click 'Yes' if the age is estimated and add the age in the  |
    |flag                     |box to the right. Otherwise, click 'No'                     |
    +-------------------------+------------------------------------------------------------+
    |Person gender            |Enter the gender. The OpenMRS default is "M" or "F".        |
    |                         |(required)                                                  |
    +-------------------------+------------------------------------------------------------+
    |Person dead flag         |Required by OpenMRS. Choose 'Yes' if the person is dead and |
    |                         |enter the date they died.                                   |
    +-------------------------+------------------------------------------------------------+
    |Cause of death UUID      |UUID of the OpenMRS concept that caused the death.          |
    |                         |(i.e. 114100AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA - Pneumonia      |
    +-------------------------+------------------------------------------------------------+
    |MotechId                 |The ID value from MOTECH that you would like entered into   |
    |                         |OpenMRS. (required)                                         |
    +-------------------------+------------------------------------------------------------+
    |Location name            |The name of the OpenMRS location where the patient was      |
    |                         |created. (i.e. Registration Desk)                           |
    +-------------------------+------------------------------------------------------------+
    |Patient Identifiers      |The extra patient Identifiers you would like added as       |
    |                         |defined in the openmrs.identifierTypes property.            |
    +-------------------------+------------------------------------------------------------+

- Create Encounter
    This task action allows you to create patient encounters in OpenMRS. The task uses the POST command to the OpenMRS Rest Web Services module. Below is a description of each field in the action.

    +-------------------------+------------------------------------------------------------+
    |Field                    |Description                                                 |
    +=========================+============================================================+
    |Encounter Date           |The date and time the encounter was created. (required)     |
    +-------------------------+------------------------------------------------------------+
    |Encounter Type           |The name of the OpenMRS encounter type. (required)          |
    +-------------------------+------------------------------------------------------------+
    |Location name            |The name of the location where the encounter took place.    |
    |                         |Must exactly match the OpenMRS location name.               |
    +-------------------------+------------------------------------------------------------+
    |Patient UUID             |The UUID of the patient to which this encounter will be     |
    |                         |applied. (required)                                         |
    +-------------------------+------------------------------------------------------------+
    |Provider UUID            |The UUID of the provider in OpenMRS to whom this encounter  |
    |                         |will be applied. (required)                                 |
    +-------------------------+------------------------------------------------------------+


- Update Person
    This task action allows you to update person in OpenMRS. The task uses the POST command to the OpenMRS Rest Web Services module. Below is a description of each field in the action.

    +-------------------------+------------------------------------------------------------+
    |Field                    |Description                                                 |
    +=========================+============================================================+
    |Person UUID              |The UUID of the person which will be updated. (required)    |
    +-------------------------+------------------------------------------------------------+
    |Person given name        |The given name of the person. (required)                    |
    +-------------------------+------------------------------------------------------------+
    |Person middle name       |The middle name of the person.                              |
    +-------------------------+------------------------------------------------------------+
    |Person family name       |The family name of the person. (required)                   |
    +-------------------------+------------------------------------------------------------+
    |Address 1                |The first row of the person's address.                      |
    +-------------------------+------------------------------------------------------------+
    |Address 2                |The second row of the person's address.                     |
    +-------------------------+------------------------------------------------------------+
    |Address 3                |The third row of the person's address.                      |
    +-------------------------+------------------------------------------------------------+
    |Address 4                |The fourth row of the person's address.                     |
    +-------------------------+------------------------------------------------------------+
    |Address 5                |The fifth row of the person's address.                      |
    +-------------------------+------------------------------------------------------------+
    |Address 6                |The sixth row of the person's address.                      |
    +-------------------------+------------------------------------------------------------+
    |Address City/Village     |The city/village from the person's address.                 |
    +-------------------------+------------------------------------------------------------+
    |Address State/Province   |The state/province from the person's address.               |
    +-------------------------+------------------------------------------------------------+
    |Address Country          |The country from the person's address                       |
    +-------------------------+------------------------------------------------------------+
    |Address Postal Code      |The postal code from the the person's address.              |
    +-------------------------+------------------------------------------------------------+
    |Address County District  |The county district from the person's address.              |
    +-------------------------+------------------------------------------------------------+
    |Address Latitude         |The latitude from the the person's address.                 |
    +-------------------------+------------------------------------------------------------+
    |Address Longitutde       |The longitutde from the person's address.                   |
    +-------------------------+------------------------------------------------------------+
    |Address Start Date       |The start date of the the person's address.                 |
    +-------------------------+------------------------------------------------------------+
    |Address End Date         |The end date of the person's address.                       |
    +-------------------------+------------------------------------------------------------+
    |Person date of birth     |The date of birth of the person, if known. If not known,    |
    |                         |click the Person birth Date Estimated flag below and add an |
    |                         |age.                                                        |
    +-------------------------+------------------------------------------------------------+
    |Person birthDateEstimated|Click 'Yes' if the age is estimated and add the age in the  |
    |flag                     |box to the right. Otherwise, click 'No'                     |
    +-------------------------+------------------------------------------------------------+
    |Person gender            |Enter the gender. The OpenMRS default is "M" or "F".        |
    |                         |(required)                                                  |
    +-------------------------+------------------------------------------------------------+
    |Person dead flag         |Required by OpenMRS. Choose 'Yes' if the person is dead and |
    |                         |enter the date they died.                                   |
    +-------------------------+------------------------------------------------------------+
    |Cause of death UUID      |UUID of the OpenMRS concept that caused the death.          |
    |                         |(i.e. 114100AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA - Pneumonia)     |
    +-------------------------+------------------------------------------------------------+


- Update Patient Identifiers
    This task action allows you to update patient identifiers in OpenMRS. The task uses the POST command to the OpenMRS Rest Web Services module. Below is a description of each field in the action.

    +-------------------------+------------------------------------------------------------+
    |Field                    |Description                                                 |
    +=========================+============================================================+
    |Patient UUID             |The UUID of the patient which identifiers will be updated.  |
    |                         |(required)                                                  |
    +-------------------------+------------------------------------------------------------+
    |Patient Identifiers      |The map which stores patient identifiers. (required)        |
    |                         |Key is the identifier type.                                 |
    |                         |Value is the identifier value.                              |
    +-------------------------+------------------------------------------------------------+


- Create Program Enrollment (OpenMRS v1.10+ only)
    This task action allows you to enroll patient to program in OpenMRS. The task uses the POST command to the OpenMRS Rest Web Services module. Below is a description of each field in the action.

    +-------------------------+------------------------------------------------------------+
    |Field                    |Description                                                 |
    +=========================+============================================================+
    |Patient UUID             |The UUID of the patient which will be enrolled. (required)  |
    +-------------------------+------------------------------------------------------------+
    |Program UUID             |The UUID of the program to which patient will be enrolled.  |
    |                         |(required)                                                  |
    +-------------------------+------------------------------------------------------------+
    |Enrolled Date            |The date of enrollment patient to program. (required)       |
    +-------------------------+------------------------------------------------------------+
    |Completed Date           |The date of complete the program enrollment                 |
    +-------------------------+------------------------------------------------------------+
    |Location Name            |The name of the OpenMRS location where the patient was      |
    |                         |enrolled. Must exactly match the OpenMRS location name.     |
    +-------------------------+------------------------------------------------------------+


- Change Program Enrollment State (OpenMRS v1.10+ only)
    This task action allows you to change program enrollment state or set it's completed date in OpenMRS. The task uses the POST command to the OpenMRS Rest Web Services module. Below is a description of each field in the action.

    +-------------------------+------------------------------------------------------------+
    |Field                    |Description                                                 |
    +=========================+============================================================+
    |Program Enrollment UUID  |The UUID of the program enrollment which state will be      |
    |                         |changed. (required)                                         |
    +-------------------------+------------------------------------------------------------+
    |Completed Date           |The completed date of the program enrollment.               |
    +-------------------------+------------------------------------------------------------+
    |State UUID               |The UUID of the new state.                                  |
    +-------------------------+------------------------------------------------------------+
    |State Start Date         |The start date of the new state.                            |
    +-------------------------+------------------------------------------------------------+



Tips
----

- Provider
    Provider class has custom serializator and deserializator, because the signature of the JSON has changed through OpenMRS 1.9 and higher versions. 

- Relationship
    Relationship can be lookup by Person UUID, but this UUID refers only to personB field in Relationship object. Main reason is that this data source is used mainly for searching doctor-patient relationship.

- Concept
    Concept is widely used through OpenMRS. It represents a single, specific concept and could provide an expanded description in many languages. It could be a drug or cause of death (e.g. drowning).

- Patient Identifier Types
    Patient Identifier Types are used to identify patients in OpenMRS. In specific implementation could be many identifier types. MOTECH Id is just an identifier, which will be used in MOTECH by default. MOTECH could support many identifiers. If this is needed they must be added in Configuration, otherwise they will be ignored. 
