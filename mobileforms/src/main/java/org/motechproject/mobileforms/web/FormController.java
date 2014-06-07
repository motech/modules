package org.motechproject.mobileforms.web;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;
import org.fcitmuk.epihandy.EpihandyXformSerializer;
import org.motechproject.mobileforms.callbacks.FormGroupPublisher;
import org.motechproject.mobileforms.callbacks.FormParser;
import org.motechproject.mobileforms.parser.FormDataParser;
import org.motechproject.mobileforms.repository.AllMobileForms;
import org.motechproject.mobileforms.service.MobileFormsService;
import org.motechproject.mobileforms.service.UsersService;
import org.motechproject.mobileforms.utils.MapToBeanConvertor;
import org.motechproject.mobileforms.validator.FormValidator;
import org.motechproject.mobileforms.domain.Study;
import org.motechproject.mobileforms.domain.Form;
import org.motechproject.mobileforms.domain.FormBean;
import org.motechproject.mobileforms.domain.FormBeanGroup;
import org.motechproject.mobileforms.domain.FormGroup;
import org.motechproject.mobileforms.validator.FormGroupValidator;
import org.motechproject.mobileforms.domain.FormOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.serializer.support.SerializationFailedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;

import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.collect;
import static ch.lambdaj.Lambda.on;


@Controller
public class FormController {
    private final Logger log = LoggerFactory.getLogger(FormController.class);

    @Autowired
    private UsersService usersService;

    @Autowired
    private FormGroupPublisher formGroupPublisher;

    private FormGroupValidator formGroupValidator;

    @Autowired
    private MobileFormsService mobileFormsService;

    @Autowired
    private AllMobileForms allMobileForms;

    private ServletContext servletContext;

    @Autowired
    private Properties mobileFormsProperties;

    private EpihandyXformSerializer serializer;

    private FormParser formParser;

    public static final byte RESPONSE_ERROR = 0;
    public static final byte RESPONSE_SUCCESS = 1;

    public static final byte ACTION_DOWNLOAD_STUDY_LIST = 2;
    public static final byte ACTION_DOWNLOAD_USERS_AND_FORMS = 11;

    public static final String FAILED_TO_SERIALIZE_DATA = "failed to serialize data";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    public FormController() {
        formGroupValidator = new FormGroupValidator();
        serializer = new EpihandyXformSerializer();
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void downloadHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ZOutputStream zOutput = new ZOutputStream(response.getOutputStream(), JZlib.Z_BEST_COMPRESSION);
        DataInputStream dataInput = new DataInputStream(request.getInputStream());
        DataOutputStream dataOutput = new DataOutputStream(zOutput);
        try {
            readParameters(dataInput);
            byte action = readActionByte(dataInput);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            if (action == ACTION_DOWNLOAD_STUDY_LIST) {
                handleDownloadStudies(byteStream);

            } else if (action == ACTION_DOWNLOAD_USERS_AND_FORMS) {
                handleDownloadUsersAndForms(byteStream, dataInput);
            }

            dataOutput.writeByte(RESPONSE_SUCCESS);
            dataOutput.write(byteStream.toByteArray());
            log.info("successfully downloaded the xforms");
        } catch (Exception e) {
            dataOutput.writeByte(RESPONSE_ERROR);
            throw new SerializationFailedException(FAILED_TO_SERIALIZE_DATA, e);
        } finally {
            dataOutput.close();
            response.flushBuffer();
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void uploadHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ZOutputStream zOutput = new ZOutputStream(response.getOutputStream(), JZlib.Z_BEST_COMPRESSION);
        DataInputStream dataInput = new DataInputStream(request.getInputStream());
        DataOutputStream dataOutput = new DataOutputStream(zOutput);
        FormOutput formOutput = getFormOutput();
        try {
            readParameters(dataInput);
            readActionByte(dataInput);

            List<Study> studies = extractBeans(dataInput);
            final Map<String, FormValidator> formValidators = getFormValidators();
            List<FormBean> allForms = flatten(collect(studies, on(Study.class).forms()));
            for (Study study : studies) {
                for (FormBeanGroup group : study.groupedForms()) {
                    formGroupPublisher.publishFormsForLogging(group);
                    formGroupValidator.validate(group, formValidators, allForms);
                    formGroupPublisher.publish(new FormBeanGroup(new FormBeanGroup(group.validForms()).sortByDependency()));
                }
                formOutput.addStudy(study);
            }

            response.setContentType(APPLICATION_OCTET_STREAM);
            formOutput.writeFormErrors(dataOutput);
        } catch (Exception e) {
            log.error("Error in uploading form:", e);
            dataOutput.writeByte(RESPONSE_ERROR);
            throw new SerializationFailedException(FAILED_TO_SERIALIZE_DATA, e);
        } finally {
            dataOutput.close();
            response.flushBuffer();
        }
    }

    private FormOutput getFormOutput() {
        return new FormOutput();
    }

    private Map<String, FormValidator> getFormValidators() {
        Map<String, FormValidator> validators = new HashMap<>();
        final Enumeration attributeNames = servletContext.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            final String attributeName = (String) attributeNames.nextElement();
            final Object attributeValue = servletContext.getAttribute(attributeName);
            if (attributeValue instanceof FormValidator) {
                validators.put(attributeName, (FormValidator) attributeValue);
            }
        }
        return validators;
    }

    private void readParameters(DataInputStream dataInput) throws IOException {
        dataInput.readUTF(); // name
        dataInput.readUTF(); // password
        dataInput.readUTF(); // getSerializer
        dataInput.readUTF(); // locale
    }

    private byte readActionByte(DataInputStream dataInput) throws IOException {
        return dataInput.readByte();
    }

    private void handleDownloadStudies(ByteArrayOutputStream byteStream) throws SerializerException {
        try {
            serializer.serializeStudies(byteStream, mobileFormsService.getAllFormGroups());
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }

    private void handleDownloadUsersAndForms(ByteArrayOutputStream byteStream, DataInputStream dataInput)
            throws IOException, SerializerException {
        try {
            serializer.serializeUsers(byteStream, usersService.getUsers());
        } catch (Exception e) {
            throw new SerializerException(e);
        }

        int studyIndex = dataInput.readInt();
        FormGroup groupNameAndForms = mobileFormsService.getForms(studyIndex);
        List<String> formsXmlContent = collect(groupNameAndForms.getForms(), on(Form.class).content());
        try {
            serializer.serializeForms(byteStream, formsXmlContent, studyIndex, groupNameAndForms.getName());
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }

    private List<Study> extractBeans(DataInputStream dataInput) throws SerializerException {
        if (formParser == null) {
            String marker = mobileFormsProperties.getProperty("forms.xml.form.name");
            formParser = new FormParser(new FormDataParser(), new MapToBeanConvertor(), allMobileForms, marker);
            serializer.addDeserializationListener(formParser);
        }

        try {
            serializer.deserializeStudiesWithEvents(dataInput, mobileFormsService.getFormIdMap());
        } catch (Exception e) {
            throw new SerializerException(e);
        }

        return formParser.getStudies();
    }
}
