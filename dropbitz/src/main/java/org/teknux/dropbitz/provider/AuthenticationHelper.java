package org.teknux.dropbitz.provider;

import org.teknux.dropbitz.Application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Helper class to authenticate "user" on server side and help verify is a request is authenticated.
 */
public class AuthenticationHelper {

    public static final String SESSION_ATTRIBUTE_IS_SECURED = "IS_SECURED";

    public static boolean isSecured(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Boolean isSecured = (Boolean) session.getAttribute(SESSION_ATTRIBUTE_IS_SECURED);
        return isSecured != null && isSecured;
    }

    public static boolean authenticate(HttpServletRequest request, String secureId) {
        final boolean isAuthorized = Application.getConfigurationFile().getSecureId().equals(secureId);
        request.getSession().setAttribute(SESSION_ATTRIBUTE_IS_SECURED, isAuthorized ? Boolean.TRUE : Boolean.FALSE);
        return isAuthorized;
    }
}
