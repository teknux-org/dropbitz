package org.teknux.dropbitz.service;

import java.util.Locale;

public interface II18nService extends IService {
    String get(String key, Locale locale);
}
