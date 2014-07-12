package org.teknux.dropbitz.service.email;

import java.text.MessageFormat;
import java.util.Objects;

import org.apache.commons.mail.DefaultAuthenticator;
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
        logger.debug("Send email...");
        
        HtmlEmail email = new HtmlEmail();
        
        //Global Configuration
        email.setHostName(config.getEmailHost());
        email.setSmtpPort(config.getEmailPort());
        if ((config.getEmailUsername() != null && !config.getEmailUsername().isEmpty()) || (config.getEmailPassword() != null && !config.getEmailPassword().isEmpty())) {
            email.setAuthenticator(new DefaultAuthenticator(config.getEmailUsername(), config.getEmailPassword()));
        }
        email.setSSLOnConnect(config.isSsl());
        
        email.setSubject(dropbitzEmail.getSubject());
        try {
            email.setFrom(dropbitzEmail.getEmailFrom());
            email.addTo(dropbitzEmail.getEmailTo());
            email.setHtmlMsg(dropbitzEmail.getHtmlMsg());
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
