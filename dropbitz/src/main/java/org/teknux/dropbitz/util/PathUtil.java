package org.teknux.dropbitz.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.teknux.dropbitz.config.ConfigurationFactory;

public class PathUtil {
    private static final String CHARSET_UTF8 = "UTF-8";
    
    /**
     * Get Jar location
     * 
     * @return Jar directory
     */
    public static String getJarDir() {
        return decodeUrl(new File(ConfigurationFactory.class
                .getProtectionDomain().getCodeSource().getLocation().getPath())
                .getParent());
    }

    /**
     * Decode Url
     * 
     * @param url
     * @return
     */
    private static String decodeUrl(String url) {
        if (url == null) {
            return null;
        }

        try {
            return URLDecoder.decode(url, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }
}
