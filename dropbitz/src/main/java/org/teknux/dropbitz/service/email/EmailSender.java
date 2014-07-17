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

import java.text.MessageFormat;
import java.util.Objects;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.model.DropbitzEmail;

public class EmailSender implements IEmailSender {
    
    private final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final Configuration config;

    public EmailSender(final Configuration configuration) {
        this.config = Objects.requireNonNull(configuration, "Email Sender require configuration");
    }
    
    public void sendEmail(DropbitzEmail dropbitzEmail) throws EmailServiceException {
        sendEmail(dropbitzEmail, new HtmlEmail());
    }
    
    public void sendEmail(DropbitzEmail dropbitzEmail, HtmlEmail email) throws EmailServiceException {
        logger.debug("Send email...");
        
        if (dropbitzEmail == null) {
            throw new EmailServiceException("DropbitzEmail can not be null");
        }
        if (email == null) {
            throw new EmailServiceException("HtmlEmail can not be null");
        }
        
        //Global Configuration
        email.setHostName(Objects.requireNonNull(config.getEmailHost(), "Email Host is required"));
        email.setSmtpPort(config.getEmailPort());
        if ((config.getEmailUsername() != null && !config.getEmailUsername().isEmpty()) || (config.getEmailPassword() != null && !config.getEmailPassword().isEmpty())) {
            email.setAuthentication(config.getEmailUsername(), config.getEmailPassword());
        }
        email.setSSLOnConnect(config.isEmailSsl());
        
        email.setSubject(dropbitzEmail.getSubject());
        try {
            email.setFrom(Objects.requireNonNull(dropbitzEmail.getEmailFrom(), "Email From is required"));
            if (dropbitzEmail.getEmailTo() == null || dropbitzEmail.getEmailTo().length == 0) {
                throw new EmailServiceException("Email To is required");
            }
            email.addTo(dropbitzEmail.getEmailTo());
            email.setHtmlMsg(Objects.requireNonNull(dropbitzEmail.getHtmlMsg(), "HtmlMsg is required"));
            if (dropbitzEmail.getTextMsg() != null) {
                email.setTextMsg(dropbitzEmail.getTextMsg());
            }
            email.send();
            
            logger.trace(MessageFormat.format("Email sent from [{0}] to [{1}]", dropbitzEmail.getEmailFrom(), String.join(",", dropbitzEmail.getEmailTo())));
        } catch (EmailException e) {
            throw new EmailServiceException("Email not sent", e);
        }
    }
}
