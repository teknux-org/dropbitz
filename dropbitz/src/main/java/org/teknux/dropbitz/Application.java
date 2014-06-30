package org.teknux.dropbitz;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.ConfigurationException;
import org.teknux.dropbitz.config.ConfigurationFile;
import org.teknux.dropbitz.config.ConfigurationFileFactory;
import org.teknux.dropbitz.config.ConfigurationValidationException;
import org.teknux.jettybootstrap.JettyBootstrap;
import org.teknux.jettybootstrap.JettyBootstrapException;
import org.teknux.jettybootstrap.configuration.JettyConfiguration;
import org.teknux.jettybootstrap.configuration.JettyConnector;

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
            startApplication();
           
		} catch (ConfigurationValidationException e) {
            logger.error("Configuration validation error", e);
            System.exit(EXIT_CODE_CONFIG_VALIDATION_ERROR);
        } catch (ConfigurationException | IllegalArgumentException e) {
			logger.error("Configuration file error", e);
			System.exit(EXIT_CODE_CONFIG_ERROR);
		} catch (JettyBootstrapException e) {
			logger.error("Internal Server Error", e);
			System.exit(EXIT_CODE_JETTY_STARTUP_ERROR);
		}
	}

	/**
	 * Load configuration
	 * 
	 * @return ConfigurationFile
	 * @throws ConfigurationException on error
	 */
    protected ConfigurationFile loadConfiguration() throws ConfigurationException {
        return ConfigurationFileFactory.getConfiguration();
    }
	
    /**
     * Check configuration
     * 
     * @param configurationFile Instance that contains configuration properties
     * @throws ConfigurationValidationException on error
     */
	protected void checkConfigurationFile(ConfigurationFile configurationFile) throws ConfigurationValidationException {
		if (! configurationFile.getDirectory().isDirectory() || ! configurationFile.getDirectory().canWrite()) {
			throw new ConfigurationValidationException(MessageFormat.format("Can not write into Upload Directory : [{0}]", configurationFile.getDirectory().getPath()));
		}
		
	}
	
	/**
	 * Start Jetty Container
	 * 
	 * @throws JettyBootstrapException on error
	 */
	protected void startApplication() throws JettyBootstrapException {
        JettyConfiguration jettyConfiguration = new JettyConfiguration();
        if (configurationFile.isSsl()) {
        	jettyConfiguration.setJettyConnectors(JettyConnector.HTTPS);
        	jettyConfiguration.setSslPort(configurationFile.getPort());	
        } else {
        	jettyConfiguration.setPort(configurationFile.getPort());
        }
        JettyBootstrap jettyBootstrap = new JettyBootstrap(jettyConfiguration);
        jettyBootstrap.addSelf().startServer();
	}

	/**
	 * Get configuration
	 * @return ConfigurationFile on error
	 */
	public static ConfigurationFile getConfigurationFile() {
		return configurationFile;
	}
}
