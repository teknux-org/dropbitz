package org.teknux.dropbitz.service;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.exception.I18nServiceException;


public class I18nService implements II18nService {
    
    private static Logger logger = LoggerFactory.getLogger(I18nService.class);
    
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    
    private static String RESOURCE_BASE_NAME = "i18n.dropbitz";

    public I18nService() {
    }
        
    public String get(String key, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BASE_NAME, locale);
                
        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            logger.warn("Can not find I18n key [{}] in locale [{}]", key, locale.toString());
            return key;
        }
    }

    @Override
    public void start(final IServiceManager serviceManager) {
        Locale.setDefault(DEFAULT_LOCALE);
    }

    @Override
    public void stop() {
    }
    
    public static Locale getLocaleFromString(String name) throws I18nServiceException {
        String parts[] = name.split("_");
        Locale locale = null;
        switch (parts.length) {
            case 1:
                locale = new Locale(name);
                break;
            case 2:
                locale = new Locale(parts[0], parts[1]);
                break;
            case 3:
                locale = new Locale(parts[0], parts[1], parts[2]);
                break;
            default:
                throw new I18nServiceException("Bad Lang format");
        }
        
        try {
            //Check validity
            locale.getISO3Language();
            locale.getISO3Country();
            
            return locale;
        } catch (MissingResourceException e) {
            throw new I18nServiceException(e);
        }
    }
}
