package org.motechproject.csd.util;

import org.junit.Test;
import org.motechproject.csd.domain.CodedType;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.domain.Geocode;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class CSDReferenceFinderTest {

    @Test
    public void shouldFindReferencingFields() throws Exception {
        CSDReferenceFinder csdReferenceFinder = new CSDReferenceFinder();
        Collection<Field> fields = csdReferenceFinder.findReferencingFields(CodedType.class).values();
        assertEquals(10, fields.size());

        fields = csdReferenceFinder.findReferencingFields(Facility.class).values();
        assertEquals(0, fields.size());

        fields = csdReferenceFinder.findReferencingFields(Geocode.class).values();
        assertEquals(1, fields.size());
    }

}
