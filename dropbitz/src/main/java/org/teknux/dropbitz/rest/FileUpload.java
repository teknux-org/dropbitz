package org.teknux.dropbitz.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.mvc.Viewable;

@Path("/")
public class FileUpload {

	private static final String DESTINATION_DIRECTORY = "/home/pp/Desktop";
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
	private static final String CODE = "sma";
	
	@GET
	public Viewable index() {
		return new Viewable("index");
	}
	
	@POST
	@Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Response uploadFile(
            @FormDataParam("file") final InputStream inputStream,
            @FormDataParam("file") final FormDataContentDisposition formDataContentDisposition,
            @FormDataParam("code") final String code,
            @FormDataParam("name") final String name,
            @FormDataParam("fallback") final Boolean fallback) {
		
		if (code.isEmpty() || ! code.equals(CODE)) {
			if (fallback != null) {
				return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).entity("Code incorrect<br /><a href=\"/\">Retry</a>").build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).entity("{\"error\":\"Code incorrect\"}").build();
			}
		}
		
		if (name.isEmpty()) {
			if (fallback != null) {
				return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Name missing<br /><a href=\"/\">Retry</a>").build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("{\"error\":\"Name missing\"}").build();
			}
		}
		
    	String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    	String fileName = name + "-" + date + "-" + formDataContentDisposition.getFileName();
    	
        try { 	
    		if (formDataContentDisposition.getFileName().isEmpty()) {
    			if (fallback != null) {
    				return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("File missing<br /><a href=\"/\">Retry</a>").build();
    			} else {
    				return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("{\"error\":\"File missing\"}").build();
    			}
    		}
        	
        	java.nio.file.Path outputPath = FileSystems.getDefault().getPath(DESTINATION_DIRECTORY, fileName);
            Files.copy(inputStream, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
            if (fallback != null) {
            	return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity("Can't get file").build();
            } else {
            	return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity("{\"error\":\"Can't get file\"}").build();
            }
        }

        if (fallback != null) {
        	return Response.status(Response.Status.OK.getStatusCode()).entity("File uploaded !<br /><a href=\"/\">Other file ?</a>").build();
        } else {
        	return Response.status(Response.Status.OK.getStatusCode()).build();
        }
    }
}
