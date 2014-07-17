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

package org.teknux.dropbitz.service.email;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.DropbitzEmail;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.IServiceManager;

/**
 * Service queuing emails and sending them asynchronously.
 */
public class EmailService implements IEmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final int TIMEOUT_ON_STOP = 1; // minutes

    private BlockingQueue<DropbitzEmail> emailQueue;

    private IEmailTemplateResolver emailTemplateResolver;

    private Configuration configuration;

    private ExecutorService emailExecutorService;

    public EmailService() {
    }

    @Override
    public void sendEmail(String subject, String viewName) {
        sendEmail(subject, viewName, (String[])null);
    }
    
    @Override
    public void sendEmail(String subject, String viewName, String[] emailTo) {
        sendEmail(subject, viewName, new Model(), emailTo);
    }

    @Override
    public void sendEmail(String subject, String viewName, IModel model) {
        sendEmail(subject, viewName, model, (String)null);
    }
    
    @Override
    public void sendEmail(String subject, String viewName, IModel model, String[] emailTo) {
        sendEmail(subject, viewName, model, null, emailTo);
    }
    
    @Override
    public void sendEmail(String subject, String viewName, IModel model, String viewNameAlt) {
        sendEmail(subject, viewName, model, viewNameAlt, null);
    }

    @Override
    public void sendEmail(String subject, String viewName, IModel model, String viewNameAlt, String[] emailTo) {
        if (configuration.isEmailEnable()) {
            logger.debug("Email : Add new email to queue...");

            DropbitzEmail dropbitzEmail = new DropbitzEmail();
            dropbitzEmail.setSubject(subject);
            try {
                dropbitzEmail.setEmailFrom(configuration.getEmailFrom());
                dropbitzEmail.setEmailTo(emailTo == null?configuration.getEmailTo():emailTo);
                dropbitzEmail.setHtmlMsg(emailTemplateResolver.resolve(viewName, model));
                if (viewNameAlt != null) {
                    dropbitzEmail.setTextMsg(emailTemplateResolver.resolve(viewNameAlt, model));
                }

                emailQueue.offer(dropbitzEmail);

                logger.trace("Email added to queue");
            } catch (EmailServiceException e) {
                logger.error("Can not add email to queue", e);
            }
        } else {
            logger.debug("Email is disabled. Email not added to queue");
        }
    }

    public void start(final Configuration configuration, IEmailTemplateResolver emailTemplateResolver, IEmailSender emailSender) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration can not be null");
        
        if (configuration.isEmailEnable()) {
            this.emailTemplateResolver = Objects.requireNonNull(emailTemplateResolver, "EmailTemplateResolver can not be null");

            emailQueue = new LinkedBlockingQueue<DropbitzEmail>();

            // Start EmailRunnable
            emailExecutorService = Executors.newSingleThreadExecutor();
            emailExecutorService.execute(new EmailRunnable(emailQueue, Objects.requireNonNull(emailSender, "EmailSender can not be null")));
        }
    }

    @Override
    public void start(final IServiceManager serviceManager) throws ServiceException {
        Configuration configuration = serviceManager.getService(IConfigurationService.class).getConfiguration();
        
        EmailTemplateResolver emailTemplateResolver = new EmailTemplateResolver(serviceManager.getServletContext());
        EmailSender emailSender = new EmailSender(configuration);

        start(configuration, emailTemplateResolver, emailSender);
    }

    @Override
    public void stop() throws ServiceException {
        // Stop EmailRunnable
        if (emailExecutorService != null) {
            emailExecutorService.shutdownNow();

            try {
                emailExecutorService.awaitTermination(TIMEOUT_ON_STOP, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                throw new ServiceException(e);
            }
        }
    }
}
