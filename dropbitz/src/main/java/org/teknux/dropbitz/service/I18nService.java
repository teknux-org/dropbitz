package org.teknux.dropbitz.service;

import java.util.Locale;
import java.util.ResourceBundle;


public class I18nService implements IService{
    private String resourceBaseName;

    public I18nService(String resourceBaseName) {
        this.resourceBaseName = resourceBaseName;
    }
        
    public String get(String key, Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceBaseName, locale);
                
        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            return key;
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
