package org.teknux.dropbitz.service.email;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.config.JerseyFreemarkerConfig;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.DropbitzEmail;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.IServiceManager;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;


/**
 * Service queuing emails and sending them asynchronously.
 */
public class EmailService implements IEmailService {

	private final Logger logger = LoggerFactory.getLogger(EmailService.class);

	private static final String DEFAULT_VIEWS_PATH = "/webapp/views/email";
	private static final int TIMEOUT_ON_STOP = 1; //minutes
	
	private static final String MODEL_NAME_ATTRIBUTE = "model";
	private static final String VIEW_EXTENSION = ".ftl";

	private String viewsPath = DEFAULT_VIEWS_PATH;
	private IEmailSender emailSender;
	
	private BlockingQueue<DropbitzEmail> emailQueue;

	private JerseyFreemarkerConfig jerseyFreemarkerConfig;

	private Configuration configuration;
	private ServletContext servletContext;

	private ExecutorService emailExecutorService;

	public EmailService() {
	}

	public void setViewsPath(String viewsPath) {
		this.viewsPath = viewsPath;
	}
	
	public void setEmailSender(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }

	@Override
	public void sendEmail(String subject, String viewName) {
		sendEmail(subject, viewName, new Model());
	}

	@Override
	public void sendEmail(String subject, String viewName, IModel model) {
		sendEmail(subject, viewName, model, null);
	}

	@Override
	public void sendEmail(String subject, String viewName, IModel model, String viewNameAlt) {
		if (configuration.isEmailEnable()) {
			logger.debug("Email : Add new email to queue...");

			DropbitzEmail dropbitzEmail = new DropbitzEmail();
			dropbitzEmail.setSubject(subject);
			try {
				dropbitzEmail.setEmailFrom(configuration.getEmailFrom());
				dropbitzEmail.setEmailTo(configuration.getEmailTo());
				dropbitzEmail.setHtmlMsg(resolve(model, viewName));
				if (viewNameAlt != null) {
					dropbitzEmail.setTextMsg(resolve(model, viewNameAlt));
				}

				emailQueue.offer(dropbitzEmail);

				logger.error("Email added to queue");
			} catch (IOException | TemplateException e) {
				logger.error("Can not add email to queue", e);
			}
		} else {
			logger.debug("Email is disabled. Email not added to queue");
		}
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

		Objects.requireNonNull(model).setServletContext(servletContext);

		Map<String, IModel> map = new HashMap<String, IModel>();
		map.put(MODEL_NAME_ATTRIBUTE, model);

		template.process(map, writer);

		return writer.toString();
	}

	@Override
	public void start(final IServiceManager serviceManager) throws ServiceException {

		this.configuration = serviceManager.getService(IConfigurationService.class).getConfiguration();

		if (configuration.isEmailEnable()) {
			this.servletContext = serviceManager.getServletContext();

			emailQueue = new LinkedBlockingQueue<DropbitzEmail>();

			//Init Freemarker
			try {
				jerseyFreemarkerConfig = new JerseyFreemarkerConfig(servletContext);
			} catch (TemplateModelException e) {
				throw new ServiceException(e);
			}

            //Set Default EmailSender if not setted
            if (emailSender == null) {
                emailSender = new EmailSender(configuration);
            }

			//Start EmailRunnable
			emailExecutorService = Executors.newSingleThreadExecutor();
			emailExecutorService.execute(new EmailRunnable(emailQueue, emailSender));
		}
	}

	@Override
	public void stop() throws ServiceException {
	    //Stop EmailRunnable    
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
