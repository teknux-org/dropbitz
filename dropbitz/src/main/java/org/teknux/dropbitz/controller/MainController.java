package org.teknux.dropbitz.controller;

import java.net.URISyntaxException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.teknux.dropbitz.contant.I18nKey;
import org.teknux.dropbitz.contant.Route;
import org.teknux.dropbitz.freemarker.View;
import org.teknux.dropbitz.model.Message.Type;
import org.teknux.dropbitz.provider.Authenticated;
import org.teknux.dropbitz.provider.AuthenticationHelper;

@Path(Route.INDEX)
public class MainController extends AbstractController {
	
	public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "ERROR_MESSAGE";
	private AuthenticationHelper authenticationHelper;

    public MainController() {
        authenticationHelper = new AuthenticationHelper();
    }

	@GET
	@Authenticated
	public Response index() throws URISyntaxException {
		return Response.seeOther(uri(Route.DROP)).build();
	}

    @GET
    @Path(Route.LOGOUT)
    public Response logout() throws URISyntaxException {
        authenticationHelper.logout(getHttpServletRequest());
        return Response.seeOther(uri(Route.AUTH)).build();
    }
	
	@GET
	@Path(Route.AUTH)
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
	@Path(Route.AUTH)
    public Response authenticate(@FormParam("secureId") final String secureId) throws URISyntaxException {
	
		if (! authenticationHelper.authenticate(getHttpServletRequest(), secureId)) {
			getSession().setAttribute(SESSION_ATTRIBUTE_ERROR_MESSAGE, i18n(I18nKey.AUTH_SECUREID_ERROR));
		}

		return Response.seeOther(uri(Route.INDEX)).build();
    }
}
