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
import org.teknux.dropbitz.provider.Authenticated;
import org.teknux.dropbitz.provider.AuthenticationFilter;
import org.glassfish.jersey.server.mvc.Viewable;
import org.teknux.dropbitz.provider.AuthenticationHelper;

@Path("/")
public class MainController {
	
	private static final String SECURE_ID_ERROR_MESSAGE = "Incorrect Secure Id";
	
	@Context
	private HttpServletRequest request;
	
	@GET
	@Authenticated
	public Viewable index() {
		return new Viewable("/main/drop");
	}
	
	@POST
	@Path("authenticate")
    public Response authenticate(@FormParam("secureId") final String secureId) throws URISyntaxException {
	
		if (! AuthenticationHelper.authenticate(request, secureId)) {
			request.getSession().setAttribute(AuthenticationFilter.SESSION_ATTRIBUTE_ERROR_MESSAGE, SECURE_ID_ERROR_MESSAGE);
		}

		return Response.seeOther(new URI("/")).build();
    }
}
