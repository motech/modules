package org.motechproject.rapidpro.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.rapidpro.event.publisher.EventPublisher;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.service.ContactService;
import org.motechproject.rapidpro.service.FlowService;
import org.motechproject.rapidpro.webservice.FlowRunWebService;
import org.motechproject.rapidpro.webservice.FlowWebService;
import org.motechproject.rapidpro.webservice.GroupWebService;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.motechproject.rapidpro.webservice.dto.Flow;
import org.motechproject.rapidpro.webservice.dto.FlowRunRequest;
import org.motechproject.rapidpro.webservice.dto.FlowRunResponse;
import org.motechproject.rapidpro.webservice.dto.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test class for {@link FlowServiceImpl}
 */
public class FlowServiceImplTest {

    private FlowService flowService;

    @Mock
    private ContactService contactService;

    @Mock
    private FlowWebService flowWebService;

    @Mock
    private FlowRunWebService flowRunWebService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private GroupWebService groupWebService;

    @Before
    public void setup() {
        initMocks(this);
        flowService = new FlowServiceImpl(flowWebService, contactService, flowRunWebService, eventPublisher, groupWebService);
    }

    @Test
    public void shouldStartContactFlowName() throws Exception {
        String externalId = "externalId";
        String flowName = "flowName";
        Flow flow = new Flow();
        flow.setName(flowName);
        flow.setUuid(UUID.randomUUID());
        Contact contact = new Contact();
        contact.setUuid(UUID.randomUUID());
        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(contactService.findByExternalId(externalId)).thenReturn(contact);
        when(flowWebService.getFlow(flowName)).thenReturn(flow);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);

        flowService.startFlowForContact(flowName, externalId, true, new HashMap<String, String>());

