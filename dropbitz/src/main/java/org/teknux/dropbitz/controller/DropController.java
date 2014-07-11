package org.teknux.dropbitz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.contant.I18nKey;
import org.teknux.dropbitz.contant.Route;
import org.teknux.dropbitz.exception.I18nServiceException;
import org.teknux.dropbitz.freemarker.View;
import org.teknux.dropbitz.model.Message.Type;
import org.teknux.dropbitz.model.view.DropEmailModel;
import org.teknux.dropbitz.provider.Authenticated;
import org.teknux.dropbitz.service.ConfigurationService;
import org.teknux.dropbitz.service.I18nService;
import org.teknux.dropbitz.service.email.EmailService;

@Path(Route.DROP)
public class DropController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(DropController.class);
	
	private final static String DROPZONE_ERROR_ATTRIBUTE = "error";
	
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CHARSET_ISO_8859_1 = "iso-8859-1";
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
		
    @GET
    @Authenticated
    public Viewable index() {
        return viewable(View.DROP);
    }
	
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
		
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
		String destFileName = date + (name.isEmpty()?"":"-" + name) + "-" + fileName;
		
        try { 	
    		if (formDataContentDisposition.getFileName().isEmpty()) {
    			return getResponse(fallback, Status.BAD_REQUEST, fileName, i18n(I18nKey.DROP_FILE_MISSING));
    		}   	
    		Configuration config = getServiceManager().getService(ConfigurationService.class).getConfiguration();
        	java.nio.file.Path outputPath = FileSystems.getDefault().getPath(config.getDirectory().getAbsolutePath(), destFileName);
            Files.copy(inputStream, outputPath);
        } catch (IOException e) {
        	logger.error("Can not copy file", e);
        
            sendEmail(false, name, fileName, null);
        	
        	return getResponse(fallback, Status.INTERNAL_SERVER_ERROR, fileName, i18n(I18nKey.DROP_FILE_ERROR));
        }
        
        sendEmail(true, name, fileName, destFileName);
        
        return getResponse(fallback, Status.OK, fileName, null);
    }
	
	private Response getResponse(Boolean fallback, Status status, String fileName, String errorMessage) {
		if (fallback != null && fallback) {
		    if (errorMessage == null) {	        
		        addMessage(MessageFormat.format(i18n(I18nKey.DROP_FALLBACK_MESSAGE_OK), fileName), Type.SUCCESS);
		    } else {
		        addMessage(MessageFormat.format(i18n(I18nKey.DROP_FALLBACK_MESSAGE_ERROR), fileName, errorMessage), Type.DANGER);
		    }
			
			return Response.status(status.getStatusCode()).entity(viewable(View.FALLBACK)).build();	
		} else {
			Map<String, String> map = new HashMap<String, String>();
			if (errorMessage != null) {
				map.put(DROPZONE_ERROR_ATTRIBUTE, errorMessage);
			}
		    
		    return Response.status(status.getStatusCode()).entity(map).build();	
		}
	}
	
	private void sendEmail(boolean success, String name, String fileName, String finalFileName) {
	    Configuration config = getServiceManager().getService(ConfigurationService.class).getConfiguration();
	    
	    Locale locale = null;
	    if (config.getEmailLang() != null) {
            try {
                locale = I18nService.getLocaleFromString(config.getEmailLang());
            } catch (I18nServiceException e) {
                logger.warn("Bad email lang configuration property : [{}]", config.getEmailLang(), e);
            }
        }
	    if (locale == null) {
            locale = getHttpServletRequest().getLocale();
        }
	    
		DropEmailModel dropEmailModel = new DropEmailModel();
		dropEmailModel.setName(name.isEmpty()?i18n(I18nKey.DROP_EMAIL_NAME_UNKNOWN, locale):name);
		dropEmailModel.setFileName(fileName);
		dropEmailModel.setFinalFileName(finalFileName);
		dropEmailModel.setSuccess(success);
		dropEmailModel.setLocale(locale);
		
		getServiceManager().getService(EmailService.class).sendEmail(i18n(success?I18nKey.DROP_EMAIL_SUBJECT_OK:I18nKey.DROP_EMAIL_SUBJECT_ERROR, locale), "/drop", dropEmailModel, "/dropalt");
	}
}
