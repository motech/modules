package org.motechproject.ihe.interop.service.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IHEInitializerTest {

    IHEInitializerImpl iheInitializer = new IHEInitializerImpl();

    @Test
    public void testInitialization() {
        assertEquals("Groovy compiled", iheInitializer.init());
    }
}
