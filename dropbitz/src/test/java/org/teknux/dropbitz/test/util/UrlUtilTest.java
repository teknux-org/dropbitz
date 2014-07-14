package org.teknux.dropbitz.test.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.teknux.dropbitz.test.fake.FakeServletContext;
import org.teknux.dropbitz.util.UrlUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UrlUtilTest {

    private FakeServletContext servletContext;

    @Before
    public void initServetContext() {
        servletContext = new FakeServletContext();
    }

    @Test(expected = NullPointerException.class) 
    public void test01Error() {
        UrlUtil.getAbsoluteUrl(null, null);
    }
    
    @Test
    public void test02Root() {
        servletContext.setContextPath("");
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));

        servletContext.setContextPath("/");
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
    }

    @Test
    public void test03NotRoot() {
        servletContext.setContextPath("notroot");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
        
        servletContext.setContextPath("/notroot");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
        
        servletContext.setContextPath("notroot/");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
        
        servletContext.setContextPath("/notroot/");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
    }
}
