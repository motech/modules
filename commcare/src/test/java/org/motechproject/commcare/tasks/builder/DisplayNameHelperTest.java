package org.motechproject.commcare.tasks.builder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisplayNameHelperTest {

    @Test
    public void shouldBuildDisplayNamesWithFourArguments() {
        assertEquals("Received Form: Birth [app-name: cfg-name]",
                DisplayNameHelper.buildDisplayName("Received Form", "Birth", "app-name", "cfg-name"));
        assertEquals("Location [cchq-demo]",
                DisplayNameHelper.buildDisplayName("Location", "", "", "cchq-demo"));
        assertEquals("Location [cchq-demo]",
                DisplayNameHelper.buildDisplayName("Location", null, null, "cchq-demo"));
    }

    @Test
    public void shouldBuildDisplayNamesWithTwoArguments() {
        assertEquals("User [myConf]", DisplayNameHelper.buildDisplayName("User", "myConf"));
    }
}
