package org.teknux.dropbitz.test.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.provider.AuthenticationHelper;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.IServiceManager;
import org.teknux.dropbitz.util.DropBitzServlet;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationHelperTest {

    private HttpSession session = mock(HttpSession.class);

    private HttpServletRequest request = mock(HttpServletRequest.class);

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private void setup(Boolean isSecured, String secureId) {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_IS_SECURED)).thenReturn(isSecured);

        ServletContext servletContext = mock(ServletContext.class);
        when(request.getServletContext()).thenReturn(servletContext);

        IServiceManager serviceManager = mock(IServiceManager.class);
        when(servletContext.getAttribute(DropBitzServlet.CONTEXT_ATTRIBUTE_SERVICE_MANAGER)).thenReturn(serviceManager);

        IConfigurationService configurationService = mock(IConfigurationService.class);
        when(serviceManager.getService(IConfigurationService.class)).thenReturn(configurationService);

        Configuration configuration = mock(Configuration.class);
        when(configuration.getSecureId()).thenReturn(secureId);
        when(configurationService.getConfiguration()).thenReturn(configuration);
    }

    public void testAuthenticate() {
        AuthenticationHelper authenticationHelper = new AuthenticationHelper();

        setup(true, "123");
        Assert.assertTrue(authenticationHelper.authenticate(request, "123"));
        verify(session).setAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_IS_SECURED, Boolean.TRUE);

        setup(true, "123");
        Assert.assertFalse(authenticationHelper.authenticate(request, "321"));
        verify(session).setAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_IS_SECURED, Boolean.FALSE);

        setup(true, "");
        Assert.assertTrue(authenticationHelper.authenticate(request, "123"));
        verify(session).setAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_IS_SECURED, Boolean.TRUE);

        setup(true, "");
        Assert.assertTrue(authenticationHelper.authenticate(request, "321"));
        verify(session).setAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_IS_SECURED, Boolean.TRUE);
    }

    public void testIsAuthorized() {
        AuthenticationHelper authenticationHelper = new AuthenticationHelper();

        setup(false, "123");
        Assert.assertFalse(authenticationHelper.isAuthorized(request));

        setup(true, "123");
        Assert.assertTrue(authenticationHelper.isAuthorized(request));
    }

    public void testIsLogged() {
        AuthenticationHelper authenticationHelper = new AuthenticationHelper();

        setup(false, "123");
        Assert.assertFalse(authenticationHelper.isLogged(request));

        setup(true, "123");
        Assert.assertTrue(authenticationHelper.isLogged(request));

        setup(false, "");
        Assert.assertFalse(authenticationHelper.isLogged(request));

        setup(true, "");
        Assert.assertTrue(authenticationHelper.isLogged(request));
    }

    @Test
    public void testLogout() {
        setup(true, "123");

        AuthenticationHelper authenticationHelper = new AuthenticationHelper();
        authenticationHelper.logout(request);
        verify(session).removeAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_IS_SECURED);
    }
}
