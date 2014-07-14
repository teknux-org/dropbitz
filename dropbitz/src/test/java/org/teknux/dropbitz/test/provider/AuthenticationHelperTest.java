package org.teknux.dropbitz.test.provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.provider.AuthenticationHelper;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.IServiceManager;
import org.teknux.dropbitz.util.DropBitzServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationHelperTest {

    private HttpSession session = mock(HttpSession.class);

    @Mock
    private HttpServletRequest request = mock(HttpServletRequest.class);

    @Mock
    private HttpServletResponse response = mock(HttpServletResponse.class);

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

    public void testIsSecured() {
        AuthenticationHelper authenticationHelper = new AuthenticationHelper();

        setup(false, "123");
        Assert.assertFalse(authenticationHelper.isSecured(request));

        setup(true, "123");
        Assert.assertTrue(authenticationHelper.isSecured(request));
    }

    @Test
    public void testLogout() {
        setup(true, "123");

        AuthenticationHelper authenticationHelper = new AuthenticationHelper();
        authenticationHelper.logout(request);
        verify(session).removeAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_IS_SECURED);
    }
}
