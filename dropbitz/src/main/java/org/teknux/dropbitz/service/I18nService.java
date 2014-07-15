package org.teknux.dropbitz.service;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.exception.I18nServiceException;

public class I18nService implements II18nService {

    private static Logger logger = LoggerFactory.getLogger(I18nService.class);

    private static final Locale DEFAULT_DEFAULT_LOCALE = Locale.ENGLISH;
    private static final String DEFAULT_RESOURCE_BASE_NAME = "i18n.dropbitz";

    private String resourceBaseName;

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
            logger.warn("Can not find I18n key [{}] in locale [{}]", key, (locale == null ? Locale.getDefault().toString() : locale.toString()));
            return key;
        }
    }

    public void start(Locale defaultLocale, String resourceBaseName) {
        Locale.setDefault(Objects.requireNonNull(defaultLocale, "DefaultLocale can not be null"));

        this.resourceBaseName = Objects.requireNonNull(resourceBaseName, "ResourceBaseName can not be null");
    }

    @Override
    public void start(final IServiceManager serviceManager) throws I18nServiceException {
        start(DEFAULT_DEFAULT_LOCALE, DEFAULT_RESOURCE_BASE_NAME);
    }

    @Override
    public void stop() {
    }
}
