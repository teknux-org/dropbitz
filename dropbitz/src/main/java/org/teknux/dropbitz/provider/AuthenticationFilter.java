package org.teknux.dropbitz.provider;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.controller.MainController;

/**
 * Custom authentication filter used to control decorated Jax-RS resource with @Authenticated annotation.
 * It checks whether or not the request is allowed.
 */
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

	private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	private static final String FORBIDDEN_ERROR_MESSAGE = "You don't have authorisation. Thank you to authenticate";
		
    @Context
    private HttpServletRequest httpServletRequest;
    
    @Context  
    private HttpServletResponse httpServletResponse;  

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    	logger.trace("Request on URI [{}]...", containerRequestContext.getUriInfo().getPath());
    	
    	//Check if authenticated
        if (!AuthenticationHelper.isSecured(httpServletRequest)) {
        	logger.debug("Not authenticate, redirect to auth view...");
        	
        	//If page is different to root page, set error message
        	if (! containerRequestContext.getUriInfo().getPath().isEmpty()) {
        	    httpServletRequest.getSession().setAttribute(MainController.SESSION_ATTRIBUTE_ERROR_MESSAGE, FORBIDDEN_ERROR_MESSAGE);
        	}
        	
        	//Redirect to auth page
    	    httpServletResponse.sendRedirect("/auth");
        }
    }
}
