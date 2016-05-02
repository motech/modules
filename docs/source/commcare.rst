.. _commcare-module:

===============
Commcare Module
===============

.. contents::
   :depth: 4

The Commcare module allows MOTECH to interact with `Commcare applications <https://www.commcarehq.org>`_ that are hosted anywhere in the world. MOTECH has the capability to receive forwarded forms and cases as well as pull historical forms and cases from existing Commcare applications. These inputs can be used in the tasks module as task triggers that can perform many more task actions including storing information in MOTECH, forwarding data to DHIS2 and contacting the client. Note that MOTECH offers point-to-point interaction with the Commcare API which requires a `paid Commcare plan <https://www.commcarehq.org/software_services/#software-services-pricing>`_.

About Commcare
--------------
Commcare is a Software-as-a-Service mobile data collection system that focuses on hard to reach populations. The primary use case is to support community health or frontline workers in low and middle income countries. `Dimagi <http://dimagi.com>`_ is the company behind Commcare and has global workforce dedicated to implementing and supporting Commcare's use. Commcare's information structure is comprised of cases and forms. Forms are collected in a single interaction and cases allow things to be tracked over time. Dimagi's robust documentation provides a great description of their `case management features <https://confluence.dimagi.com/display/commcarepublic/Case+Management>`_.

There are a few Commcare terms that are useful to know as you proceed through the documentation.  A project is the unit where you are able to assign the workforce and is the unit for billing. Each project can have multiple applications which are independent units within a project. For example, you may have a Maternal and Child Health project that has a maternal application and a child immunization application. Each application is independent. Within each application, you are able to create modules, or collections of forms which are displayed to the end user who enter the data from the field. These forms are able to contribute to a case, which allows you to track changes to something over time. To continue the example, the maternal application may have a prenatal care module that has a registration form, a 3 month, 6 month and 9 month checkup form and a delivery form. Each of these forms would contribute to the case, allowing you to follow a mother over time until she delivers.

Feature List
------------
Below is a list of features available in the Commcare module:

- Configure connections to multiple Commcare projects
- Setup form forwarding in the Commcare project from the MOTECH user interface
- Respond to forwarded Commcare forms and cases from multiple Commcare applications with each project and act on those events with the tasks module
- Automatically update the schema in MOTECH when a user deploys the application in CommCare
- Pull in forms for a specified date range, allowing for retroactive processing of field deployed Commcare applications and advanced workflows
- Integrate with Commcare Supply to Query Stock Ledger and process all stock transactions

Configuring a Commcare Connection
---------------------------------
After installing the module, you must configure the connection. Click Modules > Commcare and you are presented with the configuration screen.

.. image:: img/CommCare_blank_config_screenshot.png

Account Settings
^^^^^^^^^^^^^^^^
- Configuration Name: This is the name of the configuration that will be referenced for this connection to the Commcare app
- Commcare Base URL: This is the base URL for the Commcare server. Generally, this doesn't need to be updated unless you're using a country deployment of Commcare such as https://india.commcarehq.org/a
- Commcare Domain: This is the name of the Commcare project. This is available in the URL just after you login to your Commcare account and generally comes after the 'a' in the URL. For example, our Commcare domain would be 'motechtest' in the following URL https://www.commcarehq.org/a/motechtest/
- Username/Password: These are the username and password that MOTECH will use to authenticate with Commcare. This must be a web user, not a mobile worker in Commcare.

Once you complete the field, click Save and then Verify to verify that you can connect to the Commcare project. You will receive a red message if you're not able to connect at this point.

**Make sure to click save before leaving the screen.** MOTECH does not autosave drafts in this configuration.

Event Forwarding
^^^^^^^^^^^^^^^^
We are able to choose an event forwarding strategy to setup in Commcare through the MOTECH UI. Event forwarding can also be setup in the Commcare Project Settings. More information on the Event Forwarding strategy can be found in the `Commcare documentation <https://confluence.dimagi.com/pages/viewpage.action?pageId=12224128>`_.

