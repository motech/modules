package org.motechproject.http.agent.factory;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.motechproject.config.SettingsFacade;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

/**
 * A factory used by a RestTemplate. This factory provides authentication details
 * per request.
 */
public class HttpComponentsClientHttpRequestFactoryWithAuth extends HttpComponentsClientHttpRequestFactory {

    private static final String HTTP_AUTH_PREEMPTIVE = "http.auth.preemptive";
    private final String username;
    private final String password;
    private final SettingsFacade settings;

    public HttpComponentsClientHttpRequestFactoryWithAuth(String username, String password, SettingsFacade settings) {
        this.username = username;
        this.password = password;
        this.settings = settings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
        BasicHttpContext localContext = new BasicHttpContext();
        if (Boolean.valueOf(settings.getProperty(HTTP_AUTH_PREEMPTIVE))) {
            HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
            BasicScheme basicScheme = new BasicScheme();
            BasicAuthCache authCache = new BasicAuthCache();
            authCache.put(targetHost, basicScheme);
            localContext.setAttribute(ClientContext.AUTH_CACHE, authCache);
        }

        return createHttpContext(localContext);
    }

    private HttpContext createHttpContext(BasicHttpContext localContext) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        localContext.setAttribute(ClientContext.CREDS_PROVIDER, credentialsProvider);

        return localContext;
    }
}
