package org.teknux.dropbitz.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Custom authentication filter used to control decorated Jax-RS resource with @Authenticated annotation.
 * It checks whether or not the request is allowed.
 */
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        logger.info("Filter Request");
        throw new WebApplicationException(Response.Status.FORBIDDEN);
    }
}
