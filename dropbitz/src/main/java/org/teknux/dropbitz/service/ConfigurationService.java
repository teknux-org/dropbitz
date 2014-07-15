package org.teknux.dropbitz.service;

import java.util.Objects;

import org.teknux.dropbitz.Application;
import org.teknux.dropbitz.config.Configuration;


public class ConfigurationService implements
    IConfigurationService {

	private Configuration configuration;

	public ConfigurationService() {
	}

	public void start(final Configuration configuration) {
	    this.configuration = Objects.requireNonNull(configuration, "Configuration can not be null");
	}
	
	@Override
	public void start(final IServiceManager serviceManager) {
		start(Application.getConfiguration());
	}

	@Override
	public void stop() {
	}

	public Configuration getConfiguration() {
		return configuration;
	}
}