ConnectCommCareHQ
^^^^^^^^^^^^^^^^^
This section allows you to choose which items you wish to forward from Commcare to MOTECH. You may see a blue info box stating "To be able to set forwarding rules you must set server.url property in the Admin settings" as is in the screenshot above. This can be resolved by clicking Admin > Settings and completing the field titled server URL with the fully qualified domain name.

If the domain server URL has already been defined, you will see a set of URL endpoints that MOTECH makes available to Commcare in case you wish to copy and paste the URL into the Commcare Project Settings. When you check one of the boxes, it sends the request to Commcare that sets up the form forwarding through API calls instead of through the Commcare Project Settings user interface.

Below is a sample of the demo ConnectCommCareHQ section:

.. image:: img/CommCare_config_connect_CommCareHQ_screenshot.png

Viewing Imported Metadata
-------------------------
Clicking the verify button on the configuration screen imports the project's metadata including, forms and cases. You can view this information in the "Forms" and "Cases" tabs in the Commcare module. The select configuration dropdown menu allows you to view all of this information for each Commcare server configuration you have setup. For example, our test configuration has two applications with three modules, each with two forms.

.. image:: img/CommCare_forms_tab_screenshot.png

You can click the JSON and View buttons of each form to view their representation in MOTECH.

Viewing All Cases
-----------------
The Cases tab in the Commcare module displays a list of cases in Commcare. You can filter these cases and view their case information in this tab.

.. image:: img/CommCare_cases_tab_screenshot.png

Importing Forms
---------------
The Import Forms tab allows administrators to import a historical list of forms from a Commcare server into MOTECH for processing. This feature was added to support users who need to add MOTECH to existing Commcare implementations. You have the ability to import all forms or a subset of forms based on a particular datetime range. Each imported form raises a "Received Form: (Form Name) [Configuration Name]" event in the system and can trigger tasks based on that event.

**Steps**

- Click Import Forms
- Choose the configuration from which to import forms
- Choose to import all forms or specify a date range. Note that Commcare administrators can view the form received date range in their Commcare account to adjust this to the second and import a very accurate subset of forms.
- Click Import Forms and MOTECH queries the Commcare Forms API to identify how many forms will be imported. If ready, click Start Import and the import will begin.
- A progress bar shows the status and any errors are tracked in the server log in case of an internet outage.

Tasks Integration
-----------------
The Commcare module currently acts as a task trigger when forms, cases, stubs and error logs are received from Commcare. It can also act as a data source, allowing for querying specific Commcare API endpoints. A new feature in MOTECH 1.0 will add a new task action that queries the stock ledger API. A full explanation of the tasks module can be found in the `Implementer's Getting Started Guide <http://docs.motechproject.org/en/latest/get_started/using_tasks/using_tasks.html#tasks-ui>`_.

Task Triggers
^^^^^^^^^^^^^
Each Commcare configuration has it's own set of the following task triggers. The configuration name is identified in brackets. For example, Received Form: Registration [motechTestConfig] will start the task when MOTECH receives a form with title "Registration" from the "motechTestConfig" configuration. Note that the Case ID is Commcare's unique case identifier. This can be useful when storing which cases have been processed, forwarding information to DHIS2 or querying Commcare APIs.

- Received Form: (Form Name) [Configuration Name]
    This task trigger will fire when MOTECH receives a forwarded form from the Commcare server. Each form in the Commcare application is available as a task trigger and all of the received form fields and `form metadata <https://bitbucket.org/javarosa/javarosa/wiki/OpenRosaMetaDataSchema>`_ are available to the data source, filter, and task action.
- Received Case: (Case Name) [Configuration Name]
    This task trigger will fire when MOTECH receives a forwarded case from the Commcare server. Each case in the Commcare application is available as a task trigger and all of the received case fields and `metadata <https://github.com/dimagi/commcare/wiki/casexml20#case-xml-element>`_ are available to the data source, filter, and task action.
