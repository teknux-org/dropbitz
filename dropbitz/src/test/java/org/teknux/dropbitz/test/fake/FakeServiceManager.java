package org.teknux.dropbitz.test.fake;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.teknux.dropbitz.service.IService;
import org.teknux.dropbitz.service.IServiceManager;

public class FakeServiceManager implements IServiceManager {

    private final Map<Class<? extends IService>, IService> services = new HashMap<>();
    
    private ServletContext servletContext;
        
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    public <T extends IService> void addService(Class<T> clazz, T service) {
        services.put(clazz, service);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends IService> T getService(Class<T> serviceClass) {
        final IService service = services.get(serviceClass);
        if (service == null) {
            throw new IllegalArgumentException("Service Unavailable");
        }
        return (T) service;
    }
};
