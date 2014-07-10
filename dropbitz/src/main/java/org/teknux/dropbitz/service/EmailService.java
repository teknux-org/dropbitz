package org.teknux.dropbitz.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.config.JerseyFreemarkerConfig;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.view.IModel;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;


public class EmailService implements
        IService {

	private final Logger logger = LoggerFactory.getLogger(EmailService.class);

	private static final String MODEL_NAME_ATTRIBUTE = "model";
	
	private static final String VIEW_EXTENSION = ".ftl";

	private static final String DEFAULT_VIEWS_PATH = "/webapp/views/email";

	private String viewsPath = DEFAULT_VIEWS_PATH;

	private JerseyFreemarkerConfig jerseyFreemarkerConfig;

	private Configuration config;
	private ServletContext servletContext;

	public EmailService() {
	}

	public void setDefaultViewsPath(String viewsPath) {
		this.viewsPath = viewsPath;
	}

	/**
	 * Send email
	 * 
	 * @param subject
	 *            Email Subject
	 * @param viewName
	 *            Freemarker Template Name
	 */
	public void sendEmail(String subject, String viewName) {
		sendEmail(subject, viewName, null);
	}

	/**
	 * Send email
	 * 
	 * @param subject
	 *            Email Subject
	 * @param viewName
	 *            Freemarker Template Name
	 * @param model
	 *            Object model for Freemarker template
	 */
	public void sendEmail(String subject, String viewName, IModel model) {
		sendEmail(subject, viewName, model, null);
	}

	/**
	 * Send email
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
	public void sendEmail(String subject, String viewName, IModel model, String viewNameAlt) {
		if (config.isEmailEnable()) {
			logger.debug("Email : Send new email...");

			try {
				HtmlEmail email = getNewEmail();

				email.setSubject(subject);
				email.setHtmlMsg(resolve(model, viewName));
				if (viewNameAlt != null) {
					email.setTextMsg(resolve(model, viewNameAlt));
				}
				email.send();
				logger.debug("Email : Success");
			} catch (EmailException | IOException | TemplateException e) {
				logger.error("Email : Can not send message", e);
			}
		} else {
			logger.debug("Email is disabled");
		}
	}

	/**
	 * Build new email from configuration file
	 * 
	 * @return HtmlEmail
	 * @throws EmailException
	 */
	private HtmlEmail getNewEmail() throws EmailException {
		logger.trace("Email : Build new email...");

		HtmlEmail email = null;

		email = new HtmlEmail();
		email.setHostName(config.getEmailHost());
		email.setSmtpPort(config.getEmailPort());
		if ((config.getEmailUsername() != null && !config.getEmailUsername().isEmpty()) || (config.getEmailPassword() != null && !config.getEmailPassword().isEmpty())) {
			email.setAuthenticator(new DefaultAuthenticator(config.getEmailUsername(), config.getEmailPassword()));
		}
		email.setSSLOnConnect(config.isSsl());
		email.setFrom(config.getEmailFrom());
		email.addTo(config.getEmailTo());

		logger.trace(MessageFormat.format("Email : From [{0}] to [{1}]", config.getEmailFrom(), String.join(",", config.getEmailTo())));

		return email;
	}

	/**
	 * Resolve template
	 * 
	 * @param model
	 *            Object model for Freemarker template
	 * @param viewName
	 *            Freemarker Template Name
	 * @return String
	 * @throws IOException
	 *             on load error
	 * @throws TemplateException
	 *             on template syntax error
	 */
	private String resolve(IModel model, String viewName) throws IOException, TemplateException {
	    
		Template template = jerseyFreemarkerConfig.getTemplate(viewsPath + viewName + VIEW_EXTENSION);
		Writer writer = new StringWriter();
		
        model.setServletContext(servletContext);
        
        Map<String,IModel> map = new HashMap<String,IModel>();
        map.put(MODEL_NAME_ATTRIBUTE, model);
        
		template.process(map, writer);

		return writer.toString();
	}

	@Override
	public void start(final ServiceManager serviceManager) throws ServiceException {
       this.config = serviceManager.getConfigurationService().getConfiguration();
	        
		if (config.isEmailEnable()) {
		    this.servletContext = serviceManager.getServletContext();
		    
			// Init Freemarker
			try {
                jerseyFreemarkerConfig = new JerseyFreemarkerConfig(servletContext);
            } catch (TemplateModelException e) {
                throw new ServiceException(e);
            }
		}
	}

	@Override
	public void stop() {

	}
}
