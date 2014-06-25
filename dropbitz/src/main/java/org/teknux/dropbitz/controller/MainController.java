package org.teknux.dropbitz.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.teknux.dropbitz.provider.AuthenticationHelper;

@Path("/")
public class MainController {
	
	public static final String SESSION_ATTRIBUTE_IS_RETRIED = "IS_RETRIED";
	
	@Context
	private HttpServletRequest request;
	
	@GET
	public Viewable index() {
		
		if (AuthenticationHelper.isSecured(request)) {
			return new Viewable("drop");
		}

		Boolean retry = (Boolean) request.getSession().getAttribute(SESSION_ATTRIBUTE_IS_RETRIED);
		if (retry != null && retry) {
			request.getSession().setAttribute(SESSION_ATTRIBUTE_IS_RETRIED, Boolean.FALSE);
		}
                
		return new Viewable("index", retry);
	}
	
	@POST
	@Path("authenticate")
    public Response authenticate(@FormParam("secureId") final String secureId) throws URISyntaxException {
	
		if (! AuthenticationHelper.authenticate(request, secureId)) {
			request.getSession().setAttribute(SESSION_ATTRIBUTE_IS_RETRIED, Boolean.TRUE);
		}

		return Response.seeOther(new URI("/")).build();
    }
}
