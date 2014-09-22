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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

import org.apache.commons.validator.routines.EmailValidator;
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
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.email.IEmailService;
import org.teknux.dropbitz.util.FileUtil;
import org.teknux.dropbitz.util.I18nUtil;

@Path(Route.UPLOAD)
public class UploadController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	private final static String DROPZONE_ERROR_ATTRIBUTE = "error";
	
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CHARSET_ISO_8859_1 = "iso-8859-1";
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
		
    @GET
    @Authenticated
    public Viewable index() {
        return viewable(View.UPLOAD);
    }
	
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
	@Authenticated
    public Response uploadFile(
            @FormDataParam("file") final InputStream inputStream,
            @FormDataParam("file") final FormDataContentDisposition formDataContentDisposition,
            @FormDataParam("name") final String name,
            @FormDataParam("email") final String email,
            @FormDataParam("fallback") final Boolean fallback) {

        String fileName = null;
		try {
			fileName = new String(formDataContentDisposition.getFileName().getBytes(CHARSET_ISO_8859_1), CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			logger.warn("Can not translate file name charset", e);
			fileName = formDataContentDisposition.getFileName();
		};

        logger.trace("Checking email");
        if (! email.isEmpty()) {
            //validate email
            if (!EmailValidator.getInstance().isValid(email)) {
                return getResponse(fallback, Status.INTERNAL_SERVER_ERROR, fileName, i18n(I18nKey.DROP_FILE_EMAIL_ERROR));
            }
        }
		
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
		String destFileName = date + (name.isEmpty()?"":"-" + name) + "-" + fileName;
		
        try { 	
    		if (formDataContentDisposition.getFileName().isEmpty()) {
    			return getResponse(fallback, Status.BAD_REQUEST, fileName, i18n(I18nKey.DROP_FILE_MISSING));
    		}   	
    		Configuration config = getServiceManager().getService(IConfigurationService.class).getConfiguration();
        	java.nio.file.Path outputPath = FileSystems.getDefault().getPath(config.getDirectory().getAbsolutePath(), destFileName);
        	
        	//Checks destination directory
        	if (!FileUtil.isChildOfDirectory(outputPath.toFile(), config.getDirectory())) {
        	    return getResponse(fallback, Status.FORBIDDEN, fileName, i18n(I18nKey.DROP_FILE_MISSING));
        	}
        	
            Files.copy(inputStream, outputPath);

            long fileSize = outputPath.toFile().length();
            sendEmail(true, name, fileName, destFileName, fileSize, email);
        } catch (IOException e) {
        	logger.error("Can not copy file", e);

            sendEmail(false, name, fileName, null, 0L, email);
        	return getResponse(fallback, Status.INTERNAL_SERVER_ERROR, fileName, i18n(I18nKey.DROP_FILE_ERROR));
        }
        
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
			Map<String, String> map = new HashMap<>();
			if (errorMessage != null) {
				map.put(DROPZONE_ERROR_ATTRIBUTE, errorMessage);
			}
		    
		    return Response.status(status.getStatusCode()).entity(map).build();	
		}
	}

    private void sendEmail(boolean success, String name, String fileName, String finalFileName, long fileSize, String email) {
        DropEmailModel dropEmailModel = new DropEmailModel(fileName, finalFileName, fileSize, success);

        dropEmailModel.setName(name);
        if (email != null && !email.isEmpty()) {
            dropEmailModel.setEmail(email);
        }

        sendEmailToManager(success, name, dropEmailModel);
        if (email != null && !email.isEmpty()) {
            sendEmailToUploader(success, name, dropEmailModel, email);
        }
    }

    private void sendEmailToManager(boolean success, String name, DropEmailModel dropEmailModel) {
	    IEmailService emailService = getServiceManager().getService(IEmailService.class);
	    
	    Configuration config = getServiceManager().getService(IConfigurationService.class).getConfiguration();
        Locale locale = null;
        if (config.getEmailLang() != null) {
            try {
                locale = I18nUtil.getLocaleFromString(config.getEmailLang());
            } catch (I18nServiceException e) {
                logger.warn("Bad email lang configuration property : [{}]", config.getEmailLang(), e);
            }
        }
        if (locale == null) {
            locale = getHttpServletRequest().getLocale();
        }
        
        dropEmailModel.setName(name.isEmpty()?i18n(I18nKey.DROP_EMAIL_NAME_UNKNOWN, locale):name);
        dropEmailModel.setLocale(locale);
        emailService.sendEmail(i18n(success?I18nKey.DROP_EMAIL_SUBJECT_OK:I18nKey.DROP_EMAIL_SUBJECT_ERROR, locale), "/drop", dropEmailModel, "/dropalt");
	}
	
	private void sendEmailToUploader(boolean success, String name, DropEmailModel dropEmailModel, String email) {
	    IEmailService emailService = getServiceManager().getService(IEmailService.class);
	    
	    Locale locale = getHttpServletRequest().getLocale();
	    
        dropEmailModel.setName(name.isEmpty()?i18n(I18nKey.DROP_EMAIL_NAME_UNKNOWN, locale):name);
        dropEmailModel.setLocale(locale);
        
        emailService.sendEmail(i18n(success?I18nKey.DROP_EMAIL_SUBJECT_OK:I18nKey.DROP_EMAIL_SUBJECT_ERROR, locale), "/drop", dropEmailModel, "/dropalt", Arrays.asList(email));
    }
}
