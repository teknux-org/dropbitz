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

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.teknux.dropbitz.config.FreemarkerConfig;
import org.teknux.dropbitz.exception.I18nServiceException;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.service.I18nService;
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

    @Mock
    private ServletContext servletContext = mock(ServletContext.class);
        
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
}
