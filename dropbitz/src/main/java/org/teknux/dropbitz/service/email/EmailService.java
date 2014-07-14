package org.teknux.dropbitz.service.email;

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

	private static final int TIMEOUT_ON_STOP = 1; //minutes

	private BlockingQueue<DropbitzEmail> emailQueue;
	
	private IEmailTemplateResolver emailTemplateResolver;
	private IEmailSender emailSender;

	private Configuration configuration;

	private ExecutorService emailExecutorService;

	public EmailService() {
	}

	public void setEmailTemplateResolver(IEmailTemplateResolver emailTemplateResolver) {
        this.emailTemplateResolver = emailTemplateResolver;
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


	@Override
	public void start(final IServiceManager serviceManager) throws ServiceException {

		this.configuration = serviceManager.getService(IConfigurationService.class).getConfiguration();

		if (configuration.isEmailEnable()) {

			emailQueue = new LinkedBlockingQueue<DropbitzEmail>();
			
			//Set Default EmailTemplateResolver if not setted
            if (emailTemplateResolver == null) {
                emailTemplateResolver = new EmailTemplateResolver(serviceManager.getServletContext());
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
