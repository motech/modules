package org.motechproject.mobileforms.api.web;

import com.jcraft.jzlib.ZInputStream;
import org.fcitmuk.epihandy.EpihandyXformSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.motechproject.mobileforms.api.callbacks.FormGroupPublisher;
import org.motechproject.mobileforms.api.callbacks.FormParser;
import org.motechproject.mobileforms.api.domain.FormBean;
import org.motechproject.mobileforms.api.domain.FormBeanGroup;
import org.motechproject.mobileforms.api.validator.TestFormBean;
import org.motechproject.mobileforms.api.validator.TestFormValidator;
import org.motechproject.mobileforms.api.vo.Study;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.motechproject.mobileforms.api.utils.TestUtilities.toPrimitive;
import static org.motechproject.mobileforms.api.utils.TestUtilities.slice;
import static org.motechproject.mobileforms.api.utils.TestUtilities.toObjectByteArray;
import static org.motechproject.mobileforms.api.utils.TestUtilities.readFully;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:META-INF/motech/*.xml")
@PrepareForTest( { FormController.class } )
public class FormControllerIT {
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletContext servletContext;
    @Mock
    FormGroupPublisher formGroupPublisher;
    @Mock
    private FormParser formParser;

    @Autowired
    private FormController formController;

    @Before
    public void setUp() {
        initMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        servletContext = new MockServletContext();
    }

    @Test
    public void shouldProcessUploadedFormAndReturnValidationErrorsIfErrorsAreFound() throws Exception {
        ReflectionTestUtils.setField(formController, "formGroupPublisher", formGroupPublisher);
        FormController controller = spy(formController);

        ReflectionTestUtils.setField(controller, "formParser", formParser);

        final TestFormBean formBeanWithOutError = new TestFormBean("study", "form1", "<xml>xml</xml>", TestFormValidator.class.getName(), "type", Collections.<String>emptyList(), "Abc", null);
        final TestFormBean formBeanWithError = new TestFormBean("study", "form2", "<xml>xml</xml>", TestFormValidator.class.getName(), "type", Collections.<String>emptyList(), "1Abc", null);
        List<FormBean> formBeans = Arrays.<FormBean>asList(formBeanWithOutError, formBeanWithError);

        Study study = new Study("study_name", formBeans);
        List<Study> studies = Arrays.asList(study);
        when(formParser.getStudies()).thenReturn(studies);

        EpihandyXformSerializer epihandyXformSerializer = spy(new EpihandyXformSerializer());
        doNothing().when(epihandyXformSerializer).deserializeStudiesWithEvents(any(DataInputStream.class), anyObject());

        ReflectionTestUtils.setField(controller, "serializer", epihandyXformSerializer);

        TestFormValidator testFormValidator = new TestFormValidator();
        servletContext.setAttribute(TestFormValidator.class.getName(), testFormValidator);
        ReflectionTestUtils.setField(controller, "servletContext", servletContext);

        setupRequestWithActionAndOtherRequestParameters(request, "username", "password", FormController.ACTION_DOWNLOAD_STUDY_LIST, null);

        controller.uploadHandler(request, response);

        DataInputStream responseSentToMobile = readUploadResponse(response);
        int expectedNoOfUploadedForms = formBeans.size();
        int expectedNoOfFailedForms = 1;
        int expectedStudyIndex = 0;
        int expectedFormIndex = 1;
        assertThat(responseSentToMobile.readByte(), is(equalTo(FormController.RESPONSE_SUCCESS)));
        assertThat(responseSentToMobile.readInt(), is(equalTo(expectedNoOfUploadedForms)));
        assertThat(responseSentToMobile.readInt(), is(equalTo(expectedNoOfFailedForms)));
        assertThat(responseSentToMobile.readByte(), is(equalTo((byte) expectedStudyIndex)));
        assertThat(responseSentToMobile.readShort(), is(equalTo((short) expectedFormIndex)));
        assertThat(responseSentToMobile.readUTF(), is(equalTo("Errors:firstName=wrong format")));

        verify(formGroupPublisher).publishFormsForLogging(new FormBeanGroup(Arrays.<FormBean>asList(formBeanWithError)));
        verify(formGroupPublisher).publish(new FormBeanGroup(Arrays.<FormBean>asList(formBeanWithOutError)));
    }

    @Test
    public void shouldReturnTheListOfFormGroups() throws Exception {
        List<Byte[]> responseSentToMobile = null;
        EpihandyXformSerializer serializer = new EpihandyXformSerializer();
        ByteArrayOutputStream expectedGroups = new ByteArrayOutputStream();

        serializer.serializeStudies(expectedGroups, Arrays.asList(new Object[]{0, "GroupNameI"}, new Object[]{1, "GroupNameII"}));

        setupRequestWithActionAndOtherRequestParameters(request, "username", "password", FormController.ACTION_DOWNLOAD_STUDY_LIST, null);

        formController.downloadHandler(request, response);
        responseSentToMobile = readDownloadResponse(response);

        assertThat(responseSentToMobile.get(0)[0], is(equalTo(FormController.RESPONSE_SUCCESS)));
        assertThat(toPrimitive(responseSentToMobile.get(1)), is(equalTo(expectedGroups.toByteArray())));
    }

    @Test
    public void shouldReturnListOfUserAccountsTogetherWithTheListOfFormsOfTheGroup_GivenTheIndexOfTheGroup() throws Exception {
        String motechUserSalt = "7357658437bd298b4a48b7357489357";
        String guyzbUserSalt = "135df6eacf3e3f21866ecff10378035edbf7";
        List<Object[]> expectedUserAccounts = Arrays.asList(new Object[]{1, "motech", "6f6347e4b28216556ec7dfa14d7dfadb873a15", motechUserSalt},
                new Object[]{2, "guyzb", "730c0e85d51b5c293b6ec8f22579ec7b67c5d8", guyzbUserSalt});
        String newLine = System.getProperty("line.separator");
        String expectedFormContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + newLine +
                "<xf:xforms xmlns:xf=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" id=\"7\">" + newLine +
                "  <xf:model>" + newLine +
                "    <xf:instance id=\"death\">" + newLine +
                "      <death id=\"7\" name=\"Client Death\">" + newLine +
                "        <staffId/>" + newLine +
                "      </death>" + newLine +
                "    </xf:instance>" + newLine +
                "    <xf:bind id=\"staffId\" nodeset=\"/death/staffId\" required=\"true()\" type=\"xsd:int\" constraint=\". &lt; 2147483647\" message=\"Number too large. Keep under 2147483647\"/>" + newLine +
                "  </xf:model>" + newLine +
                "</xf:xforms>";

        int groupIndex = 1;
        String expectedGroupName = "GroupNameII";
        EpihandyXformSerializer serializer = new EpihandyXformSerializer();
        ByteArrayOutputStream expectedSerializedFormsAndUsers = new ByteArrayOutputStream();

        serializer.serializeUsers(expectedSerializedFormsAndUsers, expectedUserAccounts);
        serializer.serializeForms(expectedSerializedFormsAndUsers, Arrays.asList(expectedFormContent), groupIndex, expectedGroupName);

        List<Byte[]> responseSentToMobile = null;

        setupRequestWithActionAndOtherRequestParameters(request, "username", "password", FormController.ACTION_DOWNLOAD_USERS_AND_FORMS, groupIndex);

        formController.downloadHandler(request, response);
        responseSentToMobile = readDownloadResponse(response);

        assertThat(response.getStatus(), is(equalTo(HttpServletResponse.SC_OK)));

        assertThat(responseSentToMobile.get(0)[0], is(equalTo(FormController.RESPONSE_SUCCESS)));
        assertThat(toPrimitive(responseSentToMobile.get(1)), is(equalTo(expectedSerializedFormsAndUsers.toByteArray())));
    }

    private DataInputStream readUploadResponse(MockHttpServletResponse response) {
        return new DataInputStream(new ZInputStream(new ByteArrayInputStream(response.getContentAsByteArray())));
    }

    private List<Byte[]> readDownloadResponse(MockHttpServletResponse response) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ZInputStream(new ByteArrayInputStream(response.getContentAsByteArray())));
        return slice(toObjectByteArray(readFully(dataInputStream)), 1);
    }

    private void setupRequestWithActionAndOtherRequestParameters(MockHttpServletRequest request, String userName, String password, byte actionCode, Integer groupIndex) throws IOException {
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
}
