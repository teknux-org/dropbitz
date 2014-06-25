package org.teknux.dropbitz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.Application;
import org.teknux.dropbitz.provider.Authenticated;

@Path("/upload")
public class UploadController {

	private final Logger logger = LoggerFactory.getLogger(Application.class);
	
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
	
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
	@Authenticated
    public Response uploadFile(
            @FormDataParam("file") final InputStream inputStream,
            @FormDataParam("file") final FormDataContentDisposition formDataContentDisposition,
            @FormDataParam("name") final String name,
            @FormDataParam("fallback") final Boolean fallback) {
			
		if (name.isEmpty()) {
			if (fallback != null) {
				return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Name missing<br /><a href=\"/drop\">Retry</a>").build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("{\"error\":\"Name missing\"}").build();
			}
		}
		
    	String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    	String fileName = name + "-" + date + "-" + formDataContentDisposition.getFileName();
    	
        try { 	
    		if (formDataContentDisposition.getFileName().isEmpty()) {
    			if (fallback != null) {
    				return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("File missing<br /><a href=\"/drop\">Retry</a>").build();
    			} else {
    				return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("{\"error\":\"File missing\"}").build();
    			}
    		}
        	
        	java.nio.file.Path outputPath = FileSystems.getDefault().getPath(Application.getConfigurationFile().getDirectory().getAbsolutePath(), fileName);
            Files.copy(inputStream, outputPath);
        } catch (IOException e) {
        	logger.error("Can't get file", e);
        	
            if (fallback != null) {
            	return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity("Can't get file").build();
            } else {
            	return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity("{\"error\":\"Can't get file\"}").build();
            }
        }

        if (fallback != null) {
        	return Response.status(Response.Status.OK.getStatusCode()).entity("File uploaded !<br /><a href=\"/drop\">Other file ?</a>").build();
        } else {
        	return Response.status(Response.Status.OK.getStatusCode()).build();
        }
    }
}
