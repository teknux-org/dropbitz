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

package org.teknux.dropbitz.test.freemarker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.config.FreemarkerConfig;
import org.teknux.dropbitz.exception.I18nServiceException;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.provider.AuthenticationHelper;
import org.teknux.dropbitz.service.I18nService;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.II18nService;
import org.teknux.dropbitz.service.IServiceManager;
import org.teknux.dropbitz.util.DropBitzServlet;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class FreemarkerTest {

    private static final String RESOURCE_BASE_NAME = "i18n.dropbitz";
    
    private static final String MODEL_NAME_ATTRIBUTE = "model";
    private final static String VIEWS_PATH = "/views";
    private static final String VIEW_EXTENSION = ".ftl";

    private ServletContext servletContext = mock(ServletContext.class);
    private HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        
    private FreemarkerConfig jerseyFreemarkerConfig;

    @Before
    public void initJerseyFreemarker() throws ServiceException {
        // Init Freemarker
        try {
            jerseyFreemarkerConfig = new FreemarkerConfig();
        } catch (TemplateModelException e) {
            throw new ServiceException(e);
        }
    }

    private String resolve(String viewName, IModel model) throws IOException, TemplateException {
        if (model == null) {
            model = new Model();
        }

        Template template = jerseyFreemarkerConfig.getTemplate(Objects.requireNonNull(VIEWS_PATH, "viewsPath can not be null")
                + Objects.requireNonNull(viewName, "viewName can not be null") + VIEW_EXTENSION);
        Writer writer = new StringWriter();

        model.setServletContext(servletContext);
        model.setHttpServletRequest(servletRequest);

        Map<String, IModel> map = new HashMap<String, IModel>();
        map.put(MODEL_NAME_ATTRIBUTE, model);

        template.process(map, writer);

        return writer.toString();
    }

    @Test
    public void test01Simple() throws IOException, TemplateException {
        Assert.assertEquals("simple", resolve("/simple", null));
    }

    @Test
    public void test02Model() throws IOException, TemplateException {
        Assert.assertEquals("testModel", resolve("/model", new FakeModel("testModel")));
    }

    @Test
    public void test03UrlHelper() throws IOException, TemplateException {
        when(servletContext.getContextPath()).thenReturn("");
        Assert.assertEquals("/", resolve("/urlHelperRoute", null));
        Assert.assertEquals("/url", resolve("/urlHelper", new FakeModel("url")));

        when(servletContext.getContextPath()).thenReturn("/root");
        Assert.assertEquals("/root", resolve("/urlHelperRoute", null));
        Assert.assertEquals("/root/url", resolve("/urlHelper", new FakeModel("url")));
    }
    
    @Test
    public void test04I18nHelper() throws I18nServiceException, IOException, TemplateException  {
        I18nService i18nService = new I18nService();
        i18nService.start(Locale.ENGLISH, RESOURCE_BASE_NAME);
        
        IServiceManager serviceManager = mock(IServiceManager.class);
        
        when(serviceManager.getService(II18nService.class)).thenReturn(i18nService);
        when(servletContext.getAttribute(DropBitzServlet.CONTEXT_ATTRIBUTE_SERVICE_MANAGER)).thenReturn(serviceManager);

        Assert.assertEquals("value1", resolve("/i18nHelper", null));
        
        i18nService.stop();
        i18nService.start(Locale.FRENCH, RESOURCE_BASE_NAME);
        
        Assert.assertEquals("value1fr", resolve("/i18nHelper", null));
    }

    @Test
    public void test05UserHelper() throws IOException, TemplateException {
        //setup
        IConfigurationService configurationService = mock(IConfigurationService.class);
        Configuration configuration = mock(Configuration.class);
        when(configurationService.getConfiguration()).thenReturn(configuration);
        IServiceManager serviceManager = mock(IServiceManager.class);
        when(serviceManager.getService(IConfigurationService.class)).thenReturn(configurationService);
        when(servletContext.getAttribute(DropBitzServlet.CONTEXT_ATTRIBUTE_SERVICE_MANAGER)).thenReturn(serviceManager);

        HttpSession session = mock(HttpSession.class);
        when(servletRequest.getSession()).thenReturn(session);
        when(servletRequest.getServletContext()).thenReturn(servletContext);

        final String view = "/authHelper";

        //test 1
        when(configuration.getSecureId()).thenReturn("");
        when(session.getAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_USER)).thenReturn(Boolean.FALSE);
        Assert.assertEquals("false|true", resolve(view, null));

        //test 2
        when(configuration.getSecureId()).thenReturn("");
        when(session.getAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_USER)).thenReturn(Boolean.TRUE);
        Assert.assertEquals("true|true", resolve(view, null));

        //test 3
        when(configuration.getSecureId()).thenReturn("123");
        when(session.getAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_USER)).thenReturn(Boolean.FALSE);
        Assert.assertEquals("false|false", resolve(view, null));

        //test 4
        when(configuration.getSecureId()).thenReturn("123");
        when(session.getAttribute(AuthenticationHelper.SESSION_ATTRIBUTE_USER)).thenReturn(Boolean.TRUE);
        Assert.assertEquals("true|true", resolve(view, null));
    }
}
