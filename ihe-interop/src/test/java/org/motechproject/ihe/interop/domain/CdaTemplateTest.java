package org.motechproject.ihe.interop.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CdaTemplateTest {

    @Test
    public void shouldRemoveCommentsFromXml() {
        String templateWithComments = "<ClinicalDocument>\n" +
                "<!--  This is comment line -->\n" +
                "</ClinicalDocument>";
        byte[] bytes = templateWithComments.getBytes();
        Byte[] byteObjects = new Byte[bytes.length];
        int i = 0;
        for (byte b : bytes) {
            byteObjects[i++] = b;
        }
        String templateWithoutComments = "<ClinicalDocument>\n" +
                "\n" +
                "</ClinicalDocument>";

        CdaTemplate cdaTemplate = new CdaTemplate();
        cdaTemplate.setTemplateData(byteObjects);
        
        bytes = new byte[cdaTemplate.getTemplateData().length];
        i = 0;
        for (Byte b : cdaTemplate.getTemplateData()) {
            bytes[i++] = b;
        }

        assertEquals(templateWithoutComments, new String(bytes));
    }
}
