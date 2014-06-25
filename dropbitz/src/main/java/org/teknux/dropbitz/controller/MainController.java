package org.teknux.dropbitz.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.glassfish.jersey.server.mvc.Viewable;
import org.teknux.dropbitz.provider.Authenticated;

@Path("/")
public class MainController {
	
	@GET
	public Viewable index() {
		return new Viewable("index");
	}

    @GET
    @Path("/secured")
    @Authenticated
    public Viewable secured() {
        return new Viewable("index");
    }
}
