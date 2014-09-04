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

import org.teknux.dropbitz.contant.I18nKey;
import org.teknux.dropbitz.contant.Route;
import org.teknux.dropbitz.freemarker.View;
import org.teknux.dropbitz.model.Message.Type;
import org.teknux.dropbitz.provider.Authenticated;
import org.teknux.dropbitz.provider.AuthenticationFilter;
import org.teknux.dropbitz.provider.AuthenticationHelper;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URISyntaxException;


@Path(Route.INDEX)
public class LoginController extends AbstractController {

	public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "ERROR_MESSAGE";
	private AuthenticationHelper authenticationHelper;

	public LoginController() {
		authenticationHelper = new AuthenticationHelper();
	}

	@GET
	@Authenticated
	public Response index() throws URISyntaxException {
		return Response.seeOther(uri(Route.UPLOAD)).build();
	}

	@GET
	@Path(Route.LOGOUT)
	public Response logout() throws URISyntaxException {
		authenticationHelper.logout(getHttpServletRequest());
		return Response.seeOther(uri(Route.INDEX)).build();
	}

	@GET
	@Path(Route.AUTH)
	public Response getAuthentication(@DefaultValue("") @QueryParam("secureId") final String secureId) throws URISyntaxException {
		// if already logged, redirect to index page
		if (AuthenticationHelper.isAuthenticated(getHttpServletRequest())) {
			return Response.seeOther(uri(Route.INDEX)).build();
		}

		// user provided an upload secure id to upload it's files
		if (secureId != null && !secureId.isEmpty()) {
			return postAuthentication(secureId);
		}

		// show the code login
		Status status = Status.OK;
		String errorMessage = (String) getSession().getAttribute(AuthenticationFilter.SESSION_ATTRIBUTE_ERROR_MESSAGE);
		if (errorMessage != null) {
			getSession().removeAttribute(AuthenticationFilter.SESSION_ATTRIBUTE_ERROR_MESSAGE);

			status = Status.FORBIDDEN;
			addMessage(errorMessage, Type.DANGER);
		}

		return Response.status(status).entity(viewable(View.AUTH)).build();
	}

	@POST
	@Path(Route.AUTH)
	public Response postAuthentication(@FormParam("secureId") final String secureId) throws URISyntaxException {
		if (!AuthenticationHelper.authenticate(getHttpServletRequest(), secureId)) {
			getSession().setAttribute(AuthenticationFilter.SESSION_ATTRIBUTE_ERROR_MESSAGE, i18n(I18nKey.AUTH_SECUREID_ERROR));
			return Response.seeOther(uri(Route.AUTH)).build();
		}

		return Response.seeOther(uri(Route.INDEX)).build();
	}
}
