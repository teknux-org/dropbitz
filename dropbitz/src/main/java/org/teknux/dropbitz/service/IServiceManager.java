package org.teknux.dropbitz.service;

import javax.servlet.ServletContext;

public interface IServiceManager {
    ServletContext getServletContext();
    <T extends IService> T getService(final Class<T> serviceClass);
}
