package org.teknux.dropbitz.service;

import javax.servlet.ServletContext;

public interface IServiceManager {
    ServletContext getServletContext();
    <T> T getService(final Class<T> serviceClass);
}
