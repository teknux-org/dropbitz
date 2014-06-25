package org.teknux.dropbitz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.Application;
import org.teknux.dropbitz.provider.Authenticated;

import static org.teknux.dropbitz.Application.getConfigurationFile;

@Path("/upload")
public class UploadController {

	private final Logger logger = LoggerFactory.getLogger(Application.class);
	
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
	private static String HTML_NEWLINE = "<br />";
	private static String HTML_BACKLINK = "<a href=\"/drop\">Back</a>";
	
	private static String ERROR_MESSAGE_FILE_MISSING = "File missing";
	private static String ERROR_MESSAGE_FILE_IOEXCEPTION = "Can't get file";
	private static String SUCCESS_MESSAGE_FILE_UPLOADED = "File uploaded";
	
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
	@Authenticated
    public Response uploadFile(
            @FormDataParam("file") final InputStream inputStream,
            @FormDataParam("file") final FormDataContentDisposition formDataContentDisposition,
            @FormDataParam("name") final String name,
            @FormDataParam("fallback") final Boolean fallback) {
			
    	String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    	String fileName = date + (name.isEmpty()?"":"-" + name) + "-" + formDataContentDisposition.getFileName();
    	
        try { 	
    		if (formDataContentDisposition.getFileName().isEmpty()) {
    			return getResponse(fallback, ERROR_MESSAGE_FILE_MISSING, Status.BAD_REQUEST);
    		}
    		       	
        	java.nio.file.Path outputPath = FileSystems.getDefault().getPath(getConfigurationFile().getDirectory().getAbsolutePath(), fileName);
            Files.copy(inputStream, outputPath);
        } catch (IOException e) {
        	logger.error(ERROR_MESSAGE_FILE_IOEXCEPTION, e);
        
        	return getResponse(fallback, ERROR_MESSAGE_FILE_IOEXCEPTION, Status.INTERNAL_SERVER_ERROR);
        }

        return getResponse(fallback, SUCCESS_MESSAGE_FILE_UPLOADED, Status.OK);
    }
	
	private Response getResponse(Boolean fallback, String message, Status status) {
		if (fallback != null && fallback) {
			return Response.status(status.getStatusCode()).entity(message + HTML_NEWLINE + HTML_BACKLINK).build();	
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", message);
		    
		    return Response.status(status.getStatusCode()).entity(map).build();	
		}
	}
}
