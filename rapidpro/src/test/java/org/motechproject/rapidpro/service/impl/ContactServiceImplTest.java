package org.motechproject.rapidpro.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.rapidpro.exception.NoMappingException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.service.ContactMapperService;
import org.motechproject.rapidpro.service.ContactService;
import org.motechproject.rapidpro.webservice.ContactWebService;
import org.motechproject.rapidpro.webservice.GroupWebService;
import org.motechproject.rapidpro.webservice.dto.Contact;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test class for {@link ContactServiceImpl}
 */
public class ContactServiceImplTest {

    private ContactService contactService;

    @Mock
    ContactMapperService contactMapperService;

    @Mock
    ContactWebService contactWebService;

    @Mock
    StatusMessageService statusMessageService;

    @Mock
    GroupWebService groupWebService;


    @Before
    public void setup() {
        initMocks(this);
        contactService = new ContactServiceImpl(contactMapperService, contactWebService, statusMessageService, groupWebService);
    }

    @Test
    public void shouldCreateContact() throws Exception {
        String externalID = "externalID";
        String phoneNumber = "1234567890";
        String uuid = "contactUUID";
        String name = "Name";
        List<String> urns = new ArrayList<>();
        urns.add("tel:+" + phoneNumber);
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhone(phoneNumber);
        contact.setUrns(urns);
        Contact fromWebservice = new Contact();
        fromWebservice.setName(name);
        fromWebservice.setPhone(phoneNumber);
        fromWebservice.setUrns(urns);
        fromWebservice.setUuid(uuid);

        when(contactMapperService.exists(externalID)).thenReturn(false);
        when(contactWebService.getContactByPhoneNumber(phoneNumber)).thenReturn(null);
        when(contactWebService.createOrUpdateContact(contact)).thenReturn(fromWebservice);

        contactService.create(externalID, phoneNumber, contact);

        verify(contactMapperService).create(externalID, uuid);
    }

    @Test
    public void shouldSendContactExistsMessage() throws Exception {
        String phoneNumber = "1234567890";
        String externalID = "externalID";
        Contact contact = new Contact();
        contact.setName("Name");
        contact.setPhone(phoneNumber);
        List<String> urns = new ArrayList<>();
        urns.add("tel:+" + phoneNumber);
        contact.setUrns(urns);

        when(contactWebService.getContactByPhoneNumber(phoneNumber)).thenReturn(contact);
        when(contactMapperService.exists(externalID)).thenReturn(false);

        contactService.create(externalID, phoneNumber, contact);

        verify(statusMessageService).warn(anyString(), eq("rapidpro"));
    }

    @Test
    public void shouldSendMappingExistsMessage() throws Exception {
        String phoneNumber = "1234567890";
        String externalID = "externalID";
        Contact contact = new Contact();
        contact.setName("Name");
        contact.setPhone(phoneNumber);
        List<String> urns = new ArrayList<>();
        urns.add("tel:+" + phoneNumber);
        contact.setUrns(urns);

        when(contactMapperService.exists(externalID)).thenReturn(true);

        contactService.create(externalID, phoneNumber, contact);

        verify(statusMessageService).warn(anyString(), eq("rapidpro"));
    }

    @Test
    public void shouldSendWebserviceFailMessageCreate() throws Exception {
        String phoneNumber = "1234567890";
        String externalID = "externalID";
        Contact contact = new Contact();
        contact.setName("Name");
        contact.setPhone(phoneNumber);
        List<String> urns = new ArrayList<>();
        urns.add("tel:+" + phoneNumber);
        contact.setUrns(urns);

        when(contactMapperService.exists(externalID)).thenReturn(false);
        when(contactWebService.getContactByPhoneNumber(phoneNumber)).thenReturn(null);
        when(contactWebService.createOrUpdateContact(contact)).thenThrow(new WebServiceException("message"));

        contactService.create(externalID, phoneNumber, contact);

        verify(statusMessageService).warn(anyString(), eq("rapidpro"));
    }

    @Test
    public void shouldUpdateContact() throws Exception{
        String externalID = "externalID";
        String phoneNumber = "1234567890";
        String uuid = "contactUUID";
        String name = "Name";
        List<String> urns = new ArrayList<>();
        urns.add("tel:+" + phoneNumber);
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhone(phoneNumber);
        contact.setUrns(urns);
        Contact fromWebservice = new Contact();
        fromWebservice.setName(name);
        fromWebservice.setPhone(phoneNumber);
        fromWebservice.setUrns(urns);
        fromWebservice.setUuid(uuid);

        when(contactMapperService.getRapidproUUIDFromExternalId(externalID)).thenReturn(uuid);
        when(contactWebService.getContactByUUID(uuid)).thenReturn(fromWebservice);
        when(contactWebService.createOrUpdateContact(contact)).thenReturn(fromWebservice);

        contactService.update(externalID, contact);

        verify(contactWebService).createOrUpdateContact(any(Contact.class));
    }

