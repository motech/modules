package org.motechproject.alerts.it;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.motechproject.alerts.domain.AlertStatus;
import org.motechproject.alerts.domain.AlertType;
import org.motechproject.testing.osgi.http.SimpleHttpClient;
import org.motechproject.testing.utils.TestContext;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AlertsControllerBundleIT  extends AlertsBaseIT {

    @Test
    public void verifyController() throws InterruptedException, IOException {
        String url = String.format("http://localhost:%d/alerts/", TestContext.getJettyPort());

        alertService.create("foo1", "bar1", "baz1", AlertType.CRITICAL, AlertStatus.NEW, 0, null);
        alertService.create("foo2", "bar2", "baz2", AlertType.CRITICAL, AlertStatus.NEW, 0, null);
        alertService.create("foo3", "bar3", "baz3", AlertType.CRITICAL, AlertStatus.READ, 0, null);

        HttpGet httpGet = new HttpGet(url + "newAlertCount");
        HttpResponse response = SimpleHttpClient.exec(httpGet);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("2", EntityUtils.toString(response.getEntity()));

        httpGet = new HttpGet(url + "readAlertCount");
        response = SimpleHttpClient.exec(httpGet);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("1", EntityUtils.toString(response.getEntity()));

        httpGet = new HttpGet(url + "newOrReadAlertCount");
        response = SimpleHttpClient.exec(httpGet);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("3", EntityUtils.toString(response.getEntity()));

        httpGet = new HttpGet(url + "closedAlertCount");
        response = SimpleHttpClient.exec(httpGet);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("0", EntityUtils.toString(response.getEntity()));
    }
}
