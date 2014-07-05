package org.teknux.dropbitz.services;

import java.util.Objects;

import javax.servlet.ServletContext;

import org.teknux.dropbitz.util.DropBitzServlet;


/**
 * Central access point for services
 */
public class ServiceManager implements
		IService {

	private IUserService userService;
	private IConfigurationService configurationService;
	private IEmailService emailService;

	private final ServletContext servletContext;

	public ServiceManager(ServletContext context) {
		this.servletContext = context;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public synchronized void start() {
		configurationService = new ConfigurationService();
		configurationService.start();

		userService = new InMemoryUserService();
		userService.start();

		emailService = new EmailService(this);
		emailService.start();
	}

	public synchronized void stop() {
		userService.stop();
		configurationService.stop();
	}

	public synchronized static ServiceManager get(ServletContext context) {
		Objects.requireNonNull(context);
		return (ServiceManager) context.getAttribute(DropBitzServlet.CONTEXT_ATTRIBUTE_SERVICE_MANAGER);
	}

	public IUserService getUserService() {
		return userService;
	}

	public IConfigurationService getConfigurationService() {
		return configurationService;
	}

	public IEmailService getEmailService() {
		return emailService;
	}
}
