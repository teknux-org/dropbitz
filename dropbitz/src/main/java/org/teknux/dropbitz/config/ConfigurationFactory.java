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
import org.teknux.dropbitz.exception.ConfigurationException;

public class ConfigurationFactory {

	private static Logger logger = LoggerFactory
			.getLogger(ConfigurationFactory.class);

	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CONFIG_FILENAME = "config.properties";
	private static final String CONFIG_FILENAME_DIST = "config.properties-dist";
	private static final String RESOURCE_SEPARATOR = "/";
	
	private static final String CONFIG_FILE = getJarDir() + File.separator + CONFIG_FILENAME;
	private static final String CONFIG_RESOURCE = RESOURCE_SEPARATOR + CONFIG_FILENAME;
	private static final String CONFIG_DIST_RESOURCE = RESOURCE_SEPARATOR + CONFIG_FILENAME_DIST;
	

	private ConfigurationFactory() {

	}

	/**
	 * Get configuration from jar directory, from resource or create new configuration file
	 * 
	 * @return ConfigurationFile
	 * @throws ConfigurationException On failed
	 */
	public static Configuration getConfiguration() throws ConfigurationException {
		logger.debug("Get configuration...");

		InputStream inputStream = null;
		
		File file = new File(CONFIG_FILE);

		logger.trace("Check if config file exists in jar directory [{}]...", file.getPath());
		if (file.exists()) { //Check if file exists in jar directory
			inputStream = getStreamFromFile(file);
			logger.debug("Use config file in jar directory");
		} else { //Check if config file exits in resource
			logger.trace("Check if config file exists in resource [{}]...", CONFIG_RESOURCE);
				
			inputStream = getStreamFromResource(CONFIG_RESOURCE);
			if (inputStream != null) {
				logger.warn("Use resource config");				
			} else { //If not, Create file in jar directory
				logger.warn("File does not exits. Create config file exists in jar directory [{}]...", file.getPath());
				try {
                    inputStream = createConfigurationFile(CONFIG_DIST_RESOURCE, file);
                } catch (IOException e) {
                    throw new ConfigurationException(MessageFormat.format("Can not create config file in jar directory [{0}]", file.getPath()), e);
                }
			}
		}
		
		//Build configuration
		try {
            return buildConfiguration(inputStream);
        } catch (IOException e) {
            throw new ConfigurationException(MessageFormat.format("Bad config file [{0}]", file.getPath()), e);
        }
	}
		
	/**
	 * Get Stream from file
	 * 
	 * @param file
	 * @return InputStream
	 * @throws ConfigurationException
	 */
	private static InputStream getStreamFromFile(File file) throws ConfigurationException {
        if (! file.canRead()) { //Check if file is readable in jar directory
            throw new ConfigurationException(MessageFormat.format("Config file is not readable in jar directory [{0}]", file.getPath()));
        } else { //Open config file in jar directory
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new ConfigurationException(MessageFormat.format("Can not read config file [{0}]", file.getPath()));
            }
        }
	}
	
	/**
	 * Get Stream from resource
	 * 
	 * @param resource
	 * @return InputStream
	 */
	private static InputStream getStreamFromResource(String resource) {
	    return ConfigurationFactory.class.getResourceAsStream(resource);
	}

	/**
	 * Create Configuration File from resource
	 * 
	 * @param file
	 * @param distResource
	 * @return InputStream
	 * @throws IOException 
	 */
	private static InputStream createConfigurationFile(String distResource, File file) throws IOException {
	    InputStream inputStream = null;
        FileUtils.copyURLToFile(ConfigurationFactory.class.getResource(distResource), file);
        inputStream = new FileInputStream(file);
        
	    return inputStream;
	}
	
	/**
	 * Build Configuration from Stream
	 * 
	 * @param inputStream
	 * @return Configuration
	 * @throws IOException
	 */
    private static Configuration buildConfiguration(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } finally {
            if (inputStream != null) {
              try {
                  inputStream.close();
              } catch (IOException e) {
                  logger.error("Can't close input stream", e);
              }
          }
        }
        
        ConfigurationObjectFactory factory = new ConfigurationObjectFactory(properties);
        return factory.build(Configuration.class);
    }
	   
	/**
	 * Get Jar location
	 * 
	 * @return
	 */
	private static String getJarDir() {
		return decodeUrl(new File(ConfigurationFactory.class
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
