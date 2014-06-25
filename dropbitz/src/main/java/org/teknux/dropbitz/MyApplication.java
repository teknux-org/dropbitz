package org.teknux.dropbitz;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.teknux.dropbitz.provider.AuthenticationFilter;

public class MyApplication extends ResourceConfig {

    public MyApplication() {
        super(MultiPartFeature.class);
        register(AuthenticationFilter.class);
    }
}
