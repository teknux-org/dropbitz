package org.teknux.dropbitz.provider;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.freemarker.View;
import org.teknux.dropbitz.model.view.AuthModel;

/**
 * Custom authentication filter used to control decorated Jax-RS resource with @Authenticated annotation.
 * It checks whether or not the request is allowed.
 */
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

	private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "ERROR_MESSAGE";
	private static final String FORBIDDEN_ERROR_MESSAGE = "You don't have authorisation. Thank you to authenticate";
	
	@Inject
	private ServletContext servletContext;
	
    @Context
    private HttpServletRequest httpServletRequest;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    	logger.trace("Request on URI [{}]...", containerRequestContext.getUriInfo().getPath());
    	
        if (!AuthenticationHelper.isSecured(httpServletRequest)) {
        	logger.debug("Not authenticate, redirect to auth view...");
        	
    		containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(getAuthView(containerRequestContext.getUriInfo().getPath())).build());
        }
    }
    
    private Viewable getAuthView(String path) {
        String errorMessage = (String) httpServletRequest.getSession().getAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE);
        if (errorMessage != null) {
            httpServletRequest.getSession().removeAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE);
        } else if (! path.isEmpty()) {
            errorMessage = FORBIDDEN_ERROR_MESSAGE;
        }
        
        AuthModel authModel = new AuthModel();
        authModel.setServletContext(servletContext);
        authModel.setHttpServletRequest(httpServletRequest);
        authModel.setErrorMessage(errorMessage);
        
        return new Viewable(View.AUTH.getTemplateName(), authModel);
    }
}
