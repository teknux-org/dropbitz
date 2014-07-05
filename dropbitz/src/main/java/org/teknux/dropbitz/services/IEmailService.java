package org.teknux.dropbitz.services;

/**
 * Service to send emails.
 */
public interface IEmailService extends
		IService {

	void sendEmail(String subject, String viewName);

	void sendEmail(String subject, String viewName, Object model);

	void sendEmail(String subject, String viewName, Object model, String viewNameAlt);
}
