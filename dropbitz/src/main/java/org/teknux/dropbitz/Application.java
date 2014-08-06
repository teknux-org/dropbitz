/*
 * Copyright (C) 2014 TekNux.org
 *
 * This file is part of the dropbitz Community GPL Source Code.
 *
 * dropbitz Community Source Code is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dropbitz Community Source Code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dropbitz Community Source Code.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teknux.dropbitz;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.config.ConfigurationFactory;
import org.teknux.dropbitz.exception.ConfigurationException;
import org.teknux.dropbitz.exception.ConfigurationValidationException;
import org.teknux.jettybootstrap.JettyBootstrap;
import org.teknux.jettybootstrap.JettyBootstrapException;
import org.teknux.jettybootstrap.configuration.JettyConfiguration;
import org.teknux.jettybootstrap.configuration.JettyConnector;

import java.net.URL;
import java.text.MessageFormat;

public class Application {

    private static final String LOGBACK_LEVEL_STDOUT_KEY = "logback.level.stdout";
    private static final String LOGBACK_LEVEL_FILE_KEY = "logback.level.file";
    private static final String LOGBACK_STDOUT_PATTERN_KEY = "logback.stdout.pattern";
    private static final String LOGBACK_LEVEL_DEBUG_VALUE = "TRACE";
    private static final String LOGBACK_STDOUT_PATTERN_DEBUG_VALUE = "FULL";

    private static final int EXIT_CODE_CONFIG_ERROR = 1;
    private static final int EXIT_CODE_CONFIG_VALIDATION_ERROR = 2;
    private static final int EXIT_CODE_JETTY_STARTUP_ERROR = 3;
    private static final int EXIT_CODE_LOGGER_ERROR = 4;

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static volatile Configuration configuration = null;

    private JettyBootstrap jettyBootstrap;

    public Application() {
        this(null);
    }

    public Application(Configuration configuration) {
        Application.configuration = configuration;
    }

    public void start() {
        start(false);
    }

    public void start(boolean join) {
        if (jettyBootstrap == null) {
            init();
        }

        try {
            logger.debug("Starting Application...");
            jettyBootstrap.startServer(join);
        } catch (JettyBootstrapException e) {
            logger.error("Internal Server Error", e);
            System.exit(EXIT_CODE_JETTY_STARTUP_ERROR);
        }
    }

    public void stop() {
        if (!isStarted()) {
            throw new IllegalArgumentException("Application is not started");
        }
        try {
            logger.debug("Stopping Application...");
            jettyBootstrap.stopServer();
        } catch (JettyBootstrapException e) {
            logger.error("Error while stopping application", e);
        }
    }

    public boolean isStarted() {
        return jettyBootstrap != null && jettyBootstrap.isServerStarted();
    }

    private void init() {
        logger.debug("Init application...");

        try {
            if (configuration == null) {
                logger.debug("Loading application configuration...");
                Application.configuration = loadConfiguration();
            } else {
                logger.debug("Using provided application configuration...");
            }

            logger.debug("Init logger...");
            initLogger(Application.configuration.isDebug());

            logger.debug("Validating application configuration...");
            checkConfiguration(Application.configuration);

            logger.debug("Init Temporary Directory...");
            initTempDirectory();

            logger.debug("Init Server...");
            initServer();

        } catch (JoranException e) {
            logger.error("Logger error", e);
            System.exit(EXIT_CODE_LOGGER_ERROR);
        } catch (ConfigurationValidationException e) {
            logger.error("Configuration validation error", e);
            System.exit(EXIT_CODE_CONFIG_VALIDATION_ERROR);
        } catch (ConfigurationException | IllegalArgumentException e) {
            logger.error("Configuration file error", e);
            System.exit(EXIT_CODE_CONFIG_ERROR);
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
        return ConfigurationFactory.getConfiguration(Configuration.class);
    }

    /**
     * Logger Initialization
     * Reload logger if debug flag is setted
     *
     * @param debug
     * @throws JoranException
     */
    private void initLogger(boolean debug) throws JoranException {
        // make JDK logging redirect to LogBack
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        if (debug) { //If debug flag, reload debugger with good properties
            logger.info("Debug : Reload Logger Configuration...");

            System.setProperty(LOGBACK_LEVEL_STDOUT_KEY, LOGBACK_LEVEL_DEBUG_VALUE);
            System.setProperty(LOGBACK_LEVEL_FILE_KEY, LOGBACK_LEVEL_DEBUG_VALUE);
            System.setProperty(LOGBACK_STDOUT_PATTERN_KEY, LOGBACK_STDOUT_PATTERN_DEBUG_VALUE);

            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

            ContextInitializer contextInitializer = new ContextInitializer(loggerContext);
            URL url = contextInitializer.findURLOfDefaultConfigurationFile(true);

            JoranConfigurator joranConfigurator = new JoranConfigurator();
            joranConfigurator.setContext(loggerContext);
            loggerContext.reset();
            joranConfigurator.doConfigure(url);

            StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
        }
    }

    /**
     * Initialize the application temporary directory.
     */
    private void initTempDirectory() {
        if (getConfiguration().getTemporaryDirectory() != null) {
            System.setProperty("java.io.tmpdir", getConfiguration().getTemporaryDirectory().getPath());
        }
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

        if (configuration.getTemporaryDirectory() != null && (!configuration.getTemporaryDirectory().isDirectory() || !configuration.getTemporaryDirectory().canWrite() )) {
            throw new ConfigurationValidationException(MessageFormat.format("Can not write into Temporary Directory : [{0}]", configuration.getTemporaryDirectory().getPath()));
        }
    }

    /**
     * Init Jetty Container
     */
    protected void initServer() {
        JettyConfiguration jettyConfiguration = new JettyConfiguration();
        if (configuration.isSsl()) {
            jettyConfiguration.setJettyConnectors(JettyConnector.HTTPS);
            jettyConfiguration.setSslPort(configuration.getPort());
        } else {
            jettyConfiguration.setPort(configuration.getPort());
        }
        jettyBootstrap = new JettyBootstrap(jettyConfiguration);
        jettyBootstrap.addSelf(configuration.getBasePath());
    }

    public static Configuration getConfiguration() {
        return configuration;
    }
}