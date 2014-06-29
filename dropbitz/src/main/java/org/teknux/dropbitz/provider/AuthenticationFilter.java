package org.teknux.dropbitz.provider;

import javax.servlet.http.HttpServletRequest;
import org.glassfish.jersey.server.mvc.Viewable;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Custom authentication filter used to control decorated Jax-RS resource with @Authenticated annotation.
 * It checks whether or not the request is allowed.
 */
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

	public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "ERROR_MESSAGE";
	private static final String FORBIDDEN_ERROR_MESSAGE = "You don't have authorisation. Thank you to authenticate";
	
    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        if (!AuthenticationHelper.isSecured(request)) {
        	
    		String errorMessage = (String) request.getSession().getAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE);
    		if (errorMessage != null) {
    			request.getSession().removeAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE);
    		} else if (! containerRequestContext.getUriInfo().getPath().isEmpty()) {
    			errorMessage = FORBIDDEN_ERROR_MESSAGE;
    		}

    		containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(new Viewable("/main/auth", errorMessage)).build());
        }
    }
}
