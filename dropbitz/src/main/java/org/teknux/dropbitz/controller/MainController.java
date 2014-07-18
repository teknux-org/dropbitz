/*
 * Copyright (C) 2014 TekNux.org
 *
 * This file is part of the dropbitz Community GPL Source Code.
 *
 * dropbitz Community Source Code is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dropbitz Community Source Code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dropbitz Community Source Code.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        
        //If authorized, redirect to index page, else redirect to auth page
        if (getAuth().isAuthorized()) {
            return Response.seeOther(uri(Route.INDEX)).build();
        } else {
            return Response.seeOther(uri(Route.AUTH)).build();
        }
    }
	
	@GET
	@Path(Route.AUTH)
    public Response auth() throws URISyntaxException {
        //If authorized, we don't need to login
        if (getAuth().isAuthorized()) {
	        return Response.seeOther(uri(Route.INDEX)).build();
	    }
	    
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
			return Response.seeOther(uri(Route.AUTH)).build();
		}

		return Response.seeOther(uri(Route.INDEX)).build();
    }
}
