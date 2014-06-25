package org.teknux.dropbitz.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Custom authentication filter used to control decorated Jax-RS resource with @Authenticated annotation.
 * It checks whether or not the request is allowed.
 */
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

    public static final String SESSION_ATTRIBUTE_SECURE_ID = "DB_SECURE_ID";

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        logger.info("Filter Request");

        if (request != null) {
            HttpSession session = request.getSession();
            String secureId = (String) session.getAttribute(SESSION_ATTRIBUTE_SECURE_ID);
            if (secureId == null || secureId.isEmpty()) {
                //TODO: implements secure id comparaison
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }

        }
    }
}
