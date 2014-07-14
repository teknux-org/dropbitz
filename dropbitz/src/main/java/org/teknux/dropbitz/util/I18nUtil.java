package org.teknux.dropbitz.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;

import org.teknux.dropbitz.exception.I18nServiceException;

import com.google.common.base.Strings;

public class I18nUtil {
    private static final String LOCALE_STRING_SEPARATOR = "_";
    
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
