package org.teknux.dropbitz;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.ConfigurationFile;

public class Mail {
	private final Logger logger = LoggerFactory.getLogger(Mail.class);
	
	private Email email = null;

	public Mail() {
		ConfigurationFile config = Application.getConfigurationFile();
			
		if (config.isEmailEnable()) {
			logger.debug("Email : Send email...");
			
			email = new SimpleEmail();
			email.setHostName(config.getEmailHost());
			email.setSmtpPort(config.getEmailPort());
			if (config.getEmailUsername() != null || config.getEmailPassword() != null) {
				email.setAuthenticator(new DefaultAuthenticator(config.getEmailUsername(), config.getEmailPassword()));
			}
			email.setSSLOnConnect(config.isSsl());
			try {
				email.setFrom(config.getEmailFrom());
				email.addTo(config.getEmailTo());
			} catch (EmailException e) {
				logger.error("Email : Bad 'from' or 'to' property", e);
			}
		} else {
			logger.debug("Email is disabled");
		}
	}
	
	public void sendEmail(String subject, String message) {
		if (email != null) {
			email.setSubject(subject);
			try {
				email.setMsg(message);
				email.send();
			} catch (EmailException e) {
				logger.error("Email : Can not send message", e);
			}
		}
	}
}
