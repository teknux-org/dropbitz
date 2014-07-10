package org.teknux.dropbitz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.util.DropBitzServlet;


/**
 * Central access point for services
 */
public class ServiceManager {
    
    private static Logger logger = LoggerFactory.getLogger(ServiceManager.class);

    /**
     * Service list that will be started by manager
     */
    @SuppressWarnings("serial")
    private final static List<Class <? extends IService>> SERVICE_CLASSES = new ArrayList<Class<? extends IService>>() {{
        add(ConfigurationService.class);
        add(StorageService.class);
        add(EmailService.class);
        add(I18nService.class);
    }};
    
    /**
     * Running Service List
     */
	private Map<Class<? extends IService>, IService> services = new HashMap<Class<? extends IService>, IService>();

	private final ServletContext servletContext;

	public ServiceManager(ServletContext context) {
		this.servletContext = context;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public synchronized void start() throws ServiceException {
	    logger.trace("Starting {} Services...", SERVICE_CLASSES.size());
		for (Class<? extends IService> serviceClass : SERVICE_CLASSES) {
		    logger.trace("Starting Service [{}]...", serviceClass.getSimpleName());
		    
		    IService service = null;
		    try {
                service = serviceClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ServiceException(e);
            }
		    service.start(this);
		    services.put(serviceClass, service);
		    
		    logger.trace("Service [{}] started", serviceClass.getSimpleName());
		}
		logger.trace("{} Services started", SERVICE_CLASSES.size());
	}

	public synchronized void stop() {
	    logger.trace("Stopping {} Services...", services.size());
	    for (IService service : services.values()) {
	        logger.trace("Stopping Service [{}]...", service.getClass().getSimpleName());
	        service.stop();
	        logger.trace("Service [{}] stopped", service.getClass().getSimpleName());
	    }
	    services.clear();
	    logger.trace("{} Services stopped", services.size());
	}

	public synchronized static ServiceManager get(ServletContext context) {
		Objects.requireNonNull(context);
		return (ServiceManager) context.getAttribute(DropBitzServlet.CONTEXT_ATTRIBUTE_SERVICE_MANAGER);
	}

    @SuppressWarnings("unchecked")
    public <T extends IService> T getService(Class<T> serviceClass) {
        if (! services.containsKey(serviceClass)) {
            throw new IllegalArgumentException("Service unreachable");
        }
	    return (T)services.get(serviceClass);
	}
}
