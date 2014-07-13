package org.teknux.dropbitz.service;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.exception.I18nServiceException;

import com.google.common.base.Strings;


public class I18nService implements II18nService {
    
    private static Logger logger = LoggerFactory.getLogger(I18nService.class);
    
    private static final Locale DEFAULT_DEFAULT_LOCALE = Locale.ENGLISH;
    private static final String DEFAULT_RESOURCE_BASE_NAME = "i18n.dropbitz";
    
    private boolean defaultLocaleSetted = false;
    private String resourceBaseName = DEFAULT_RESOURCE_BASE_NAME;
    
    private static final String LOCALE_STRING_SEPARATOR = "_";

    public I18nService() {
    }
    
    public void setDefaultLocale(Locale defaultLocale) {
        defaultLocaleSetted = true;
        Locale.setDefault(defaultLocale);
    }
    
    public void setResourceBaseName(String resourceBaseName) {
        this.resourceBaseName = resourceBaseName;
    }
        
    public String get(String key, Locale locale) {
        Objects.requireNonNull(resourceBaseName, "I18n resourceBaseName can not be null");
        ResourceBundle resourceBundle = null;
        if (locale == null) {
            resourceBundle = ResourceBundle.getBundle(resourceBaseName);
        } else {
            resourceBundle = ResourceBundle.getBundle(resourceBaseName, locale);
        }
                
        if (resourceBundle.containsKey(Objects.requireNonNull(key, "I18n key can not be null"))) {
            return resourceBundle.getString(key);
        } else {
            logger.warn("Can not find I18n key [{}] in locale [{}]", key, (locale == null?Locale.getDefault().toString():locale.toString()));
            return key;
        }
    }

    @Override
    public void start(final IServiceManager serviceManager) throws I18nServiceException {
        if (! defaultLocaleSetted) {
            Locale.setDefault(DEFAULT_DEFAULT_LOCALE);
        }
    }

    @Override
    public void stop() {
    }
    
    public static Locale getLocaleFromString(String name) throws I18nServiceException {
        String parts[] = Objects.requireNonNull(Strings.emptyToNull(name), "Name can not be null or empty").split(LOCALE_STRING_SEPARATOR);
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