    @Test
    public void shouldSendNoMappingMessageUpdate() throws Exception {
        String externalID = "externalID";
        String phoneNumber = "1234567890";
        String name = "Name";
        List<String> urns = new ArrayList<>();
        urns.add("tel:+" + phoneNumber);
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhone(phoneNumber);
        contact.setUrns(urns);

        when(contactMapperService.getRapidproUUIDFromExternalId(externalID)).thenThrow(new NoMappingException(externalID));

        contactService.update(externalID, contact);

        verify(statusMessageService).warn(anyString(), eq("rapidpro"));
    }

    @Test
    public void shouldSendWebserviceFailMessageUpdate() throws Exception{
        String externalID = "externalID";
        String phoneNumber = "1234567890";
        String uuid = "contactUUID";
        String name = "Name";
        List<String> urns = new ArrayList<>();
        urns.add("tel:+" + phoneNumber);
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhone(phoneNumber);
        contact.setUrns(urns);
        Contact fromWebservice = new Contact();
        fromWebservice.setName(name);
        fromWebservice.setPhone(phoneNumber);
        fromWebservice.setUrns(urns);
        fromWebservice.setUuid(uuid);

        when(contactMapperService.getRapidproUUIDFromExternalId(externalID)).thenReturn(uuid);
        when(contactWebService.getContactByUUID(uuid)).thenThrow(new WebServiceException("message"));

        contactService.update(externalID, contact);

        verify(statusMessageService).warn(anyString(), eq("rapidpro"));
    }

    @Test
    public void shouldDeleteContact() throws Exception {
        String externalID = "externalID";
        String uuid = "contactUUID";
        when(contactMapperService.getRapidproUUIDFromExternalId(externalID)).thenReturn(uuid);
        contactService.delete(externalID);
        verify(contactWebService).deleteContactByUUID(uuid);
    }

    @Test
    public void shouldSendNoMappingMessageDelete() throws Exception {
        String externalID = "externalID";
        when(contactMapperService.getRapidproUUIDFromExternalId(externalID)).thenThrow(new NoMappingException(externalID));
        contactService.delete(externalID);
        verify(statusMessageService).warn(anyString(), eq("rapidpro"));
    }

    @Test
    public void shouldSendWebserviceFailMessageDelete() throws Exception {
        String externalID = "externalID";
        String uuid = "contactUUID";
        when(contactMapperService.getRapidproUUIDFromExternalId(externalID)).thenReturn(uuid);
        doThrow(new WebServiceException("message")).when(contactWebService).deleteContactByUUID(uuid);
        contactService.delete(externalID);
        verify(statusMessageService).warn(anyString(), eq("rapidpro"));
    }

    @Test
    public void shouldFindContactByExternalId() throws Exception {
        String externalID = "externalID";
        String phoneNumber = "1234567890";
        String uuid = "contactUUID";
        String name = "Name";
        List<String> urns = new ArrayList<>();
        urns.add("tel:+" + phoneNumber);
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhone(phoneNumber);
        contact.setUrns(urns);
        contact.setUuid(uuid);
        when(contactMapperService.getRapidproUUIDFromExternalId(externalID)).thenReturn(uuid);
        when(contactWebService.getContactByUUID(uuid)).thenReturn(contact);

        Contact fromWebservice = contactService.findByExternalId(externalID);
        assertEquals(fromWebservice.getUuid(), contact.getUuid());
    }

    @Test
    public void shouldSendNoMappingMessageFind() throws Exception {
        String externalID = "externalID";
        when(contactMapperService.getRapidproUUIDFromExternalId(externalID)).thenThrow(new NoMappingException(externalID));
        contactService.findByExternalId(externalID);
        verify(statusMessageService).warn(anyString(), eq("rapidpro"));
    }

    @Test
    public void shouldSendWebserviceFailMessageFind() throws Exception {
        String externalID = "externalID";
        String uuid = "contactUUID";
        when(contactMapperService.getRapidproUUIDFromExternalId(externalID)).thenReturn(uuid);
        when(contactWebService.getContactByUUID(uuid)).thenThrow(new WebServiceException("message"));
        contactService.findByExternalId(externalID);
        verify(statusMessageService).warn(anyString(), eq("rapidpro"));
    }


}
