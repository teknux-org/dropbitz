package org.teknux.dropbitz.services;

import java.util.Locale;
import java.util.ResourceBundle;


public class I18nService {
    private ResourceBundle resourceBundle;

    public I18nService(String resourceBaseName) {
        resourceBundle = ResourceBundle.getBundle(resourceBaseName, Locale.getDefault());
    }
    
    public String get(String key) {

        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            return key;
        }
    }
}
