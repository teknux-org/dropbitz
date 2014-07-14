package org.teknux.dropbitz.util;

import java.util.Objects;

import javax.servlet.ServletContext;

public class UrlUtil {
    private static final String URL_SEPARATOR = "/";

    public static String getAbsoluteUrl(ServletContext servletContext, String path) {
        String contextPath = Objects.requireNonNull(servletContext, "ServletContext can not be null").getContextPath();

        if (contextPath.isEmpty()) {
            if ((path == null || path.isEmpty() || path.equals(URL_SEPARATOR))) {
                contextPath = URL_SEPARATOR;
            }
        } else if (contextPath.equals(URL_SEPARATOR)) {
            if (path != null && !path.isEmpty() && !path.equals(URL_SEPARATOR)) {
                contextPath = "";
            }
        } else {
            // ContextPath should starts with /
            if (!contextPath.startsWith(URL_SEPARATOR)) {
                contextPath = URL_SEPARATOR + contextPath;
            }
            // ContextPath Should not ends with /
            if (contextPath.endsWith(URL_SEPARATOR)) {
                contextPath = contextPath.substring(0, contextPath.length() - 1);
            }
        }

        if (path == null || path.isEmpty() || path.equals(URL_SEPARATOR)) {
            return contextPath;
        } else {
            // Path Should starts with /
            if (!path.startsWith(URL_SEPARATOR)) {
                path = URL_SEPARATOR + path;
            }
            // Path Should not ends with /
            if (path.endsWith(URL_SEPARATOR)) {
                path = path.substring(0, path.length() - 1);
            }

            return contextPath + path;
        }
    }
}
