package org.teknux.dropbitz.util;

import java.util.Objects;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.service.ServiceManager;


public class DropBitzServlet extends ServletContainer {

	private static final long serialVersionUID = 1L;

	public static final String CONTEXT_ATTRIBUTE_SERVICE_MANAGER = "DropBitzServiceManager";

	private static Logger logger = LoggerFactory.getLogger(DropBitzServlet.class);

	ServiceManager serviceManager;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			logger.trace("Starting Service Manager...");
			serviceManager = new ServiceManager(filterConfig.getServletContext());
			serviceManager.start();
			filterConfig.getServletContext().setAttribute(CONTEXT_ATTRIBUTE_SERVICE_MANAGER, serviceManager);
			logger.trace("Service Manager started");
		} catch (Exception e) {
			logger.error("Error while starting application services", e);
			throw new UnavailableException("Error while initializing application services");
		}

		super.init(filterConfig);
	}

	@Override
	public void destroy() {
		Objects.requireNonNull(serviceManager);
		serviceManager.stop(); // stop the service
		getServletContext().removeAttribute(CONTEXT_ATTRIBUTE_SERVICE_MANAGER);
		logger.trace("Service Manager stopped.");

		super.destroy();
	}
}
