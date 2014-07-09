package org.teknux.dropbitz.service;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class I18nService implements IService{
    private String resourceBaseName;

    public I18nService(String resourceBaseName) {
        this.resourceBaseName = resourceBaseName;
    }
        
    public String get(String key, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceBaseName, locale);
                
        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            return key;
        }
    }

    @Override
    public void start() {
        //Workaround because ResourceBundle use default locale as default traduction file
        Locale.setDefault(Locale.ENGLISH);
    }

    @Override
    public void stop() {
    }
    
    public static Locale getLocaleFromString(String name) {
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
                return null;
        }
        
        try {
            //Check validity
            locale.getISO3Language();
            locale.getISO3Country();
            
            return locale;
        } catch (MissingResourceException e) {
            return null;
        }
    }
}
