package org.teknux.dropbitz.test.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.teknux.dropbitz.contant.Route;
import org.teknux.dropbitz.controller.MainController;
import org.teknux.dropbitz.provider.AuthenticationFilter;
import org.teknux.dropbitz.provider.AuthenticationHelper;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilterTest {

    private ContainerRequestContext containerContext = mock(ContainerRequestContext.class);
    private HttpSession session = mock(HttpSession.class);

    @Mock
    private ServletContext servletContext = mock(ServletContext.class);

    @Mock
    private HttpServletRequest request = mock(HttpServletRequest.class);

    @Mock
    private HttpServletResponse response = mock(HttpServletResponse.class);

    @Mock
    private AuthenticationHelper authenticationHelper;

    @InjectMocks
    private AuthenticationFilter filter;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private void setup(String ctxPath, String url, Boolean isSecured) {
        when(servletContext.getContextPath()).thenReturn(ctxPath);

        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getPath()).thenReturn(url);

        when(containerContext.getUriInfo()).thenReturn(uriInfo);
        when(request.getSession()).thenReturn(session);
        when(authenticationHelper.isSecured(request)).thenReturn(isSecured);
    }

    @Test
    public void testNotAuthenticated() throws IOException {
        setup("", "/test", false);

        //run filter
        filter.filter(containerContext);

        //verify the redirect has been invoked
        verify(response).sendRedirect(Route.AUTH);
        //verify the error message has been set
        verify(session).setAttribute(MainController.SESSION_ATTRIBUTE_ERROR_MESSAGE, AuthenticationFilter.FORBIDDEN_ERROR_MESSAGE);
    }

    @Test
    public void testAuthenticated() throws IOException {
        setup("", "/test", true);

        //run filter
        filter.filter(containerContext);

        //verify the redirect has not been invoked
        verify(response, never()).sendRedirect(Route.AUTH);
        //verify no error message has been set
        verify(session, never()).setAttribute(MainController.SESSION_ATTRIBUTE_ERROR_MESSAGE, AuthenticationFilter.FORBIDDEN_ERROR_MESSAGE);
    }
}
