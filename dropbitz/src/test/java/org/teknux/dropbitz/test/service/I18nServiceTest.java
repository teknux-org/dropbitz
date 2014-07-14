package org.teknux.dropbitz.test.service;

import java.util.Locale;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.teknux.dropbitz.exception.I18nServiceException;
import org.teknux.dropbitz.service.I18nService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class I18nServiceTest {
    
    @Test
    public void test01BadConfiguration() throws I18nServiceException  {
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
    public void test02KeyExistsDefaultEn() throws I18nServiceException  {
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
