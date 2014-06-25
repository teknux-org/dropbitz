package org.teknux.dropbitz;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.ConfigurationException;
import org.teknux.dropbitz.config.ConfigurationFile;
import org.teknux.dropbitz.config.ConfigurationFileFactory;
import org.teknux.jettybootstrap.JettyBootstrap;
import org.teknux.jettybootstrap.JettyBootstrapException;

public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	private static ConfigurationFile configurationFile = null;
	
	public Application() {
		try {
			configurationFile = ConfigurationFileFactory.getConfiguration();
		} catch (IOException | ConfigurationException | IllegalArgumentException e) {
			LOGGER.error("Configuration file error", e);
			System.exit(1);
		}
		
		LOGGER.debug("Starting Self...");
		try {
			JettyBootstrap.startSelf();
		} catch (JettyBootstrapException e) {
			LOGGER.error("Internal Server Error", e);
			System.exit(1);
		}
	}
	
	public static ConfigurationFile getConfigurationFile() {
		return configurationFile;
	}
}