        verify(eventPublisher).publishFlowStartedContact(eq(externalId), eq(contact), any(FlowRunRequest.class), eq(flowRunResponses.get(0)));
    }

    @Test
    public void shouldStartContactFlowUUID() throws Exception {
        String externalId = "externalId";
        UUID flowUUID = UUID.randomUUID();
        Flow flow = new Flow();
        flow.setName("flowName");
        flow.setUuid(UUID.randomUUID());
        Contact contact = new Contact();
        contact.setUuid(UUID.randomUUID());
        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(contactService.findByExternalId(externalId)).thenReturn(contact);
        when(flowWebService.getFlow(flowUUID)).thenReturn(flow);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);

        flowService.startFlowForContact(flowUUID, externalId, true, new HashMap<String, String>());

        verify(eventPublisher).publishFlowStartedContact(eq(externalId), eq(contact), any(FlowRunRequest.class), eq(flowRunResponses.get(0)));
    }

    @Test
    public void shouldStartGroupFlowName() throws Exception {
        String groupName = "groupName";
        Group group = new Group();
        group.setName(groupName);
        group.setUuid(UUID.randomUUID());
        UUID flowUUID = UUID.randomUUID();
        Flow flow = new Flow();
        flow.setName("flowName");
        flow.setUuid(UUID.randomUUID());

        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(groupWebService.getGroupByName(groupName)).thenReturn(group);
        when(flowWebService.getFlow(flow.getName())).thenReturn(flow);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);

        flowService.startFlowForGroup(flow.getName(), groupName, true, new HashMap<String, String>());
        verify(eventPublisher).publishFlowStartedGroup(eq(flow.getName()), eq(group), any(FlowRunRequest.class), eq(flowRunResponses));
    }

    @Test
    public void shouldStartGroupFlowUUID() throws Exception {
        String groupName = "groupName";
        Group group = new Group();
        group.setName(groupName);
        group.setUuid(UUID.randomUUID());
        UUID flowUUID = UUID.randomUUID();
        Flow flow = new Flow();
        flow.setName("flowName");
        flow.setUuid(UUID.randomUUID());

        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(groupWebService.getGroupByName(groupName)).thenReturn(group);
        when(flowWebService.getFlow(flowUUID)).thenReturn(flow);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);

        flowService.startFlowForGroup(flowUUID, groupName, true, new HashMap<String, String>());
        verify(eventPublisher).publishFlowStartedGroup(eq(flow.getName()), eq(group), any(FlowRunRequest.class), eq(flowRunResponses));
    }

    @Test
    public void shouldFailContactNoContact() throws Exception {
        String externalId = "externalId";
        String flowName = "flowName";
        Flow flow = new Flow();
        flow.setName(flowName);
        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(contactService.findByExternalId(externalId)).thenReturn(null);
        when(flowWebService.getFlow(flowName)).thenReturn(flow);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);
        Map<String, String> extra = new HashMap<>();

        flowService.startFlowForContact(flowName, externalId, true, extra);

        verify(eventPublisher).publishFlowFailContact(anyString(), eq(externalId), eq(flowName), eq(true), eq(extra));
    }

    @Test
    public void shouldFailContactNoFlow() throws Exception {
        String externalId = "externalId";
        String flowName = "flowName";
        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(contactService.findByExternalId(externalId)).thenReturn(null);
        when(flowWebService.getFlow(flowName)).thenReturn(null);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);
        Map<String, String> extra = new HashMap<>();

        flowService.startFlowForContact(flowName, externalId, true, extra);

        verify(eventPublisher).publishFlowFailContact(anyString(), eq(externalId), eq(flowName), eq(true), eq(extra));
    }

    @Test
    public void shouldFailContactWSExceptionFlow() throws Exception {
        String externalId = "externalId";
        String flowName = "flowName";
        Flow flow = new Flow();
        flow.setName(flowName);
        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(contactService.findByExternalId(externalId)).thenReturn(null);
        when(flowWebService.getFlow(flowName)).thenThrow(new WebServiceException("exception"));
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);
        Map<String, String> extra = new HashMap<>();

        flowService.startFlowForContact(flowName, externalId, true, extra);

        verify(eventPublisher).publishFlowFailContact(anyString(), eq(externalId), eq(flowName), eq(true), eq(extra));
    }

    @Test
    public void shouldFailWSExceptionRun() throws Exception {
        String externalId = "externalId";
        UUID flowUUID = UUID.randomUUID();
        Flow flow = new Flow();
        flow.setName("flowName");
        flow.setUuid(UUID.randomUUID());
        Contact contact = new Contact();
        contact.setUuid(UUID.randomUUID());

        when(contactService.findByExternalId(externalId)).thenReturn(contact);
        when(flowWebService.getFlow(flowUUID)).thenReturn(flow);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenThrow(new WebServiceException("exception"));
        Map<String, String> extra = new HashMap<>();
        flowService.startFlowForContact(flowUUID, externalId, true, extra);

        verify(eventPublisher).publishFlowFailContact(anyString(), eq(externalId), eq(flowUUID), eq(true), eq(extra));

    }

    @Test
    public void shouldFailGroupNoFlow() throws Exception {
        String groupName = "groupName";
        String flowName = "flowName";
        Group group = new Group();
        group.setName(groupName);
        group.setUuid(UUID.randomUUID());
        Map<String, String> extra = new HashMap<>();

        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(groupWebService.getGroupByName(groupName)).thenReturn(group);
        when(flowWebService.getFlow(flowName)).thenReturn(null);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);

        flowService.startFlowForGroup(flowName, groupName, true, extra);
        verify(eventPublisher).publishFlowFailGroup(anyString(), eq(groupName), eq(flowName), eq(true), eq(extra));
    }

    @Test
    public void shouldFailGroupNoGroup() throws Exception {
        String groupName = "groupName";
        String flowName = "flowName";
        Group group = new Group();
        group.setName(groupName);
        group.setUuid(UUID.randomUUID());
        Map<String, String> extra = new HashMap<>();
        Flow flow = new Flow();
        flow.setName("flowName");
        flow.setUuid(UUID.randomUUID());

        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(groupWebService.getGroupByName(groupName)).thenReturn(null);
        when(flowWebService.getFlow(flowName)).thenReturn(flow);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);

        flowService.startFlowForGroup(flowName, groupName, true, extra);
        verify(eventPublisher).publishFlowFailGroup(anyString(), eq(groupName), eq(flowName), eq(true), eq(extra));
    }

    @Test
    public void shouldFailWSExceptionGroup() throws Exception {
        String groupName = "groupName";
        String flowName = "flowName";
        Group group = new Group();
        group.setName(groupName);
        group.setUuid(UUID.randomUUID());
        Map<String, String> extra = new HashMap<>();
        Flow flow = new Flow();
        flow.setName("flowName");
        flow.setUuid(UUID.randomUUID());

        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(groupWebService.getGroupByName(groupName)).thenThrow(new WebServiceException("exception"));
        when(flowWebService.getFlow(flowName)).thenReturn(flow);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);

        flowService.startFlowForGroup(flowName, groupName, true, extra);
        verify(eventPublisher).publishFlowFailGroup(anyString(), eq(groupName), eq(flowName), eq(true), eq(extra));
    }

    @Test
    public void shouldFailWSExceptionFlow() throws Exception {
        String groupName = "groupName";
        String flowName = "flowName";
        Group group = new Group();
        group.setName(groupName);
        group.setUuid(UUID.randomUUID());
        Map<String, String> extra = new HashMap<>();
        Flow flow = new Flow();
        flow.setName("flowName");
        flow.setUuid(UUID.randomUUID());

        List<FlowRunResponse> flowRunResponses = new ArrayList<>();
        FlowRunResponse response = new FlowRunResponse();
        flowRunResponses.add(response);
        when(groupWebService.getGroupByName(groupName)).thenReturn(group);
        when(flowWebService.getFlow(flowName)).thenThrow(new WebServiceException("exception"));
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenReturn(flowRunResponses);

        flowService.startFlowForGroup(flowName, groupName, true, extra);
        verify(eventPublisher).publishFlowFailGroup(anyString(), eq(groupName), eq(flowName), eq(true), eq(extra));
    }

    @Test
    public void shouldFailWSExceptionGroupRun() throws Exception {
        String groupName = "groupName";
        String flowName = "flowName";
        Group group = new Group();
        group.setName(groupName);
        group.setUuid(UUID.randomUUID());
        Map<String, String> extra = new HashMap<>();
        Flow flow = new Flow();
        flow.setName("flowName");
        flow.setUuid(UUID.randomUUID());

        when(groupWebService.getGroupByName(groupName)).thenReturn(group);
        when(flowWebService.getFlow(flowName)).thenReturn(flow);
        when(flowRunWebService.startFlowRuns(any(FlowRunRequest.class))).thenThrow(new WebServiceException("exception"));

        flowService.startFlowForGroup(flowName, groupName, true, extra);
        verify(eventPublisher).publishFlowFailGroup(anyString(), eq(groupName), eq(flowName), eq(true), eq(extra));
    }
}


