package org.teknux.dropbitz.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.mvc.Viewable;
import org.teknux.dropbitz.freemarker.View;
import org.teknux.dropbitz.model.Message.Type;
import org.teknux.dropbitz.provider.Authenticated;
import org.teknux.dropbitz.provider.AuthenticationHelper;

@Path("/")
public class MainController extends AbstractController {
	
	private static final String SECURE_ID_ERROR_MESSAGE = "Incorrect Secure Id";
	public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "ERROR_MESSAGE";
	
	@GET
	@Authenticated
	public Viewable index() {
		return viewable(View.DROP);
	}
	
	@GET
	@Path("auth")
    public Response auth() {
	    Status status = Status.OK;
	    
        String errorMessage = (String) getSession().getAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE);
        if (errorMessage != null) {
            getSession().removeAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE);
            
            status = Status.FORBIDDEN;
            addMessage(errorMessage, Type.DANGER);
        }      
	    
        return Response.status(status).entity(viewable(View.AUTH)).build();
    }
	
	@POST
	@Path("authenticate")
    public Response authenticate(@FormParam("secureId") final String secureId) throws URISyntaxException {
	
		if (! AuthenticationHelper.authenticate(getHttpServletRequest(), secureId)) {
			getSession().setAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE, SECURE_ID_ERROR_MESSAGE);
		}

		return Response.seeOther(new URI("/")).build();
    }
}
