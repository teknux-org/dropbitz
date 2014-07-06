package org.teknux.dropbitz.provider;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.freemarker.View;

/**
 * Custom authentication filter used to control decorated Jax-RS resource with @Authenticated annotation.
 * It checks whether or not the request is allowed.
 */
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

	private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "ERROR_MESSAGE";
	private static final String FORBIDDEN_ERROR_MESSAGE = "You don't have authorisation. Thank you to authenticate";
	
    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    	logger.trace("Request on URI [{}]...", containerRequestContext.getUriInfo().getPath());
    	
        if (!AuthenticationHelper.isSecured(request)) {
        	logger.debug("Not authenticate, redirect to auth view...");
        	
    		String errorMessage = (String) request.getSession().getAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE);
    		if (errorMessage != null) {
    			request.getSession().removeAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE);
    		} else if (! containerRequestContext.getUriInfo().getPath().isEmpty()) {
    			errorMessage = FORBIDDEN_ERROR_MESSAGE;
    		}

    		containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(new Viewable(View.AUTH.getTemplateName(), errorMessage)).build());
        }
    }
}
