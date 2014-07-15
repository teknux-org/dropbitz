package org.teknux.dropbitz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.exception.DropBitzException;

public class ApplicationProperties {

    private static Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    
    private static final String PROPERTIES_FILE = "/application.properties";
    
    public static final String APPLICATION_NAME_KEY = "application.name";
    public static final String APPLICATION_VERSION_KEY = "application.version";
    
    private static ApplicationProperties instance;
    
    private Properties properties; 
    
    //Singleton
    public ApplicationProperties(String propertiesFile) throws DropBitzException {
        InputStream inputStream = ApplicationProperties.class.getResourceAsStream(propertiesFile);
        
        if (inputStream == null) {
            throw new DropBitzException("Application properties file does not exists");
        }
        
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new DropBitzException("Can not get properties", e);
        }
        
        try {
            inputStream.close();
        } catch (IOException e) {
            logger.error("Can not close InputStream", e);
        }
    }
    
    public static ApplicationProperties getInstance() throws DropBitzException {
        if (instance == null) {
            instance = new ApplicationProperties(PROPERTIES_FILE);
        }
        
        return instance;
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
