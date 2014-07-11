package org.teknux.dropbitz.service.email;

import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.service.IService;


/**
 * Service sending emails
 */
public interface IEmailService extends
		IService {

	/**
	 * Sends an HTML email providing a template name, without model for the template.
	 * 
	 * @param subject
	 *            Email Subject
	 * @param viewName
	 *            Freemarker Template Name
	 */
	public void sendEmail(String subject, String viewName);

	/**
	 * Sends an HTML email providing a template name and the model for the template.
	 * 
	 * @param subject
	 *            Email Subject
	 * @param viewName
	 *            Freemarker Template Name
	 * @param model
	 *            Object model for Freemarker template
	 */
	public void sendEmail(String subject, String viewName, IModel model);

	/**
	 * Sends an HTML email providing a template name, the model for the template and an alternative fallback non-HTML
	 * template.
	 * 
	 * @param subject
	 *            Email Subject
	 * @param viewName
	 *            Freemarker Template Name
	 * @param model
	 *            Object model for Freemarker template
	 * @param viewNameAlt
	 *            Alternative Freemarker Template Name (Non-HTML)
	 */
	public void sendEmail(String subject, String viewName, IModel model, String viewNameAlt);

}