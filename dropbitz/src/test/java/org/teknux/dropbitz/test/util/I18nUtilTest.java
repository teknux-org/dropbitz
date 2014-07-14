package org.teknux.dropbitz.test.util;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.teknux.dropbitz.exception.I18nServiceException;
import org.teknux.dropbitz.util.I18nUtil;

public class I18nUtilTest {
    @Test
    public void test01BadLocaleFromString() throws I18nServiceException {
        try {
            I18nUtil.getLocaleFromString(null);
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e){
        }
        try {
            I18nUtil.getLocaleFromString("");
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e){
        }
        try {
            I18nUtil.getLocaleFromString("x");
            Assert.fail("Should throw I18nServiceException");
        } catch (I18nServiceException e){
        }
    }
    
    @Test
    public void test02LocaleFromString() throws I18nServiceException {
        Assert.assertEquals(Locale.ENGLISH, I18nUtil.getLocaleFromString(Locale.ENGLISH.toString()));
        Assert.assertEquals(Locale.FRENCH, I18nUtil.getLocaleFromString(Locale.FRENCH.toString()));
        Assert.assertEquals(Locale.US, I18nUtil.getLocaleFromString(Locale.US.toString()));
        Assert.assertEquals(Locale.FRANCE, I18nUtil.getLocaleFromString(Locale.FRANCE.toString()));
    }
}
