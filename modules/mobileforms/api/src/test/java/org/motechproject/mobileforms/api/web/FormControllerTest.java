package org.motechproject.mobileforms.api.web;

import com.jcraft.jzlib.ZInputStream;
import org.fcitmuk.epihandy.EpihandyXformSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.motechproject.mobileforms.api.callbacks.FormGroupPublisher;
import org.motechproject.mobileforms.api.callbacks.FormParser;
import org.motechproject.mobileforms.api.repository.AllMobileForms;
import org.motechproject.mobileforms.api.service.MobileFormsService;
import org.motechproject.mobileforms.api.service.UsersService;
import org.motechproject.mobileforms.api.validator.FormValidator;
import org.motechproject.mobileforms.api.validator.TestFormBean;
import org.motechproject.mobileforms.api.domain.Study;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.motechproject.mobileforms.api.domain.Form;
import org.motechproject.mobileforms.api.domain.FormGroup;
import org.motechproject.mobileforms.api.validator.FormGroupValidator;
import org.motechproject.mobileforms.api.domain.FormOutput;
import org.motechproject.mobileforms.api.domain.FormBean;
import org.motechproject.mobileforms.api.domain.FormError;
import org.motechproject.mobileforms.api.domain.FormBeanGroup;

import javax.servlet.ServletContext;
import java.util.Properties;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.reset;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { FormController.class } )
public class FormControllerTest {
    private FormController formController;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Integer groupIndex = 2;

    @Mock
    private MobileFormsService mobileFormsService;
    @Mock
    private AllMobileForms allMobileForms;
    @Mock
    private UsersService usersService;
    @Mock
    private FormGroupPublisher formGroupPublisher;
    @Mock
    private FormGroupValidator formGroupValidator;
    @Mock
    private EpihandyXformSerializer epihandySerializer;
    @Mock
    private Properties mobileFormsProperties;
    @Mock
    private ServletContext servletContext;
    @Mock
    private FormParser formParser;
    @Mock
    private FormOutput formOutput;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        formController = PowerMockito.spy(new FormController());

        ReflectionTestUtils.setField(formController, "usersService", usersService);
        ReflectionTestUtils.setField(formController, "formGroupPublisher", formGroupPublisher);
        ReflectionTestUtils.setField(formController, "formGroupValidator", formGroupValidator);
        ReflectionTestUtils.setField(formController, "mobileFormsService", mobileFormsService);
        ReflectionTestUtils.setField(formController, "allMobileForms", allMobileForms);
        ReflectionTestUtils.setField(formController, "servletContext", servletContext);
        ReflectionTestUtils.setField(formController, "mobileFormsProperties", mobileFormsProperties);
        ReflectionTestUtils.setField(formController, "serializer", epihandySerializer);
        ReflectionTestUtils.setField(formController, "formParser", formParser);

