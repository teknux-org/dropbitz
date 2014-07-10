package org.teknux.dropbitz.service;

import org.teknux.dropbitz.Application;
import org.teknux.dropbitz.config.Configuration;


public class ConfigurationService implements
    IService {

	private Configuration configuration;

	public ConfigurationService() {
	}

	@Override
	public void start(final ServiceManager serviceManager) {
		configuration = Application.getConfiguration();
	}

	@Override
	public void stop() {
	}

	public Configuration getConfiguration() {
		return configuration;
	}
}
