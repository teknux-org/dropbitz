package org.teknux.dropbitz.service;

import org.teknux.dropbitz.Application;
import org.teknux.dropbitz.config.Configuration;


public class ConfigurationService implements
		IConfigurationService {

	private Configuration configuration;

	public ConfigurationService() {
	}

	@Override
	public void start() {
		configuration = Application.getConfiguration();
	}

	@Override
	public void stop() {
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}
}
