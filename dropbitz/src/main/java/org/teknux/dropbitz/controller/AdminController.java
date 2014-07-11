package org.teknux.dropbitz.controller;

import org.glassfish.jersey.server.mvc.Viewable;
import org.teknux.dropbitz.contant.Route;
import org.teknux.dropbitz.freemarker.View;
import org.teknux.dropbitz.provider.Authenticated;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 */
@Path(Route.ADMIN)
public class AdminController extends AbstractController {

    @GET
    @Authenticated
    public Viewable index() {
        return viewable(View.ADMIN);
    }
}
