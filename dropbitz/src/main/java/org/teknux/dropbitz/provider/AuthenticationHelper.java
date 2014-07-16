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
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.ServiceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;


/**
 * Helper class to authenticate "user" on server side and help verify is a request is authenticated.
 */
public class AuthenticationHelper {

	private static Logger logger = LoggerFactory.getLogger(AuthenticationHelper.class);

	public static final String SESSION_ATTRIBUTE_IS_SECURED = "IS_SECURED";

	public boolean isAuthorized(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean isSecured = (Boolean) session.getAttribute(SESSION_ATTRIBUTE_IS_SECURED);
		final String securityID = ServiceManager.get(request.getServletContext()).getService(IConfigurationService.class).getConfiguration().getSecureId();
		return (isSecured != null && isSecured) || securityID.isEmpty();
	}

    public boolean isLogged(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Boolean isSecured = (Boolean) session.getAttribute(SESSION_ATTRIBUTE_IS_SECURED);
        return (isSecured != null && isSecured);
    }

	public boolean authenticate(HttpServletRequest request, String secureId) {
		logger.trace("Try to authenticate...");

		final String securityID = ServiceManager.get(request.getServletContext()).getService(IConfigurationService.class).getConfiguration().getSecureId();
		final boolean isAuthorized = Objects.equals(securityID, secureId);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_IS_SECURED, isAuthorized);

		if (isAuthorized) {
			logger.debug("Authentication success");
		} else {
			logger.warn("Authentication failed");
		}
		return isAuthorized;
	}

    public void logout(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_ATTRIBUTE_IS_SECURED);
        logger.trace("Logout success");
    }
}
