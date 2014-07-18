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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.contant.Route;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.util.Md5Util;
import org.teknux.dropbitz.util.PathUtil;

@Path(Route.RESOURCE)
public class ResourceController extends AbstractController {

    private final static String MEDIA_TYPE_IMAGE = "image/*";
    
    private final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @GET
    @Path("{resource}")
    @Produces(MEDIA_TYPE_IMAGE)
    public Response index(@Context Request request, @PathParam("resource") String resource) throws NoSuchAlgorithmException {

        if (! isAuthorizedResource(resource)) {
            logger.warn("Resource [{}] unauthorized", resource);
            return Response.status(Status.UNAUTHORIZED).build();
        }
        
        File file = new File(PathUtil.getJarDir() + File.separator + resource);

        if (!file.exists()) {
            logger.warn("Resource [{}] don't exists", resource);
            return Response.status(Status.NOT_FOUND).build();
        }
        
        //ETag is md5 sum of file path
        final EntityTag entityTag = new EntityTag(Md5Util.hash(resource));

        //Cache is based on modification date + ETag
        ResponseBuilder responseBuilder = request.evaluatePreconditions(new Date(file.lastModified()), entityTag);
        if (responseBuilder == null) { //If missing or different, send file
            String contentType = new MimetypesFileTypeMap().getContentType(file);
            return Response.ok(file, contentType).lastModified(new Date(file.lastModified())).tag(entityTag).build();
        } else { //If the same, send "Not Modified" status (HTTP 304)
            final CacheControl cacheControl = new CacheControl();
            cacheControl.setMaxAge(-1);
            return responseBuilder.cacheControl(cacheControl).lastModified(new Date(file.lastModified())).tag(entityTag).build();
        }
    }

    private boolean isAuthorizedResource(String resource) {
        if (resource == null || resource.isEmpty()) {
            return false;
        }
        
        for (String authorizedResource : getAuthorizedResource()) {
            if (resource.equals(authorizedResource)) {
                return true;
            }
        }
        
        return false;
    }

    private List<String> getAuthorizedResource() {
        List<String> authorizedResources = new ArrayList<String>();
        
        String icon = getServiceManager().getService(IConfigurationService.class).getConfiguration().getIcon();
        if (icon != null && ! icon.isEmpty()) {
            authorizedResources.add(icon);
        }
        
        String logo = getServiceManager().getService(IConfigurationService.class).getConfiguration().getHeaderLogo();
        if (logo != null && ! logo.isEmpty()) {
            authorizedResources.add(logo);
        }
        
        return authorizedResources;
    }
}
