package org.motechproject.ipf.service.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IPFInitializerTest {

    IPFInitializerImpl ipfInitializer = new IPFInitializerImpl();

    @Test
    public void someTest() {
        assertEquals("Groovy compiled", ipfInitializer.init());
    }
}
