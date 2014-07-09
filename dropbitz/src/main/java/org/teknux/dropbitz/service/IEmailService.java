package org.teknux.dropbitz.service;

import org.teknux.dropbitz.model.view.IModel;

/**
 * Service to send emails.
 */
public interface IEmailService extends
		IService {

	void sendEmail(String subject, String viewName);

    void sendEmail(String subject, String viewName, IModel model);

    void sendEmail(String subject, String viewName, IModel model, String viewNameAlt);
}
