package org.teknux.dropbitz.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.teknux.dropbitz.provider.AuthenticationFilter;

public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {
        super(MultiPartFeature.class, AuthenticationFilter.class, JacksonFeature.class);
    }
}
