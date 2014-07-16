/*
 * Copyright (C) 2014 TekNux.org
 *
 * This file is part of the dropbitz Community GPL Source Code.
 *
 * dropbitz Community Source Code is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dropbitz Community Source Code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dropbitz Community Source Code.  If not, see <http://www.gnu.org/licenses/>.
 */

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
