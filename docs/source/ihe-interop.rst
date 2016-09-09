.. _ihe-interop-module:

==================
IHE Interop Module
==================

.. contents::
   :depth: 2

The IHE Interop module allows users to send standardized XML files to other web services. The module was initially build to convert an incoming CommCare form into an HL7 v3 Continuity of Care Document. The module allows you to create an XML template, upload it to MOTECH and expose specific template fields in a task action, easing the burden of creating standardized HL7 documents. Though it was created for CDA documents, we believe this module can be used in multiple IHE profiles to support the transformation of data from any source into standardized documents. This module was tested using the open source MirthConnect as a destination system.

.. Note::

    This module was built for a 1-to-1 match between source forms and the output. It was not built to accommodate looping through fields. For example, the incoming CommCare form could have a list of diagnoses. This module is not currently able to loop through the list of diagnoses as is common in other HL7 software systems. We must know the maximum expected number of diagnoses to be supported when building the template.

Defining the Recipient System
-----------------------------
We need to define the recipient system to which we will send the XML document. This can be done by clicking Admin>Manage Modules and clicking the gear icon next to the IHE Interop module. Below is a sample file that needs to be uploaded in this section. You can also find a sample file in `our source code <https://raw.githubusercontent.com/motech/modules/master/ihe-interop/src/test/resources/hl7-recipients.json>`_.

.. code-block:: json

    [
      {
        "recipientName": "sample_recipient_no_authentication_example",
        "recipientUrl": "https://www.hl7recipient.com/myendpoint"
      },
      {
        "recipientName": "sample_recipient_authentication_example",
        "recipientUrl": "https://www.hl7recipient.com/myendpoint",
        "recipientUsername": "sample_username",
        "recipientPassword": "sample_password"
      }
    ]
The example json above shows two recipients. The first recipient does not require authentication and the second requires basic authentication.

Creating a Template
-------------------
Creating standardized templates is no easy task. We assume that the team creating these templates has an understanding of the health standard. This module has the capability to create any XML template you like. For example, you could probably create an ODK compatible xform using this module and post it to an ODK Aggregate endpoint. Our approach to solving this problem allows you to create a valid template outside of MOTECH, identify the fields you wish to fill in using a handlebar notation ${} and each variable you create in the handlebar notation will be made available in a MOTECH task action. For example, if you create a variable named ${GivenName} in your template, you'll have a task action that sends this template with a field labeled "Given Name". You define the template variables and the field names as they're displayed in the task action.

Below is an excerpt from a full CDA template.

.. code-block:: xml

    <recordTarget>
	<patientRole>
	    <id extension="996-756-495" root="2.16.840.1.113883.19.5"/>
	    <patient>
		<name>
		    <given>${GivenName}</given>
		    <family>${FamilyName}</family>
		</name>
		<administrativeGenderCode code="${Gender}" codeSystem="2.16.840.1.113883.5.1"/>
		<birthTime value="${birthtime}"/>
	    </patient>
	    <providerOrganization>
		<id root="2.16.840.1.113883.19.5"/>
		<name>Demo Clinic Location</name>
	    </providerOrganization>
	</patientRole>
    </recordTarget>

As you can see in this example the *${GivenName}*, *${FamilyName}* and *${birthtime}* are variables that we inserted in the template. The handlebar notation starts with a dollar sign then handlebars ${} You can enter any variable name as long as there are no special characters or spaces. Also, you can not repeat variable names. As you complete your document, keep track of all the variable names in a text document. You'll use this after you upload the template to define the field names that display in the task action. At this point, we have three variables.

+------------+-------------+
| Key        | Value       |
+------------+-------------+
| GivenName  | Given Name  |
+------------+-------------+
| FamilyName | Family Name |
+------------+-------------+
| birthtime  | birthtime   |
+------------+-------------+

Upload the Template
-------------------
When finished creating your template, save the template as an XML file that you can upload to MOTECH. The template needs to be uploaded directly into an MDS table names CdaTemplate. Click Modules> Data Services to access the MDS list. Search for the IHE Interop Module heading and click the green button labeled "+ Add" next to the "CdaTemplate" entity.

**Template Data:** Select the template file you created
**Template Name:** This is the name of the template that will display in the task action
**Properties (Key Value Pairs):** Enter a key value pair for each ${variable} you created in the template. The key is the variable you put in the template exactly as you typed it between the handlebar and the value is the text you wish to display for this field to the end user in the tasks module.

Click Save once you create all of your key value pairs and the task action will be created.

Tasks Integration
-----------------
This module allows you to create templated task actions that will send the XML to the recipient of your choice. At this point, you have uploaded a list of recipients, created an XML template, uploaded the template to MOTECH and defined the field names to be collected in the task. Now, we need to test that the connection works.

- Open the tasks module and create a new task with any task trigger
- Add a task action and choose the IHE Interop module
- You should now see a list of the template name in the drop down menu
- Click this template name and you'll see the list of fields you created in the Key Value Pairs
- You can now drag and drop variables from the task into these fields and they will be added to your XML template before being sent to the recipient
- Save and enable the task and test it
- Trigger the task and verify that it is received in the destination system.