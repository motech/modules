package org.motechproject.dhis2.it;

import org.junit.After;
import org.junit.Before;

public abstract class Dhis2ServerBaseIT extends BaseDhisIT {
    private Dhis2DummyServer dhis2Server = new Dhis2DummyServer();

    @Before
    public void setUpServer() {
        dhis2Server.start();
    }

    @After
    public void tearDown() {
        dhis2Server.stop();
    }
}
