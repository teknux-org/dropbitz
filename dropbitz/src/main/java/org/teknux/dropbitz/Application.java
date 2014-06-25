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

    private static int EXIT_CODE_CONFIG_ERROR = 1;
    private static int EXIT_CODE_JETTY_STARTUP_ERROR = 2;

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	private static ConfigurationFile configurationFile = null;
	
	public Application() {
		try {
			configurationFile = ConfigurationFileFactory.getConfiguration();
		} catch (IOException | ConfigurationException | IllegalArgumentException e) {
			logger.error("Configuration file error", e);
			System.exit(EXIT_CODE_CONFIG_ERROR);
		}
		
		try {
			checkConfigurationFile(configurationFile);
		} catch (ConfigurationException e) {
			logger.error("Bad configuration", e);
			System.exit(1);
		}
		
		logger.debug("Starting Self...");
		try {
			JettyBootstrap.startSelf();
		} catch (JettyBootstrapException e) {
			logger.error("Internal Server Error", e);
			System.exit(EXIT_CODE_JETTY_STARTUP_ERROR);
		}
	}
	
	private void checkConfigurationFile(ConfigurationFile configurationFile) throws ConfigurationException {
		if (! configurationFile.getDirectory().isDirectory() || ! configurationFile.getDirectory().canWrite()) {
			throw new ConfigurationException("Can't write in Directory");
		}
		
	}

	public static ConfigurationFile getConfigurationFile() {
		return configurationFile;
	}
}
