package org.teknux.dropbitz.service;

import java.util.Objects;

import javax.servlet.ServletContext;

import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.util.DropBitzServlet;


/**
 * Central access point for services
 */
public class ServiceManager {
    private ConfigurationService configurationService;
    private EmailService emailService;
    private StorageService storageService;
    private I18nService i18nService;

    private final ServletContext servletContext;

    public ServiceManager(ServletContext context) {
        this.servletContext = context;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public synchronized void start() throws ServiceException {
        storageService = new StorageService();
        storageService.start(this);

        configurationService = new ConfigurationService();
        configurationService.start(this);

        emailService = new EmailService();
        emailService.start(this);
        
        i18nService = new I18nService();
        i18nService.start(this);
    }

    public synchronized void stop() {
        configurationService.stop();
        storageService.stop();
    }

    public synchronized static ServiceManager get(ServletContext context) {
        Objects.requireNonNull(context);
        return (ServiceManager) context.getAttribute(DropBitzServlet.CONTEXT_ATTRIBUTE_SERVICE_MANAGER);
    }

    /**
     * @return a new instance of the user service. this service uses resources that needs to be free when done.
     * @see AutoCloseable
     */
    public IUserService getUserService() {
        return new DatabaseUserService(this.storageService);
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public I18nService getI18nService() {
        return i18nService;
    }
}
