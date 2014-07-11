package org.teknux.dropbitz.service;

import org.teknux.dropbitz.exception.ServiceException;


public interface IService {

	void start(final IServiceManager serviceManager) throws ServiceException;

	void stop() throws ServiceException;
}
