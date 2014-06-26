package org.teknux.dropbitz.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.skife.config.ConfigurationObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationFileFactory {

	private static Logger logger = LoggerFactory
			.getLogger(ConfigurationFileFactory.class);

	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CONFIG_FILENAME = "config.properties";
	private static final String CONFIG_FILENAME_DIST = "config.properties-dist";
	private static final String RESOURCE_SEPARATOR = "/";

	private ConfigurationFileFactory() {

	}

	/**
	 * Get configuration in jar directory, in resource or create config
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public static ConfigurationFile getConfiguration() throws ConfigurationException {
		logger.debug("Get configuration...");

		InputStream inputStream = null;
		
		File file = new File(getJarDir() + File.separator + CONFIG_FILENAME);

		logger.debug("Check if config file exists in jar directory [{}]...", file.getPath());
		if (file.exists()) { //Check if file exists in jar directory
			logger.debug("Check if config file is readable in jar directory [{}]...", file.getPath());
			if (! file.canRead()) { //Check if file is readable in jar directory
				throw new ConfigurationException(MessageFormat.format("Config file is not readable in jar directory [{0}]", file.getPath()));
			} else { //Open config file in jar directory
				logger.debug("Can not open config file in jar directory [{}]...", file.getPath());
				try {
					inputStream = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					throw new ConfigurationException(MessageFormat.format("Can not read config file [{0}]", file.getPath()));
				}
			}
		} else { //Check if config file exits in resource
			logger.debug("Check if config file exists in resource [{}]...", RESOURCE_SEPARATOR + CONFIG_FILENAME);
				
			inputStream = ConfigurationFileFactory.class.getResourceAsStream(RESOURCE_SEPARATOR + CONFIG_FILENAME);
			if (inputStream != null) {
				logger.warn("Use resource config");				
			} else { //If not, Create file in jar directory
				logger.debug("Create config file exists in jar directory [{}]...", file.getPath());
				try {
					logger.warn("File does not exits. Create...");
					FileUtils.copyURLToFile(ConfigurationFileFactory.class.getResource(RESOURCE_SEPARATOR + CONFIG_FILENAME_DIST), file);
					inputStream = new FileInputStream(file);
				} catch (IOException e) {
					throw new ConfigurationException(MessageFormat.format("Can not create config file in jar directory [{0}]", file.getPath()));
				}
			}
		}
		
		try {
			Properties properties = new Properties();
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				throw new ConfigurationException(MessageFormat.format("Bad config file [{0}]", file.getPath()));
			}
			ConfigurationObjectFactory factory = new ConfigurationObjectFactory(properties);
			return factory.build(ConfigurationFile.class);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("Can't close input stream", e);
				}
			}
		}
	}

	/**
	 * Get Jar location
	 * 
	 * @return
	 */
	private static String getJarDir() {
		return decodeUrl(new File(ConfigurationFileFactory.class
				.getProtectionDomain().getCodeSource().getLocation().getPath())
				.getParent());
	}

	/**
	 * Decode Url
	 * 
	 * @param url
	 * @return
	 */
	private static String decodeUrl(String url) {
		if (url == null) {
			return null;
		}

		try {
			return URLDecoder.decode(url, CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}
}
