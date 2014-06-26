package org.teknux.dropbitz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.Application;
import org.teknux.dropbitz.model.FallbackModel;
import org.teknux.dropbitz.provider.Authenticated;

import static org.teknux.dropbitz.Application.getConfigurationFile;

@Path("/upload")
public class UploadController {

	private final Logger logger = LoggerFactory.getLogger(Application.class);
	
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CHARSET_ISO_8859_1 = "iso-8859-1";
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
	
	private static String ERROR_MESSAGE_FILE_MISSING = "File missing";
	private static String ERROR_MESSAGE_FILE_IOEXCEPTION = "Can't get file";
	
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
	@Authenticated
    public Response uploadFile(
            @FormDataParam("file") final InputStream inputStream,
            @FormDataParam("file") final FormDataContentDisposition formDataContentDisposition,
            @FormDataParam("name") final String name,
            @FormDataParam("fallback") final Boolean fallback) {  	
    	
    	String fileName = null;
		try {
			fileName = new String(formDataContentDisposition.getFileName().getBytes(CHARSET_ISO_8859_1), CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			logger.warn("Can not translate file name charset", e);
			fileName = formDataContentDisposition.getFileName();
		};
		
        try { 	
    		if (formDataContentDisposition.getFileName().isEmpty()) {
    			return getResponse(fallback, Status.BAD_REQUEST, fileName, ERROR_MESSAGE_FILE_MISSING);
    		}
    	
    		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    		String destFileName = date + (name.isEmpty()?"":"-" + name) + "-" + fileName;
        	java.nio.file.Path outputPath = FileSystems.getDefault().getPath(getConfigurationFile().getDirectory().getAbsolutePath(), destFileName);
            Files.copy(inputStream, outputPath);
        } catch (IOException e) {
        	logger.error(ERROR_MESSAGE_FILE_IOEXCEPTION, e);
        
        	return getResponse(fallback, Status.INTERNAL_SERVER_ERROR, fileName, ERROR_MESSAGE_FILE_IOEXCEPTION);
        }

        return getResponse(fallback, Status.OK, fileName, null);
    }
	
	private Response getResponse(Boolean fallback, Status status, String fileName, String errorMessage) {
		if (fallback != null && fallback) {
			FallbackModel fallbackModel = new FallbackModel();
			fallbackModel.setFileName(fileName);
			fallbackModel.setErrorMessage(errorMessage);
			
			return Response.status(status.getStatusCode()).entity(new Viewable("fallback", fallbackModel)).build();	
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", errorMessage);
		    
		    return Response.status(status.getStatusCode()).entity(map).build();	
		}
	}
}