- Received Case ID [Configuration Name]
    This task trigger will fire when MOTECH receives a forwarded Case ID from the Commcare server. Only two fields are available to the task the Case ID and the Commcare module's Configuration Name. This feature is most often used if you wish to query the Commcare Case API using the task data source when a case is created, updated or closed in Commcare.
- Received Form Stub [Configuration Name]
    This task trigger will fire when MOTECH receives a forwarded form stub from the Commcare server. Only four fields are available, Received On date, Form Id of the received form, Case IDs related to the form and the Commcare module's configuration name. Like the Received Case ID, this feature is most often used to query the Commcare Case API using the task data source when a form is received by Commcare.
- Received Device Log [Configuration Name]
    This task trigger will fire when MOTECH receives a device log from the Commcare server. Only five fields are made available to the task, Element name, Sub-Elements, Attributes, Value and Commcare module's configuration name.
- Forms Failed [Configuration Name]
    This task trigger will fire when MOTECH receives an error from the Commcare server showing that form forwarding failed. Only two fields are made available to the task, Message and the Commcare module's configuration name. This is commonly used to alert administrators when there is an error submitting a form and could prove incredibly valuable with large form submissions in remote areas with intermittent internet connectivity.
- Retrieved Stock Transaction [Configuration Name]
    This task trigger responds to incoming stock transactions when the Query Stock Ledger Task Action is fired. Each stock transaction in the ledger is returned with seven distinct fields Product ID, Product Name, Quantity, Section ID, Stock on hand, Transaction Date and Type.