        PowerMockito.doReturn(formOutput).when(formController, "getFormOutput");
    }

    @Test
    public void shouldReturnListOfStudyNames() throws Exception {
        String studyOne = "StudyOne";
        String studyTwo = "StudyTwo";
        final String dataExpectedToBeReturned = "Mock - List of form groups";
        when(mobileFormsService.getAllFormGroups()).thenReturn(asList(new Object[]{0, studyOne}, new Object[]{1, studyTwo}));

        doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((ByteArrayOutputStream) invocationOnMock.getArguments()[0]).write(dataExpectedToBeReturned.getBytes());
                return null;
            }
        }).when(epihandySerializer).serializeStudies(any(OutputStream.class), Matchers.anyObject());

        populateHttpRequest(request, "username", "password", FormController.ACTION_DOWNLOAD_STUDY_LIST, null);

        formController.downloadHandler(request, response);

        String responseSentToMobile = readResponse(response);

        ArgumentCaptor<OutputStream> outputStreamArgumentCaptor = ArgumentCaptor.forClass(OutputStream.class);
        ArgumentCaptor<Object> dataArgumentCaptor = ArgumentCaptor.forClass(Object.class);

        verify(epihandySerializer).serializeStudies(outputStreamArgumentCaptor.capture(), dataArgumentCaptor.capture());
        assertThat(outputStreamArgumentCaptor.getValue().toString(), is(equalTo(dataExpectedToBeReturned)));
        assertThat(((List<Object[]>) dataArgumentCaptor.getValue()).size(), is(equalTo(2)));
        assertTrue(Arrays.equals(((List<Object[]>) dataArgumentCaptor.getValue()).get(0), new Object[]{0, studyOne}));
        assertTrue(Arrays.equals(((List<Object[]>) dataArgumentCaptor.getValue()).get(1), new Object[]{1, studyTwo}));
        assertThat(responseSentToMobile, is(equalTo((char) FormController.RESPONSE_SUCCESS + dataExpectedToBeReturned)));
    }

    @Test
    public void shouldReturnListOfFormsForTheGivenStudyNameWithListOfUserAccounts() throws Exception {
        final String formOneContent = "FormOne";
        final String formTwoContent = "FromTwo";
        final String groupName = "GroupOne";
        final Integer groupIndex = 2;
        final String dataExpectedToBeReturned = "Mock - List of forms";
        final String userDetailsExpectedToBeReturned = "Mock - User details";

        List<Object[]> userDetails = new ArrayList<Object[]>();
        userDetails.add(new Object[]{"username", "password", "salt"});
        when(usersService.getUsers()).thenReturn(userDetails);

        final List<String> formXmlContents = Arrays.asList(formOneContent, formTwoContent);
        final List<Form> forms = Arrays.asList(new Form(null, null, formOneContent, null, null, null, null),
                new Form(null, null, formTwoContent, null, null, null, null));
        when(mobileFormsService.getForms(groupIndex)).thenReturn(new FormGroup(groupName, forms));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((ByteArrayOutputStream) invocationOnMock.getArguments()[0]).write(userDetailsExpectedToBeReturned.getBytes());
                List<Object[]> userDetails = (List<Object[]>) invocationOnMock.getArguments()[1];
                assertThat(userDetails.size(), is(equalTo(1)));
                assertThat(userDetails.get(0)[0], is(equalTo(userDetails.get(0)[0])));
                assertThat(userDetails.get(0)[1], is(equalTo(userDetails.get(0)[1])));
                assertThat(userDetails.get(0)[2], is(equalTo(userDetails.get(0)[2])));
                return null;
            }
        }).when(epihandySerializer).serializeUsers(any(OutputStream.class), Matchers.anyObject());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((ByteArrayOutputStream) invocationOnMock.getArguments()[0]).write(dataExpectedToBeReturned.getBytes());
                assertThat((List<String>) invocationOnMock.getArguments()[1], is(equalTo(formXmlContents)));
                assertThat((Integer) invocationOnMock.getArguments()[2], is(equalTo(groupIndex)));
                assertThat((String) invocationOnMock.getArguments()[3], is(equalTo(groupName)));
                return null;
            }
        }).when(epihandySerializer).serializeForms(any(OutputStream.class), Matchers.anyObject(), anyInt(), anyString());

        populateHttpRequest(request, "username", "password", FormController.ACTION_DOWNLOAD_USERS_AND_FORMS, groupIndex);

        formController.downloadHandler(request, response);
        String responseSentToMobile = readResponse(response);

        assertThat(responseSentToMobile, is(equalTo((char) FormController.RESPONSE_SUCCESS + userDetailsExpectedToBeReturned + dataExpectedToBeReturned)));
    }

    @Test
    public void shouldProcessUploadedForms() throws Exception {
        final String validatorClass = "org.motechproject.mobileforms.api.validator.TestANCVisitFormValidator";
        final FormValidator formValidator = mock(FormValidator.class);
        FormBean successForm = new TestFormBean("study", "form1", "xml", validatorClass, "type", Collections.<String>emptyList(), "group1", "last");
        FormBean failureForm = new TestFormBean("study", "form2", "xml", validatorClass, "type", Collections.<String>emptyList(), "group1", "last");
        FormBean formInDifferentGroup = new TestFormBean("study", "form3", "xml", validatorClass, "type", Collections.<String>emptyList(), "group2", "last");

        List<FormBean> formBeans = Arrays.asList(successForm, failureForm, formInDifferentGroup);
        List<FormError> formErrors = Arrays.asList(new FormError("field_name", "error"));
        Map<Integer, String> formIdMap = new HashMap<Integer, String>();
        Study study = new Study("study", formBeans);

        when(formParser.getStudies()).thenReturn(Arrays.asList(study));

        mockServletContextToReturnValidators(new HashMap<String, FormValidator>() {{
            put(validatorClass, formValidator);
        }});

        final FormBeanGroup groupOne = new FormBeanGroup(Arrays.asList(successForm, failureForm));
        FormBeanGroup groupTwo = new FormBeanGroup(Arrays.asList(formInDifferentGroup));
        when(formValidator.validate(successForm, groupOne, formBeans)).thenReturn(Collections.EMPTY_LIST);
        when(formValidator.validate(failureForm, groupOne, formBeans)).thenReturn(formErrors);
        when(formValidator.validate(formInDifferentGroup, groupTwo, formBeans)).thenReturn(Collections.EMPTY_LIST);

        when(mobileFormsService.getFormIdMap()).thenReturn(formIdMap);

        populateHttpRequest(request, "username", "password", groupIndex);

        formController.uploadHandler(request, response);

        verify(formOutput).addStudy(study);

        verify(formOutput).writeFormErrors(any(DataOutputStream.class));

        ArgumentCaptor<FormBeanGroup> publishCaptor = ArgumentCaptor.forClass(FormBeanGroup.class);
        ArgumentCaptor<FormBeanGroup> logCaptor = ArgumentCaptor.forClass(FormBeanGroup.class);
        verify(formGroupPublisher,times(2)).publishFormsForLogging(logCaptor.capture());
        verify(formGroupPublisher, times(2)).publish(publishCaptor.capture());
        assertThat(publishCaptor.getAllValues(), is(equalTo(Arrays.asList(new FormBeanGroup(Arrays.asList(successForm, failureForm)), new FormBeanGroup(Arrays.asList(formInDifferentGroup))))));
        assertThat(logCaptor.getAllValues(), is(equalTo(Arrays.asList(new FormBeanGroup(Arrays.asList(successForm,failureForm)), new FormBeanGroup(Arrays.asList(formInDifferentGroup))))));

        verify(epihandySerializer).deserializeStudiesWithEvents(any(DataInputStream.class), eq(formIdMap));
    }

    @Test
    public void shouldReturnListOfValidatorInServletContext() throws Exception {
        when(servletContext.getAttributeNames()).thenReturn(Collections.enumeration(Arrays.asList("validator1", "validator2", "some_attribute_name")));
        final FormValidator formValidator1 = mock(FormValidator.class);
        final FormValidator formValidator2 = mock(FormValidator.class);

        when(servletContext.getAttribute("validator1")).thenReturn(formValidator1);
        when(servletContext.getAttribute("validator2")).thenReturn(formValidator2);

        Map<String, FormValidator> expected = new HashMap<String, FormValidator>() {{
            put("validator1", formValidator1);
            put("validator2", formValidator2);
        }};

        PowerMockito.doReturn(expected).when(formController, "getFormValidators");

        populateHttpRequest(request, "username", "password", groupIndex);
        formController.uploadHandler(request, response);

        PowerMockito.verifyPrivate(formController).invoke("getFormValidators");
    }

    private String readResponse(MockHttpServletResponse response) throws IOException {
        return new BufferedReader(new InputStreamReader(new ZInputStream(new ByteArrayInputStream(response.getContentAsByteArray())))).readLine();
    }

    private void populateHttpRequest(MockHttpServletRequest request, String userName,
                                     String password, byte actionCode,
                                     Integer groupIndex) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeUTF(userName);
        dataOutputStream.writeUTF(password);
        dataOutputStream.writeUTF("epihandyser");
        dataOutputStream.writeUTF("en");
        dataOutputStream.writeByte(actionCode);
        if (groupIndex != null) {
            dataOutputStream.writeInt(groupIndex);
        }

        request.setContent(byteArrayOutputStream.toByteArray());
    }

    private void populateHttpRequest(MockHttpServletRequest request, String userName,
                                     String password, Integer groupIndex) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteStream);
        dataOutputStream.writeUTF(userName);
        dataOutputStream.writeUTF(password);
        dataOutputStream.writeUTF("epihandyser");
        dataOutputStream.writeUTF("en");
        if (groupIndex != null) {
            dataOutputStream.writeInt(groupIndex);
        }

        request.setContent(byteStream.toByteArray());
    }

    private void mockServletContextToReturnValidators(Map<String, FormValidator> validators) {
        reset(servletContext);
        when(servletContext.getAttributeNames()).thenReturn(Collections.enumeration(validators.keySet()));
        for (Map.Entry<String, FormValidator> entry : validators.entrySet()) {
            when(servletContext.getAttribute(entry.getKey())).thenReturn(entry.getValue());
        }
    }
}
