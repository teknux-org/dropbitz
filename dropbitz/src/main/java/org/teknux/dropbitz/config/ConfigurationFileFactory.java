package org.teknux.dropbitz.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Properties;

import org.skife.config.ConfigurationObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigurationFileFactory {

	private static Logger logger = LoggerFactory.getLogger(ConfigurationFileFactory.class);

	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String DEFAULT_FILENAME = "config.properties";
	private static final String RESOURCE_SEPARATOR = "/";

	private ConfigurationFileFactory() {

	}

	/**
	 * Get configuration If file not specified, find config
	 * in jar location and on jar resource
	 * 
	 * @return
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public static ConfigurationFile getConfiguration() throws IOException, ConfigurationException {
		return getConfiguration(null);
	}

	/**
	 * Get configuration If file not specified, find config
	 * in jar location and on jar resource
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public static ConfigurationFile getConfiguration(File file) throws IOException, ConfigurationException {
		return getConfiguration(file, true);
	}
	
	/**
	 * Get configuration If file not specified, find config
	 * in jar location and on jar resource
	 * 
	 * @param required
	 * @return
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public static ConfigurationFile getConfiguration(boolean required) throws IOException, ConfigurationException {
		return getConfiguration(null, required);
	}

	/**
	 * Get configuration If file not specified, find config
	 * in jar location and on jar resource
	 * 
	 * @param file
	 * @param required
	 * @return
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public static ConfigurationFile getConfiguration(File file, boolean required) throws IOException, ConfigurationException {
		logger.debug("Get configuration...");

		//If file not specified, find file in jar directory
		if (file == null) {
			file = new File(getJarDir() + File.separator + DEFAULT_FILENAME);
			logger.trace("Config not file specified. Search in current directory [{}]", file);
		} else {
			logger.trace("Config file specified [{}]", file);
		}

		InputStream inputStream = null;
		
		if (!file.exists()) {
			//Try to find config as resource
			inputStream = ConfigurationFileFactory.class.getResourceAsStream(RESOURCE_SEPARATOR + DEFAULT_FILENAME);
			if (inputStream == null) {
				if (required) {
					throw new IOException(MessageFormat.format("Can not find config file [{0}]", file.getPath()));
				} else {
					logger.warn("Can not find config file [{}]", file.getPath());
					return null;
				}
			} else {
				logger.trace("Find Config in Jar resource");
			}
		} else {
			if (!file.canRead()) {
				throw new IOException(MessageFormat.format("Can not read config file [{0}]", file.getPath()));
			}
			inputStream = new FileInputStream(file);

			logger.trace("Using Config file [{}]", file);
		}

		try {
			Properties properties = new Properties();
			properties.load(inputStream);
			ConfigurationObjectFactory factory = new ConfigurationObjectFactory(properties);
			return factory.build(ConfigurationFile.class);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * Get Jar location
	 * 
	 * @return
	 */
	private static String getJarDir() {
		return decodeUrl(new File(ConfigurationFileFactory.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent());
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
