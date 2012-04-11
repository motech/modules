package org.motechproject.casexml.utils;

import org.junit.Test;
import org.motechproject.casexml.domain.Case;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class CaseMapperTest {

    @Test
    public void shouldMapValuesToObject(){
        CaseMapper<DomainObj> mapper = new CaseMapper<DomainObj>(DomainObj.class);
        Case ccCase = new Case();

        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put("field1", "someValue");
        ccCase.setFieldValues(fieldValues);
        DomainObj domainObj = mapper.mapToDomainObject(ccCase);

        assertEquals(domainObj.getField1(), "someValue");
        assertNull(domainObj.getField2());
        assertNull(domainObj.getDateField());
    }
}

