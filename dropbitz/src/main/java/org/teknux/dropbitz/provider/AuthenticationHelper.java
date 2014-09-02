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

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.model.Auth;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.ServiceManager;

/**
 * Helper class to authenticate "user" on server side and help verify is a request is authenticated.
 */
public class AuthenticationHelper {

	private static Logger logger = LoggerFactory.getLogger(AuthenticationHelper.class);

	private static final String SESSION_ATTRIBUTE_PREFIX = "DropBitz-";
	public static final String SESSION_ATTRIBUTE_USER = SESSION_ATTRIBUTE_PREFIX + "USER";
	public static final String SESSION_ATTRIBUTE_REFERER = SESSION_ATTRIBUTE_PREFIX + "REFURL";

	public static final String getUserFromSession(final HttpServletRequest request) {
		return (String) request.getSession().getAttribute(SESSION_ATTRIBUTE_USER);
	}

	public static Auth getAuth(final HttpServletRequest request) {
		return new Auth(getUserFromSession(request));
	}

	public static boolean isAuthenticated(final HttpServletRequest request) {
		String user = getUserFromSession(request);
		if (user == null) {
			return false;
		}

		return true;
	}

	public static boolean authenticate(final HttpServletRequest request, final String secureId) {
		Objects.requireNonNull(request);
		Objects.requireNonNull(secureId);

		final String storedSecureId = ServiceManager.get(request.getServletContext()).getService(IConfigurationService.class).getConfiguration().getSecureId();
		if (!Objects.equals(storedSecureId, secureId)) {
			logger.warn("Authentication failed for code {}", secureId);
			return false;
		}

		request.getSession().setAttribute(SESSION_ATTRIBUTE_USER, secureId);
		logger.debug("Authentication by code successful for {}", secureId);

		return true;
	}

    public static void logout(final HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_ATTRIBUTE_USER);
        logger.trace("Logout success");
    }

	/**
	 * Store a the original URL the user came from in the session.
	 *
	 * @param request
	 *            the http request
	 * @param url
	 *            the url to store in the session
	 */
	public static void setRefererUrl(final HttpServletRequest request, final String url) {
		Objects.requireNonNull(request).getSession().setAttribute(SESSION_ATTRIBUTE_REFERER, url);
	}

	/**
	 * Get the original URL the user came from. Calling this method will remove the previously stored URL from the
	 * session.
	 *
	 * @param request
	 *            the http request
	 * @return <code>null</code> if no url were stored, or the URL path.
	 */
	public static String getRefererUrl(final HttpServletRequest request) {
		final HttpSession session = Objects.requireNonNull(request).getSession();
		final String referer = (String) session.getAttribute(SESSION_ATTRIBUTE_REFERER);
		session.removeAttribute(SESSION_ATTRIBUTE_REFERER);
		return referer;
	}
}
