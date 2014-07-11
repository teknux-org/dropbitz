package org.teknux.dropbitz.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.service.email.EmailService;
import org.teknux.dropbitz.service.email.IEmailService;
import org.teknux.dropbitz.util.DropBitzServlet;


/**
 * Central access point for services
 */
public class ServiceManager {

	private static Logger logger = LoggerFactory.getLogger(ServiceManager.class);

	/**
	 * Running Service List
	 */
	private final Map<Class<? extends IService>, IService> services = new LinkedHashMap<>();

	private final ServletContext servletContext;

	public ServiceManager(ServletContext context) {
		this.servletContext = context;

		services.put(ConfigurationService.class, new ConfigurationService());
		services.put(StorageService.class, new StorageService());
		services.put(IEmailService.class, new EmailService());
		services.put(I18nService.class, new I18nService());
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void start() throws ServiceException {
		synchronized (services) {
			logger.trace("Starting {} Services...", services.size());
			for (final IService service : services.values()) {
				logger.trace("Starting Service [{}]...", service.getClass().getSimpleName());
				service.start(this);
				logger.trace("Service [{}] started", service.getClass().getSimpleName());
			}
			logger.trace("{} Services started", services.size());
		}
	}

	public void stop() throws ServiceException {
		synchronized (services) {
			logger.trace("Stopping {} Services...", services.size());
			for (final IService service : services.values()) {
				logger.trace("Stopping Service [{}]...", service.getClass().getSimpleName());
				service.stop();
				logger.trace("Service [{}] stopped", service.getClass().getSimpleName());
			}
			logger.trace("{} Services stopped", services.size());
		}
	}

	public static ServiceManager get(ServletContext context) {
		Objects.requireNonNull(context);
		return (ServiceManager) context.getAttribute(DropBitzServlet.CONTEXT_ATTRIBUTE_SERVICE_MANAGER);
	}

	public <T> T getService(final Class<T> serviceClass) {
		synchronized (services) {
			final IService wantedService = services.get(serviceClass);
			if (wantedService == null || !serviceClass.isAssignableFrom(wantedService.getClass())) {
				throw new IllegalArgumentException("Service Unavailable");
			}
			final T castedService = serviceClass.cast(wantedService);
			if (castedService == null) {
				throw new IllegalArgumentException("Error accessing service");
			}
			return castedService;
		}
	}
}
