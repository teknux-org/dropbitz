package org.teknux.dropbitz;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.config.ConfigurationFactory;
import org.teknux.dropbitz.exception.ConfigurationException;
import org.teknux.dropbitz.exception.ConfigurationValidationException;
import org.teknux.jettybootstrap.JettyBootstrap;
import org.teknux.jettybootstrap.JettyBootstrapException;
import org.teknux.jettybootstrap.configuration.JettyConfiguration;
import org.teknux.jettybootstrap.configuration.JettyConnector;


public class Application {

	private static int EXIT_CODE_CONFIG_ERROR = 1;
	private static int EXIT_CODE_CONFIG_VALIDATION_ERROR = 2;
	private static int EXIT_CODE_JETTY_STARTUP_ERROR = 3;

	private static Logger logger = LoggerFactory.getLogger(Application.class);

	private static volatile Configuration configuration = null;

	private JettyBootstrap jettyBootstrap;

	public Application() {
		this(null, true);
	}

	public Application(Configuration configuration, boolean join) {
		try {
			if (configuration == null) {
				logger.debug("Loading application configuration...");
				Application.configuration = loadConfiguration();
			} else {
				logger.debug("Using provided application configuration...");
				Application.configuration = configuration;
			}
			logger.debug("Validating application configuration...");
			checkConfiguration(Application.configuration);

			logger.debug("Starting Application...");
			startApplication(join);

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
	 * @return Configuration
	 * @throws ConfigurationException
	 *             on error
	 */
	protected Configuration loadConfiguration() throws ConfigurationException {
		return ConfigurationFactory.getConfiguration();
	}

	/**
	 * Check configuration
	 * 
	 * @param configuration
	 *            Instance that contains configuration properties
	 * @throws ConfigurationValidationException
	 *             on error
	 */
	protected void checkConfiguration(Configuration configuration) throws ConfigurationValidationException {
		if (!configuration.getDirectory().isDirectory() || !configuration.getDirectory().canWrite()) {
			throw new ConfigurationValidationException(MessageFormat.format("Can not write into Upload Directory : [{0}]", configuration.getDirectory().getPath()));
		}

	}

	/**
	 * Start Jetty Container
	 * 
	 * @throws JettyBootstrapException
	 *             on error
	 */
	protected void startApplication(boolean join) throws JettyBootstrapException {
		JettyConfiguration jettyConfiguration = new JettyConfiguration();
		if (configuration.isSsl()) {
			jettyConfiguration.setJettyConnectors(JettyConnector.HTTPS);
			jettyConfiguration.setSslPort(configuration.getPort());
		} else {
			jettyConfiguration.setPort(configuration.getPort());
		}
		jettyBootstrap = new JettyBootstrap(jettyConfiguration);
		jettyBootstrap.addSelf().startServer(join);
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	public JettyBootstrap getJettyBootstrap() {
		return jettyBootstrap;
	}

	public boolean isStarted() {
		return jettyBootstrap != null;
	}

	public void stop() {
		if (!isStarted()) {
			throw new IllegalArgumentException("Application is not started");
		}
		try {
			jettyBootstrap.stopServer();
		} catch (JettyBootstrapException e) {
			logger.error("Error while stopping application", e);
		}
	}
}
