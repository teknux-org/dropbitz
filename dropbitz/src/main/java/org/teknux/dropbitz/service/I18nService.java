package org.teknux.dropbitz.service;

import java.util.Locale;
import java.util.ResourceBundle;

import org.teknux.dropbitz.exception.DropBitzException;


public class I18nService implements IService{
    private String resourceBaseName;
    private ResourceBundle resourceBundle = null;

    public I18nService(String resourceBaseName) {
        this.resourceBaseName = resourceBaseName;
    }
    
    public String get(String key) {

        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            return key;
        }
    }

    @Override
    public void start() throws DropBitzException {
        resourceBundle = ResourceBundle.getBundle(resourceBaseName, Locale.getDefault());
    }

    @Override
    public void stop() {
    }
}
