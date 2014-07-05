package org.teknux.dropbitz.services;

import org.teknux.dropbitz.Application;
import org.teknux.dropbitz.config.ConfigurationFile;


public class ConfigurationService implements
		IConfigurationService {

	private ConfigurationFile configuration;

	public ConfigurationService() {
	}

	@Override
	public void start() {
		configuration = Application.getConfigurationFile();
	}

	@Override
	public void stop() {
	}

	@Override
	public ConfigurationFile getConfiguration() {
		return configuration;
	}
}
