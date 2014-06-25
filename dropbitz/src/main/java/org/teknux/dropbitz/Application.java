package org.teknux.dropbitz;

import java.io.IOException;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.ConfigurationException;
import org.teknux.dropbitz.config.ConfigurationFile;
import org.teknux.dropbitz.config.ConfigurationFileFactory;
import org.teknux.dropbitz.config.ConfigurationValidationException;
import org.teknux.jettybootstrap.JettyBootstrap;
import org.teknux.jettybootstrap.JettyBootstrapException;

public class Application {

    private static int EXIT_CODE_CONFIG_ERROR = 1;
    private static int EXIT_CODE_CONFIG_VALIDATION_ERROR = 2;
    private static int EXIT_CODE_JETTY_STARTUP_ERROR = 3;

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	private static ConfigurationFile configurationFile = null;
	
	public Application() {
		try {
            logger.debug("Loading application configuration...");
			configurationFile = loadConfiguration();

            logger.debug("Validating application configuration...");
            checkConfigurationFile(configurationFile);

            logger.debug("Starting Application...");
            JettyBootstrap.startSelf();
		} catch (ConfigurationValidationException e) {
            logger.error("Configuration validation error", e);
            System.exit(EXIT_CODE_CONFIG_VALIDATION_ERROR);
        } catch (IOException | ConfigurationException | IllegalArgumentException e) {
			logger.error("Configuration file error", e);
			System.exit(EXIT_CODE_CONFIG_ERROR);
		} catch (JettyBootstrapException e) {
			logger.error("Internal Server Error", e);
			System.exit(EXIT_CODE_JETTY_STARTUP_ERROR);
		}
	}

    protected ConfigurationFile loadConfiguration() throws IOException, ConfigurationException {
        return ConfigurationFileFactory.getConfiguration();
    }
	
	protected void checkConfigurationFile(ConfigurationFile configurationFile) throws ConfigurationValidationException {
		if (! configurationFile.getDirectory().isDirectory() || ! configurationFile.getDirectory().canWrite()) {
			throw new ConfigurationValidationException(MessageFormat.format("Can't write into Upload Directory : [{0}]", configurationFile.getDirectory().getPath()));
		}
		
	}

	public static ConfigurationFile getConfigurationFile() {
		return configurationFile;
	}
}
