package org.motechproject.commcare.service.impl;

import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.commcare.testutil.CommcareFormTestLoader;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.domain.FormNode;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.domain.MetadataValue;
import org.motechproject.commcare.request.FormListRequest;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ConfigsUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommcareFormServiceImplTest {

    private CommcareFormServiceImpl formService;

    @Mock
    private CommCareAPIHttpClient commcareHttpClient;

    @Mock
    private CommcareConfigService configService;

    @Before
    public void setUp() {
        initMocks(this);

        Config config = ConfigsUtils.prepareConfigOne();

        when(configService.getByName(null)).thenReturn(config);

        formService = new CommcareFormServiceImpl(commcareHttpClient, configService);
    }

    @Test
    public void testFormOne() {
        when(commcareHttpClient.formRequest(Matchers.any(AccountConfig.class), anyString()))
                .thenReturn(CommcareFormTestLoader.registrationFormJson());

        CommcareForm form = formService.retrieveForm("testForm1");

        assertEquals(form.getId(), "4432638b-8f77-42f5-8c65-9fc18b26fb09");
        assertEquals(form.getMd5(), "75cb64ec8ead19684895e0822a960149");
        assertEquals(form.getResourceUri(), "");
        assertEquals(form.getType(), "data");
        assertEquals(form.getUiversion(), "1");
        assertEquals(form.getVersion(), "1");

        Map<String, MetadataValue> metaData = form.getMetadata();

        assertNotNull(metaData);
        assertEquals(metaData.size(), 9);

        assertEquals(metaData.get("@xmlns").firstValue(), "http://openrosa.org/jr/xforms");
        assertEquals(
                metaData.get("appVersion").firstValue(),
                "@xmlns:http://commcarehq.org/xforms, #text:CommCare ODK, version \"2.0\"(1090). CommCare Version 2.0. Build #1090, built on: May-23-2012");
        assertEquals(metaData.get("deviceID").firstValue(), "A000002A46308E");
        assertEquals(metaData.get("instanceID").firstValue(), "4432638b-8f77-42f5-8c65-9fc18b26fb09");
        assertEquals(metaData.get("timeEnd").firstValue(), "2012-06-22T14:26:54.046462Z");
        assertEquals(metaData.get("timeStart").firstValue(), "2012-06-22T14:26:01.046462Z");
        assertEquals(metaData.get("userID").firstValue(), "abc707434d4ec780967fa65b7167cc58");
        assertEquals(metaData.get("username").firstValue(), "test");
        assertNull(metaData.get("deprecatedID"));

        FormValueElement rootElement = form.getForm();

        Map<String, String> topLevelAttributes = rootElement.getAttributes();

        assertEquals(topLevelAttributes.size(), 4);
        assertEquals(rootElement.getValue(), "data");

        List<FormValueElement> elements = rootElement.getElementsByAttribute("concept_id", "5596");
        assertEquals(elements.size(), 1);

        assertEquals("form", rootElement.getElementName());

        Multimap<String, FormValueElement> subElements = rootElement.getSubElements();
        assertNotNull(subElements.get("case"));
        assertEquals(subElements.size(), 15);

        Collection<FormValueElement> childElement = subElements.get("family_planning_acceptance");
        FormValueElement firstElement = childElement.iterator().next();
        assertNotNull(firstElement);
        assertEquals(firstElement.getSubElements().size(), 3);
    }

    @Test
    public void testFormTwo() {
        when(commcareHttpClient.formRequest(any(AccountConfig.class), anyString()))
                .thenReturn(CommcareFormTestLoader.testFormTwoJson());

        CommcareForm form = formService.retrieveForm("testForm2");

        FormValueElement rootElement = form.getForm();

        List<FormValueElement> elementsByName = rootElement.getAllElements("top_level_value_1");
        assertEquals(2, elementsByName.size());

        elementsByName = rootElement.getChildElements("value3");
        assertEquals(1, elementsByName.size());
        assertEquals("no", elementsByName.get(0).getValue());


        elementsByName = rootElement.getAllElements("value3");
        assertEquals(3, elementsByName.size());

        List<String> restrictedElements = new ArrayList<>();
        restrictedElements.add("registration");
        elementsByName = rootElement.getAllElements("value3", restrictedElements);
        assertEquals(2, elementsByName.size());
        assertEquals("no", elementsByName.get(0).getValue());
        assertEquals("innervalue", elementsByName.get(1).getValue());

        elementsByName = rootElement.getAllElements("noName");
        assertEquals(0, elementsByName.size());

        elementsByName = rootElement.getAllElements("case");
        assertEquals(2, elementsByName.size());

        FormValueElement caseElementOne = elementsByName.get(0);

        FormValueElement caseElementTwo = elementsByName.get(1);

        assertEquals(1, caseElementOne.getSubElements().size());

        assertEquals(3, caseElementTwo.getSubElements().size());

        assertNull(caseElementOne.getElement("create"));

        assertNotNull(caseElementTwo.getElement("create"));
    }

    @Test
    public void shouldSearchFromStartElement() {
        when(commcareHttpClient.formRequest(any(AccountConfig.class), anyString()))
                .thenReturn(CommcareFormTestLoader.testFormJson());

        CommcareForm form = formService.retrieveForm("testForm");

        FormValueElement rootElement = form.getForm();

        List<String> restrictedElements = new ArrayList<>();
        restrictedElements.add("restricted");
        restrictedElements.add("anotherRestricted");
        FormValueElement value = rootElement.getElement("value", restrictedElements);
        assertEquals("1", value.getValue());

        FormValueElement subelement1 = rootElement.getElement("subelement1");
        FormValueElement valueInsideSublement1 = subelement1.getElement("value", restrictedElements);
        assertEquals("4", valueInsideSublement1.getValue());

        FormValueElement valueWithoutRestriction = subelement1.getElement("value");
        assertEquals("2", valueWithoutRestriction.getValue());
    }

    @Test
    public void shouldSearchFromRootPath() {
        when(commcareHttpClient.formRequest(any(AccountConfig.class), anyString()))
                .thenReturn(CommcareFormTestLoader.testFormJson());
        CommcareForm form = formService.retrieveForm("testForm");
        FormValueElement rootElement = form.getForm();

        FormNode elementByPath = rootElement.searchFirst("//subelement1/value");

        assertEquals("4", elementByPath.getValue());
    }

    @Test
    public void shouldSearchFromRootPathWithoutRestrictedElements() {
        when(commcareHttpClient.formRequest(any(AccountConfig.class), anyString()))
                .thenReturn(CommcareFormTestLoader.testFormJson());
        CommcareForm form = formService.retrieveForm("testForm");
        FormValueElement rootElement = form.getForm();

        FormNode elementByPath = rootElement.searchFirst("/form/restricted");

        assertNull(elementByPath);
    }

    @Test
    public void shouldSearchByCurrentPath() {
        when(commcareHttpClient.formRequest(any(AccountConfig.class), anyString()))
                .thenReturn(CommcareFormTestLoader.testFormJson());
        CommcareForm form = formService.retrieveForm("testForm");
        FormValueElement rootElement = form.getForm().getElement("subelement1");

        FormNode elementByCurrentPath = rootElement.searchFirst("//validElement/value");

        assertEquals("5", elementByCurrentPath.getValue());
    }

    @Test
    public void shouldSearchForAttributes() {
        when(commcareHttpClient.formRequest(any(AccountConfig.class), anyString()))
                .thenReturn(CommcareFormTestLoader.registrationFormJson());
        CommcareForm form = formService.retrieveForm("testForm");
        FormValueElement rootElement = form.getForm();

        FormNode elementByPath = rootElement.searchFirst("//case/@case_id");

        assertEquals("6bc2f8f6-b1da-4be2-98d4-1cb2d557a329", elementByPath.getValue());
    }

    @Test
    public void shouldSearchForValue() {
        when(commcareHttpClient.formRequest(any(AccountConfig.class), anyString()))
                .thenReturn(CommcareFormTestLoader.registrationFormJson());
        CommcareForm form = formService.retrieveForm("testForm");
        FormValueElement rootElement = form.getForm();

        FormNode elementByPath = rootElement.searchFirst("//#");

        assertEquals("data", elementByPath.getValue());
    }

    @Test
    public void shouldReturnFormList() {
        final FormListRequest formListRequest = mock(FormListRequest.class);
        when(commcareHttpClient.formListRequest(any(AccountConfig.class), eq(formListRequest)))
                .thenReturn(CommcareFormTestLoader.formListJson());

        CommcareFormList formList = formService.retrieveFormList(formListRequest);

        basicListVerification(formList);
    }

    @Test
    public void shouldAllowNullRequestForFormList() {
        when(commcareHttpClient.formListRequest(any(AccountConfig.class), eq((FormListRequest) null)))
                .thenReturn(CommcareFormTestLoader.formListJson());

        CommcareFormList formList = formService.retrieveFormList(null);

        basicListVerification(formList);
    }

    private void basicListVerification(CommcareFormList formList) {
        // parsing tested in FormAdapterTest
        assertNotNull(formList);
        assertNotNull(formList.getMeta());
        assertNotNull(formList.getObjects());
        assertEquals(2, formList.getObjects().size());
    }
}
