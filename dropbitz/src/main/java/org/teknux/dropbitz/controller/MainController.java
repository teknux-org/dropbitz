package org.teknux.dropbitz.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.teknux.dropbitz.freemarker.View;
import org.teknux.dropbitz.provider.Authenticated;
import org.teknux.dropbitz.provider.AuthenticationFilter;
import org.teknux.dropbitz.provider.AuthenticationHelper;

@Path("/")
public class MainController extends AbstractController {
	
	private static final String SECURE_ID_ERROR_MESSAGE = "Incorrect Secure Id";
	
	@GET
	@Authenticated
	public Viewable index() {
		return viewable(View.DROP);
	}
	
	@POST
	@Path("authenticate")
    public Response authenticate(@FormParam("secureId") final String secureId) throws URISyntaxException {
	
		if (! AuthenticationHelper.authenticate(getHttpServletRequest(), secureId)) {
			getHttpServletRequest().getSession().setAttribute(AuthenticationFilter.SESSION_ATTRIBUTE_ERROR_MESSAGE, SECURE_ID_ERROR_MESSAGE);
		}

		return Response.seeOther(new URI("/")).build();
    }
}