Special Case - Accessing Data in Repeat Groups
""""""""""""""""""""""""""""""""""""""""""""""
Some of the Commcare forms may allow for repeating a set of questions and the number of repeats is not predefined by the schema. Due to this fact, it is impossible to generate trigger fields
in the Tasks UI to access the repeated data. Nevertheless, it is still possible to access the repeated data, by manually crafting the trigger reference and specifying the element that
is to be accessed. MOTECH will generate trigger keys for repeated questions, if two conditions are met by the received form in its XML representation:

- There must be at least two elements of the same name
- Those elements must have the "id" attribute set

If that's the case, the generated trigger event keys will have the value of the "id" attribute appended, in order to be able to distinguish one from another and to be able to access repeated data.
For example, for the following received XML:

.. code-block:: xml

    <?xml version='1.0' ?>
    <data>
        <mother name="Jane">
            <children>
                <child id="22345">John</child>
                <child id="22346">Oliver</child>
            </children>
        </mother>
    </data>

The generated trigger event keys will be:

- **/data/mother/children/child_22345**
- **/data/mother/children/child_22346**

It is now possible to use those values in the task actions, using syntax for custom trigger events, like:

- **{{trigger./data/mother/children/child_22345}}**
- **{{trigger./data/mother/children/child_22346}}**

Task Data Source
^^^^^^^^^^^^^^^^
The Tasks module will query specific Commcare APIs and make the results available to the task. These data sources are useful when supplemental information is needed from a received Case or form such as values from a lookup table, Commcare users and locations. Click Add data source in the task and choose Source: Commcare to make this information available to the task. Each of the following objects are available as a data source.

- Fixture [Configuration Name]
    Fixtures are Commcare's lookup tables and this data source allows users to look a fixture by ID using the `Fixture Data API <https://confluence.dimagi.com/display/commcarepublic/Fixture+Data>`_. Most often, a form field will capture a pointer to the item in the lookup table, but supplementary data will be available in the table. You can drag and drop the form field that references the lookup table and all of that data will be available to the task.
- Form: (Form Name) [Configuration Name]
    This data source queries the `Commcare Form Data API <https://confluence.dimagi.com/display/commcarepublic/Form+Data>`_ based on the form ID and returns all of the form results. This is most often coupled with the "Received Form Stub" task trigger.
- Case: (Case Name) [Configuration Name]
    This data source queries the `Commcare Case Data API <https://confluence.dimagi.com/display/commcarepublic/Case+Data>`_ based on the case ID and returns all of the case properties. This is most often coupled with the "Received Case ID" task trigger.
- User [Configuration Name]
    This data source queries the `Commcare Mobile Workers API <https://confluence.dimagi.com/display/commcarepublic/List+Mobile+Workers>`_ based on the user_id and returns details about the Commcare user.
- Location [Configuration Name]
    This data source is only available to users who have enabled the `Commcare Supply feature <https://confluence.dimagi.com/display/commtrack/Getting+Started+With+CommCare+Supply>`_. Commcare Supply tracks organizations and locations so users can manage supply chain items within this hierarchy. MOTECH queries the `list of locations <https://confluence.dimagi.com/display/commtrack/Locations>`_ for a particular location ID, similar to the Fixture data source. The returned items include latitude, longitude, location name, site codes and location within the location schema.

Task Actions
^^^^^^^^^^^^
The Commcare module also exposes several Task actions, that allow for querying the stock ledger API and uploading cases and forms via submission API.

- Query Stock Ledger [Configuration Name]
    This task action allows you to query the Commcare stock ledger based on the incoming Case ID and Section ID. This feature is only applicable to users who have CommCare Supply turned on. When the stock ledger is queried, each response is parsed by MOTECH and an event is raised that can be used as a task trigger of type *Retrieved Stock Transaction [Configuration Name].*
- Create Case [Configuration Name]
    Creates a Commcare Case, by sending Case XML to the Submission API on the Commcare server. Note that the case ID UUID is created by MOTECH.
- Update Case [Configuration Name]
    Updates a Commcare Case, by sending Case XML to the Submission API on the Commcare server. The case ID is required to identify the case that is supposed to be updated. The case may be optionally closed.
- Submit Form: (Form Name) [Configuration Name]
    This feature offers basic support for submitting a Commcare Form, by sending the Form XML to the Commcare Submission API. The task action fields are generated basing on the schema of the Commcare forms, present on the MOTECH server. Note that MOTECH does not have a xform player. Advanced forms with calculated fields, skip logic and required fields are not supported in the current version of this task action.

Advanced Workflows
------------------
This section defines advanced workflows available in the Commcare module, allowing users to configure MOTECH to better meet the needs of implementers.

Getting the Current Time for Task Actions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
The Create Case task action requires a date and time for input. It's not appropriate to submit a static date and time when creating a Commcare case from MOTECH. To get the current time, add a Data Source to your task action with Source "Platform commons" and Lookup "Platform commons". Drag the bubble labeled "Now" onto the task action datetime field.

Also note that you can use regular expressions to modify the current date time. This could be useful for querying the stock ledger for a specified date range say now minus 1 day to get the last day of stock ledger submissions.


Commcare Supply's Stock Ledger
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
`Commcare Supply <https://www.commcarehq.org/solutions/#solutions-supply-intro>`_ allows users to manage their supply chain. Often, those commodities need to be reported up the chain to DHIS2. We created a set of features in MOTECH Tasks to allow for commodity reporting from the Commcare stock ledger to DHIS2.

**Workflow**

- A user submits a Commcare form that updates their current stock on hand
- Commcare Supply calculates the monthly consumption
- The form is forwarded to MOTECH and acts as a task trigger.
    - The task action queries the stock ledger, pulling the latest consumption rates and fires a task for each item that's parsed in the ledger.
- MOTECH Queries the stock ledger and each item is forwarded to DHIS2 as a data element.

**Setup**

Commcare stores all products in a stock ledger that keeps a current count of each product on hand at a given location. You need to create two tasks in MOTECH for this workflow.

- Task 1 responds to the incoming form or case update and queries the stock ledger. Each item in the stock ledger is parsed and an event is raised in MOTECH.
- Task 2 will trigger when a particular item is parsed and you can then forward that data value to DHIS2 in a task action or store it in MOTECH.