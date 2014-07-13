package org.teknux.dropbitz.test.service;

import java.util.Locale;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.teknux.dropbitz.exception.I18nServiceException;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.service.I18nService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class I18nServiceTest {

    @Test
    public void test01BadLocaleFromString() throws I18nServiceException {
        try {
            I18nService.getLocaleFromString(null);
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e){
        }
        try {
            I18nService.getLocaleFromString("");
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e){
        }
        try {
            I18nService.getLocaleFromString("x");
            Assert.fail("Should throw I18nServiceException");
        } catch (I18nServiceException e){
        }
    }
    
    @Test
    public void test02LocaleFromString() throws I18nServiceException {
        Assert.assertEquals(Locale.ENGLISH, I18nService.getLocaleFromString(Locale.ENGLISH.toString()));
        Assert.assertEquals(Locale.FRENCH, I18nService.getLocaleFromString(Locale.FRENCH.toString()));
        Assert.assertEquals(Locale.US, I18nService.getLocaleFromString(Locale.US.toString()));
        Assert.assertEquals(Locale.FRANCE, I18nService.getLocaleFromString(Locale.FRANCE.toString()));
    }

    
    @Test
    public void test03BadConfiguration() throws ServiceException {
        I18nService i18nService = new I18nService();
        try {
            i18nService.setDefaultLocale(null);
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e) {
        }
        
        i18nService = new I18nService();
        i18nService.setResourceBaseName(null);
        i18nService.start(null);
        try {
            i18nService.get("key1", null);
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e) {
        }
    }
    
    @Test
    public void test04KeyExistsDefaultEn() throws ServiceException {
        I18nService i18nService = new I18nService();
        i18nService.setDefaultLocale(Locale.ENGLISH);
        i18nService.start(null);

        Assert.assertEquals("value1", i18nService.get("key1", null));
        Assert.assertEquals("value2", i18nService.get("key2", null));
        Assert.assertEquals("key3", i18nService.get("key3", null));
        Assert.assertEquals("key4", i18nService.get("key4", null));
        
        Assert.assertEquals("value1fr", i18nService.get("key1", Locale.FRENCH));
        Assert.assertEquals("value2", i18nService.get("key2", Locale.FRENCH));
        Assert.assertEquals("value3fr", i18nService.get("key3", Locale.FRENCH));
        Assert.assertEquals("key4", i18nService.get("key4", Locale.FRENCH));
        
        i18nService.setDefaultLocale(Locale.FRENCH);
        Assert.assertEquals("value1fr", i18nService.get("key1", null));
        Assert.assertEquals("value2", i18nService.get("key2", null));
        Assert.assertEquals("value3fr", i18nService.get("key3", null));
        Assert.assertEquals("key4", i18nService.get("key4", null));
        
        Assert.assertEquals("value1fr", i18nService.get("key1", Locale.ENGLISH));
        Assert.assertEquals("value2", i18nService.get("key2", Locale.ENGLISH));
        Assert.assertEquals("value3fr", i18nService.get("key3", Locale.ENGLISH));
        Assert.assertEquals("key4", i18nService.get("key4", Locale.ENGLISH));
    } 
}
