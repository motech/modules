package org.motechproject.commcare.parser;

import org.junit.Test;
import org.motechproject.commcare.testutil.CommcareFormTestLoader;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.domain.CommcareMetadataJson;
import org.motechproject.commcare.domain.FormValueElement;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class FormAdapterTest {

    @Test
    public void shouldParseJsonFormHeader() {
        CommcareForm commcareForm = FormAdapter.readJson(CommcareFormTestLoader.sampleFormJson());
        assertEquals("2012-09-29T17:24:52", commcareForm.getReceivedOn());
    }

    @Test
    public void shouldParseFormList() throws IOException {
        CommcareFormList formList = FormAdapter.readListJson(CommcareFormTestLoader.formListJson());
        assertNotNull(formList);

        CommcareMetadataJson meta = formList.getMeta();
        assertNotNull(meta);

        assertEquals(2, meta.getLimit());
        assertEquals(6, meta.getTotalCount());
        assertEquals(0, meta.getOffset());
        assertNull(meta.getPreviousPageQueryString());
        assertNull(meta.getNextPageQueryString());

        List<CommcareForm> forms = formList.getObjects();
        assertNotNull(forms);
        assertEquals(2, forms.size());

        CommcareForm form1 = forms.get(0);
        CommcareForm form2 = forms.get(1);

        verifyFirstForm(form1);
        verifySecondForm(form2);
    }

    private void verifyFirstForm(CommcareForm form) {
        verifyCommonFormFields(form);

        assertEquals("7f29b60e-15af-4f26-ba57-dff706392768", form.getId());
        assertEquals("2013-08-06T12:43:05", form.getReceivedOn());
        assertEquals("17", form.getVersion());

        assertNotNull(form.getMetadata());
        assertEquals(10, form.getMetadata().size());
        // strange version, but that's what we have on our CHQ instance so might as well test it
        assertEquals("?? (??-??-??-??-??) b:?? r:--", form.getMetadata().get("appVersion").firstValue());
        assertEquals("7J8QFA5H0G0F4YX4S1N7MR426", form.getMetadata().get("deviceID").firstValue());
        assertEquals("7f29b60e-15af-4f26-ba57-dff706392768", form.getMetadata().get("instanceID").firstValue());
        assertEquals("2013-08-06T14:42:35", form.getMetadata().get("timeStart").firstValue());
        assertEquals("2013-08-06T14:42:59", form.getMetadata().get("timeEnd").firstValue());
        assertEquals("demo_user", form.getMetadata().get("userID").firstValue());
        assertEquals("demo_user", form.getMetadata().get("username").firstValue());
        assertEquals("Metadata", form.getMetadata().get("doc_type").firstValue());
        assertEquals(Arrays.asList("0.0", "0.0", "0.0", "0.5"), form.getMetadata().get("location").getValues());
        assertNull(form.getMetadata().get("deprecatedID"));

        assertNotNull(form.getForm());
        assertEquals("data", form.getForm().getValue());
        assertEquals("form", form.getForm().getElementName());
        assertNotNull(form.getForm().getSubElements());
        assertEquals(4, form.getForm().getSubElements().size());
        verifyFormSubElement(form, "age", "49");
        verifyFormSubElement(form, "ispregnant", "no");
        verifyFormSubElement(form, "name", "TERESA");
    }

    private void verifySecondForm(CommcareForm form) {
        verifyCommonFormFields(form);

        assertEquals("fe959aef-56e8-45c0-8c64-d7299bc18f77", form.getId());
        assertEquals("2013-08-01T11:33:08", form.getReceivedOn());
        assertEquals("7", form.getVersion());

        assertNotNull(form.getMetadata());
        assertEquals(10, form.getMetadata().size());
        // strange version, but that's what we have on our CHQ instance so might as well test it
        assertEquals("2.0", form.getMetadata().get("appVersion").firstValue());
        assertEquals("cloudcare", form.getMetadata().get("deviceID").firstValue());
        assertEquals("fe959aef-56e8-45c0-8c64-d7299bc18f77", form.getMetadata().get("instanceID").firstValue());
        assertEquals("2013-08-01T11:33:02", form.getMetadata().get("timeStart").firstValue());
        assertEquals("2013-08-01T11:33:07", form.getMetadata().get("timeEnd").firstValue());
        assertEquals("2a34e758b7ed8a686e7fe8de29c3078c", form.getMetadata().get("userID").firstValue());
        assertEquals("someone@soldevelo.com", form.getMetadata().get("username").firstValue());
        assertEquals("Metadata", form.getMetadata().get("doc_type").firstValue());
        assertNull(form.getMetadata().get("location"));
        assertNull(form.getMetadata().get("deprecatedID"));

        assertNotNull(form.getForm());
        assertEquals("data", form.getForm().getValue());
        assertEquals("form", form.getForm().getElementName());
        assertNotNull(form.getForm().getSubElements());
        assertEquals(2, form.getForm().getSubElements().size());
        verifyFormSubElement(form, "question11", "f");
    }

    private void verifyFormSubElement(CommcareForm form, String elementName, String value) {
        Collection<FormValueElement> valueElements = form.getForm().getSubElements().get(elementName);
        assertNotNull(valueElements);
        assertEquals(1, valueElements.size());

        FormValueElement element = valueElements.iterator().next();
        assertNotNull(element);
        assertEquals(elementName, element.getElementName());
        assertEquals(value, element.getValue());
    }

    private void verifyCommonFormFields(CommcareForm form) {
        assertEquals("OBSOLETED", form.getMd5());
        assertEquals("1", form.getUiversion());
        assertEquals("", form.getResourceUri());
        assertEquals("data", form.getType());
        assertFalse(form.isArchived());
    }
}
