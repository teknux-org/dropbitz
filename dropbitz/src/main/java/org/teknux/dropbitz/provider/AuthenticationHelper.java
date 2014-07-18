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

	public static final String SESSION_ATTRIBUTE_IS_LOGGED = "IS_LOGGED";
	
	public Auth getAuth(HttpServletRequest request) {
	    final String securityID = ServiceManager.get(request.getServletContext()).getService(IConfigurationService.class).getConfiguration().getSecureId();
	    
	    HttpSession session = request.getSession();
	    Object isLoggedSession = session.getAttribute(SESSION_ATTRIBUTE_IS_LOGGED);
	    boolean isLogged = false;
	    if (isLoggedSession != null && isLoggedSession instanceof Boolean) {
	        isLogged = (boolean) isLoggedSession;
	    }
	    boolean isAuthorized = isLogged || securityID.isEmpty();
	    
	    Auth auth = new Auth();
	    auth.setLogged(isLogged);
	    auth.setAuthorized(isAuthorized);
	    
	    return auth;
	}
	
	public boolean authenticate(HttpServletRequest request, String secureId) {
		logger.trace("Try to authenticate...");

		final String securityID = ServiceManager.get(request.getServletContext()).getService(IConfigurationService.class).getConfiguration().getSecureId();
		final boolean isLogged = Objects.equals(securityID, secureId);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_IS_LOGGED, isLogged);

		if (isLogged) {
			logger.debug("Authentication success");
		} else {
			logger.warn("Authentication failed");
		}
		return isLogged;
	}

    public void logout(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_ATTRIBUTE_IS_LOGGED);
        logger.trace("Logout success");
    }
}
