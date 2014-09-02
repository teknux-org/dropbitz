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

package org.teknux.dropbitz.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.contant.Route;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;


/**
 * Custom authentication filter used to control decorated Jax-RS resource with @Authenticated annotation.
 * It checks whether or not the request is allowed.
 */
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

	private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	public static final String FORBIDDEN_ERROR_MESSAGE = "You don't have authorisation. Thank you to authenticate";
	public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "ERROR_MESSAGE";

	@Inject
	private ServletContext servletContext;

	@Context
	private HttpServletRequest httpServletRequest;

	@Context
	private HttpServletResponse httpServletResponse;

	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		final String requestPath = "/" + containerRequestContext.getUriInfo().getPath();
		logger.trace("Request on URI [{}]...", requestPath);

		if (requestPath.startsWith(Route.AUTH)) {
			return; // already on auth page no more work to do here
		}

		// check if authenticated
		if (!AuthenticationHelper.isAuthenticated(httpServletRequest)) {
			logger.debug("User is not authenticated, redirecting to authentication page...");

			// store the URL the user is coming from
			AuthenticationHelper.setRefererUrl(httpServletRequest, requestPath);
			logger.trace("Storing referer URL {}", requestPath);

			// when page is different than root page, set error message
			httpServletRequest.getSession().setAttribute(AuthenticationFilter.SESSION_ATTRIBUTE_ERROR_MESSAGE, FORBIDDEN_ERROR_MESSAGE);
			httpServletResponse.sendRedirect(servletContext.getContextPath() + Route.AUTH);// redirect to code authentication page
		}
	}
}
