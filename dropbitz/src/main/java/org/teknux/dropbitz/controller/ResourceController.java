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

package org.teknux.dropbitz.controller;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.contant.Route;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.util.PathUtil;

@Path(Route.RESOURCE)
public class ResourceController extends AbstractController {

    private final static String MEDIA_TYPE_IMAGE = "image/*";
    
    private final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @GET
    @Path("{resource}")
    @Produces(MEDIA_TYPE_IMAGE)
    public Response index(@PathParam("resource") String resource) {

        if (! isAuthorizedResource(resource)) {
            logger.warn("Resource [{}] unauthorized", resource);
            return Response.status(Status.UNAUTHORIZED).build();
        }
        
        File file = new File(PathUtil.getJarDir() + File.separator + resource);

        if (!file.exists()) {
            logger.warn("Resource [{}] don't exists", resource);
            return Response.status(Status.NOT_FOUND).build();
        }

        String contentType = new MimetypesFileTypeMap().getContentType(file);
        return Response.ok(file, contentType).build();
    }

    private boolean isAuthorizedResource(String resource) {
        String logo = getServiceManager().getService(IConfigurationService.class).getConfiguration().getHeaderLogo();
        if (logo != null && resource != null && resource.equals(logo)) {
            return true;
        }
        
        return false;
    }
}
