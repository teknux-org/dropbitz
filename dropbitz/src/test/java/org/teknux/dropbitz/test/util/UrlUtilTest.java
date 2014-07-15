package org.teknux.dropbitz.test.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.teknux.dropbitz.util.UrlUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class UrlUtilTest {

    @Mock
    private ServletContext servletContext  = mock(ServletContext.class);

    @Test(expected = NullPointerException.class) 
    public void test01Error() {
        UrlUtil.getAbsoluteUrl(null, null);
    }
    
    @Test
    public void test02Root() {
        when(servletContext.getContextPath()).thenReturn("");
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));

        when(servletContext.getContextPath()).thenReturn("/");
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
    }

    @Test
    public void test03NotRoot() {
        when(servletContext.getContextPath()).thenReturn("notroot");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
        
        when(servletContext.getContextPath()).thenReturn("/notroot");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
        
        when(servletContext.getContextPath()).thenReturn("notroot/");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
        
        when(servletContext.getContextPath()).thenReturn("/notroot/");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
    }
}
